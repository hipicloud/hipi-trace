package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 品类批次溯源环节表
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@TableName("tb_trace_batch_traceability")
@ApiModel(value = "TraceBatchTraceability对象", description = "品类批次溯源环节表")
@Data
public class TraceBatchTraceability implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("商品批次表主键")
    private String batchId;

    @ApiModelProperty("序号")
    private Integer sort;

    @ApiModelProperty("溯源环节管理表ID")
    private String traceabilityManageId;

    @ApiModelProperty("溯源环节管理名称")
    @TableField(exist = false)
    private String traceabilityManageName;

    @ApiModelProperty("表单字段")
    @TableField(exist = false)
    private List<CatalogueForm> catalogueFormList;

    @ApiModelProperty("环节信息")
    @TableField(exist = false)
    private List<TraceabilityManage> traceabilityManageList;


}
