package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 二维码扫码记录
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@TableName("tb_trace_qr_code_log")
@ApiModel(value = "TraceQrCodeLog对象", description = "二维码扫码记录")
@Data
public class TraceQrCodeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键 ")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private String logId;

    @ApiModelProperty("二维码")
    private String qrCode;

    @ApiModelProperty("扫码时间")
    @TableField(fill = FieldFill.INSERT)
    private String scanTime;

    @ApiModelProperty("扫码经纬定位 例：1234,1234")
    private String coordinate;

    @ApiModelProperty("扫码位置 例：北京市西城区/河北省张家口市崇礼区")
    private String scanLocation;
}
