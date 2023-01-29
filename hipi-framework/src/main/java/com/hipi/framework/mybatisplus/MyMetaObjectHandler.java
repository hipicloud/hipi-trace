package com.hipi.framework.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hipi.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author hipi
 * ClassName:MyMetaObjectHandler.java
 * date:2022-06-30 12:00
 * Description: mybatis-plus自动填充配置
 */
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("新增赋值");
        // 或者
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        String username = "";
        try {
            username = SecurityUtils.getUsername();
        } catch (Exception e) {
            username = "JOB";
        }
        this.strictInsertFill(metaObject, "createBy", String.class, username);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("修改赋值");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
