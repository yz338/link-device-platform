package com.devicemanagement.module.device.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DeviceCategoryFillConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.strictInsertFill(metaObject,
                "createTime",
                Date.class,
                date);
        this.strictInsertFill(metaObject
                , "updateTime"
                , Date.class
                , date);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date date = new Date();
        this.strictUpdateFill(metaObject,"updateTime",Date.class,date);
    }
}
