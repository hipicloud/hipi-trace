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
 * 品类管理表
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@TableName(value = "tb_category_manage", autoResultMap = true)
@ApiModel(value = "CategoryManage对象", description = "品类管理表")
@Data
public class CategoryManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("品类目录编码")
    private String categoryCatalogueCode;

    @ApiModelProperty("品类目录id")
    private String categoryCatalogueId;

    @ApiModelProperty("品类目录名称")
    @TableField(exist = false)
    private String categoryCatalogueName;

    @ApiModelProperty("品类编码")
    private String categoryCode;

    @ApiModelProperty("品类名称")
    private String categoryName;

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

    @ApiModelProperty("表单字段")
    @TableField(exist = false)
    private List<CatalogueForm> catalogueFormList;

}
