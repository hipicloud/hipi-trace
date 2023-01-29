package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hipi
 * ClassName:CatalogueForm.java
 * date:2023-01-04 16:20
 * Description:
 */
@TableName("tb_catalogue_form")
@ApiModel(value = "CatalogueForm对象", description = "目录表单字段模板表")
@Data
public class CatalogueForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("关联数据ID")
    private String dataId;

    @ApiModelProperty("数据类型：1：品类目录，2：供应商目录，3：渠道商目录，4：溯源节点目录")
    private String dataType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("类型：1：文本，2：打文本，3：图片，4：视频")
    private String type;

    @ApiModelProperty("排序")
    private Integer sort;

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
}
