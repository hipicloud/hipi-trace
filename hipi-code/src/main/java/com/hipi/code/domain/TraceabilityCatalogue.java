package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 溯源环节管理
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@TableName("tb_traceability_catalogue")
@ApiModel(value = "TraceabilityCatalogue对象", description = "溯源环节管理")
@Data
public class TraceabilityCatalogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父级ID")
    private String parentId;

    @ApiModelProperty("等级")
    private Integer grade;

    @ApiModelProperty("状态：0：启用，1：关闭")
    private Integer status;

    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer delFlag;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(update = "now()")
    private LocalDateTime updateTime;

    @ApiModelProperty("子集")
    @TableField(exist = false)
    private List<TraceabilityCatalogue> children;

    @ApiModelProperty("表单字段")
    @TableField(exist = false)
    private List<CatalogueForm> catalogueFormList;

}
