package com.im.flashcomms.common.user.cache;

import com.im.flashcomms.common.user.dao.ItemConfigDao;
import com.im.flashcomms.common.user.domain.entity.ItemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 物品缓存
 */
@Component
public class ItemCache {

    @Autowired
    private ItemConfigDao itemConfigDao;

    //获取缓存
    @Cacheable(cacheNames =  "item",key = "'itemsByType:'+#itemType")
    public List<ItemConfig> getByType(Integer itemType){
       return  itemConfigDao.getByType(itemType);
    }
}
