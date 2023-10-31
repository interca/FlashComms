package com.im.flashcomms.common.common.event.listener;

import com.im.flashcomms.common.common.event.UserOnlineEvent;
import com.im.flashcomms.common.common.event.UserRegisterEvent;
import com.im.flashcomms.common.user.cache.UserCache;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.enums.IdempotentEnum;
import com.im.flashcomms.common.user.domain.enums.ItemEnum;
import com.im.flashcomms.common.user.domain.enums.UserActiveStatusEnum;
import com.im.flashcomms.common.user.service.Impl.PushService;
import com.im.flashcomms.common.user.service.IpService;
import com.im.flashcomms.common.user.service.adapter.WSAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserOnlineListener {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IpService ipService;

    @Autowired
    private UserCache userCache;

    @Autowired
    private WSAdapter wsAdapter;
    @Autowired
    private PushService pushService;

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        User user = event.getUser();
        userCache.online(user.getId(), user.getLastOptTime());
        //推送给所有在线用户，该用户登录成功
        pushService.sendPushMsg(wsAdapter.buildOnlineNotifyResp(event.getUser()));
    }
    /**
     * 在事物结束后才执行
     * @param event
     */
    @Async
    @TransactionalEventListener (classes = UserOnlineEvent.class)
    public void saveDb(UserOnlineEvent event){
        User user = event.getUser();
        User update = new User();
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setId(user.getId());
        //用户变为在线
        update.setActiveStatus(UserActiveStatusEnum.ONLINE.getType());
        userDao.updateById(update);
        //用户ip详情解析
        ipService.refreshIpDetailAsync(user.getId());
        userCache.userInfoChange(user.getId());
    }
}
