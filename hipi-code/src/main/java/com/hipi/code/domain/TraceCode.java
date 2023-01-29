package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 品类溯源码表
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@TableName("tb_trace_code")
@ApiModel(value = "TraceCode对象", description = "品类溯源码表")
@Data
public class TraceCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键 ")
    @TableId(value = "code_id", type = IdType.ASSIGN_ID)
    private String codeId;

    @ApiModelProperty("品类管理表ID")
    private String categoryManageId;

    @ApiModelProperty("批次号")
    private String batchCode;

    @ApiModelProperty("渠道商管理表ID")
    private String distributorManageId;

    @ApiModelProperty("码类型：1：明码，2：暗码")
    private String codeType;

    @ApiModelProperty("验证码（暗码使用）")
    private String captcha;

    @ApiModelProperty("生码规则：1：纯数字，2：纯字母，3：混合")
    private String genCodeRule;

    @ApiModelProperty("字符长度")
    private Integer charLength;

    @ApiModelProperty("字符类型")
    private String charType;

    @ApiModelProperty("生成数量")
    private Integer genNumber;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;


}
