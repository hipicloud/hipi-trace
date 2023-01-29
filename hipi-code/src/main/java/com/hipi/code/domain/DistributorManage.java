package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 渠道商管理表
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@TableName(value = "tb_distributor_manage", autoResultMap = true)
@ApiModel(value = "DistributorManage对象", description = "渠道商管理表")
@Data
public class DistributorManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("渠道商目录编码")
    private String distributorCatalogueCode;

    @ApiModelProperty("渠道商目录id")
    private String distributorCatalogueId;

    @ApiModelProperty("渠道商目录名称")
    @TableField(exist = false)
    private String distributorCatalogueName;

    @ApiModelProperty("渠道商编码")
    private String distributorCode;

    @ApiModelProperty("渠道商名称")
    private String distributorName;

    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer delFlag;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(update = "now()", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "字段数据JSON")
    private Map<String, Object> dataJson;

    @TableField(exist = false)
    @ApiModelProperty(value = "销售渠道信息")
    private List<DistributorGoods> goodsList;

    @ApiModelProperty("表单字段")
    @TableField(exist = false)
    private List<CatalogueForm> catalogueFormList;
}
