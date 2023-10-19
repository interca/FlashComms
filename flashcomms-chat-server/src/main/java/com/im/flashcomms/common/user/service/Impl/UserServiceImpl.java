package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.user.dao.UserBackpackDao;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.enums.ItemEnum;
import com.im.flashcomms.common.user.domain.vo.req.ModifyNameReq;
import com.im.flashcomms.common.user.domain.vo.resp.UserInfoResp;
import com.im.flashcomms.common.user.service.UserService;
import com.im.flashcomms.common.user.service.adapter.UserAdapter;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Autowired
    private UserBackpackDao userBackpackDao;


    /**
     * 用户注册
     * @param insert
     * @return
     */
    @Override
    @Transactional
    public Long  register(User insert) {
        boolean save = userDao.save(insert);
        //用户注册事件 todo
        return insert.getId();
    }

    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer countByValidItemId = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user,countByValidItemId);
    }


    /**
     * 用户改名
     * @param uid
     * @param name
     */
    @Override
    public void modifyName(Long uid, String name) {

    }
}
