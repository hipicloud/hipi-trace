package com.hipi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hipi.framework.mybatisplus.MyMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hipi
 * ClassName:MybatisPlusConfig.java
 * date:2022-07-05 16:50
 * Description:
 */
@Configuration
@MapperScan("com.hipi.*.mapper")
public class MybatisPlusConfig {
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        System.out.println("初始化自动填充功能");
        return new MyMetaObjectHandler();
    }
}
