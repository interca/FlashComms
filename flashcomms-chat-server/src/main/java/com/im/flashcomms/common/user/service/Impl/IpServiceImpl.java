package com.im.flashcomms.common.user.service.Impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.im.flashcomms.common.common.domain.vo.resp.ApiResult;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.IpDetail;
import com.im.flashcomms.common.user.domain.entity.IpInfo;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.service.IpService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IpServiceImpl implements IpService {
    private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));

    @Autowired
    private UserDao userDao;

    /**
     * ip解析
     * @param id
     */
    @Override
    public void refreshIpDetailAsync(Long id) {
        executor.execute(() ->{
            User user = userDao.getById(id);
            IpInfo ipInfo = user.getIpInfo();
            if(Objects.isNull(ipInfo)){
                return;
            }
            String ip = ipInfo.needRefreshIp();
            if(StringUtils.isBlank(ip)){
                return;
            }
            IpDetail ipDetail = tryGetIpDetailOrNullTreeTimes(ip);
            if(Objects.nonNull(ipDetail)){
                ipInfo.refreshIpDetail(ipDetail);
                User update = new User();
                update.setId(user.getId());
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
            }
        });
    }



    private IpDetail tryGetIpDetailOrNullTreeTimes(String ip) {
        for(int i = 0 ; i < 3 ; i ++){
            IpDetail ipDetail = getIpDetailOrNUll(ip);
            if(Objects.nonNull(ipDetail)){
                return ipDetail;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("错误:{}",e);
            }
        }
        return null;
    }

    private IpDetail getIpDetailOrNUll(String ip) {
        String url =  "https://ip.taobao.com/outGetIpInfo?ip="+ip+"&accessKey=alibaba-inc";
        String s = HttpUtil.get(url);
        ApiResult apiResult = JSONUtil.toBean(s, ApiResult.class);
        IpDetail ipDetail  = (IpDetail) apiResult.getData();
        return ipDetail;
    }
}
