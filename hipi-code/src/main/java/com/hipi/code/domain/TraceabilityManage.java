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
 * 溯源环节管理表
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@TableName(value = "tb_traceability_manage", autoResultMap = true)
@ApiModel(value = "TraceabilityManage对象", description = "溯源环节管理表")
@Data
public class TraceabilityManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("溯源环节目录编码")
    private String traceabilityCatalogueCode;

    @ApiModelProperty("溯源环节目录id")
    private String traceabilityCatalogueId;

    @ApiModelProperty("溯源环节目录名称")
    @TableField(exist = false)
    private String traceabilityCatalogueName;

    @ApiModelProperty("溯源环节编码")
    private String traceabilityCode;

    @ApiModelProperty("溯源环节名称")
    private String traceabilityName;

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
