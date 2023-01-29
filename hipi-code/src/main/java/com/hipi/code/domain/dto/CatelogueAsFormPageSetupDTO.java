package com.hipi.code.domain.dto;

import lombok.Data;

/**
 * @author hipi
 * ClassName:AsUserFormPageSetupDTO.java
 * date:2022-06-30 15:29
 * Description: 用户表单页面设置
 */
@Data
public class CatelogueAsFormPageSetupDTO {
    /**
     * 标题
     */
    private String name;

    /**
     * 页面描述
     */
    private String details;

    /**
     * 个人中心 隐藏展示  0：false，1：true
     */
    private Boolean isPerson;

    /**
     * 返回按钮 隐藏展示  0：false，1：true
     */
    private Boolean isBack;

    /**
     * 高度
     */
    private Integer titleHeight;

    /**
     * 背景色
     */
    private String bgColor;

    /**
     * 背景图片
     */
    private String bgImg;
}
