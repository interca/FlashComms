package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.common.annotation.RedissonLock;
import com.im.flashcomms.common.common.event.UserBlackEvent;
import com.im.flashcomms.common.common.event.UserRegisterEvent;
import com.im.flashcomms.common.common.exception.BusinessException;
import com.im.flashcomms.common.common.exception.CommonErrorEnum;
import com.im.flashcomms.common.user.cache.ItemCache;
import com.im.flashcomms.common.user.dao.BlackDao;
import com.im.flashcomms.common.user.dao.ItemConfigDao;
import com.im.flashcomms.common.user.dao.UserBackpackDao;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.domain.entity.Black;
import com.im.flashcomms.common.user.domain.entity.ItemConfig;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.entity.UserBackpack;
import com.im.flashcomms.common.user.domain.enums.BlackTypeEnum;
import com.im.flashcomms.common.user.domain.enums.ItemEnum;
import com.im.flashcomms.common.user.domain.enums.ItemTypeEnum;
import com.im.flashcomms.common.user.domain.vo.req.BlackReq;
import com.im.flashcomms.common.user.domain.vo.resp.BadgeResp;
import com.im.flashcomms.common.user.domain.vo.resp.UserInfoResp;
import com.im.flashcomms.common.user.service.UserService;
import com.im.flashcomms.common.user.service.adapter.UserAdapter;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.Cacheable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Autowired
    private UserBackpackDao userBackpackDao;


    @Autowired
    private ItemCache itemCache;


    @Autowired
    private ItemConfigDao itemConfigDao;


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    private BlackDao blackDao;

    /**
     * 用户注册
     * @param insert
     * @return
     */
    @Override
    public Long  register(User insert) {
        boolean save = userDao.save(insert);
        //用户注册事件 发放改名卡
        System.out.println("改名卡1");
        //发布事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this,insert));
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
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        if(Objects.nonNull(oldUser)){
           throw  new BusinessException(CommonErrorEnum.BUSINESS_ERROR.getCode(),"用户名已经被抢占");
        }
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        if(Objects.isNull(firstValidItem)){
            throw  new BusinessException(CommonErrorEnum.BUSINESS_ERROR.getCode(),"改名卡不够了");
        }
        //使用改名卡
        boolean b = userBackpackDao.userItem(firstValidItem);
        if(b){
            userDao.modifyName(uid,name);
        }
    }





    /**
     * 获取用户徽章
     * @param uid
     * @return
     */
    @Override
    public List<BadgeResp> badges(Long uid) {
        //查询所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        //查询用户的徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        //用户佩戴的
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs,backpacks,user);
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        //确保有徽章
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, itemId);
        if(firstValidItem == null){
          throw new BusinessException(CommonErrorEnum.BUSINESS_ERROR.getCode(),"没有这个徽章");
        }
        //确保是徽章
        ItemConfig itemConfig = itemConfigDao.getById(firstValidItem.getItemId());
        if(itemConfig.getType() != 2){
            throw new BusinessException(CommonErrorEnum.BUSINESS_ERROR.getCode(),"不是徽章");
        }
        userDao.wearingBadge(uid,itemId);
    }

    @Override
    @Transactional
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black user = new Black();
        user.setType(BlackTypeEnum.UID.getType());
        user.setTarget(uid.toString());
        blackDao.save(user);
        User byId = userDao.getById(uid);
        //拉黑ip
        blackIp(byId.getIpInfo().getUpdateIp());
        blackIp(byId.getIpInfo().getCreateIp());
        applicationEventPublisher.publishEvent(new UserBlackEvent(this,byId));
    }

    private void blackIp(String ip) {
        if(StringUtils.isBlank(ip)){
            return;
        }
        Black user = new Black();
        user.setType(BlackTypeEnum.IP.getType());
        user.setTarget(ip);
        blackDao.save(user);
    }
}
