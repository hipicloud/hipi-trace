package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品批次表
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@TableName("tb_trace_batch")
@ApiModel(value = "TraceBatch对象", description = "商品批次表")
@Data
public class TraceBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "batch_id", type = IdType.ASSIGN_ID)
    private String batchId;

    @ApiModelProperty("批次号")
    private String batchCode;

    @ApiModelProperty("品类管理ID")
    private String categoryManageId;

    @ApiModelProperty("状态：1：激活，2：冻结")
    private String status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    @ApiModelProperty("品类管理名称")
    @TableField(exist = false)
    private String categoryManageName;

    @ApiModelProperty("生成二维码数量")
    @TableField(exist = false)
    private Integer genCodeNumber;

    @ApiModelProperty("配置信息")
    @TableField(exist = false)
    private List<TraceBatchTraceability> configList;

    @TableField(exist = false)
    @ApiModelProperty("预览二维码")
    private String qrCode;

}
