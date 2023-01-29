package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 二维码表
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@TableName("tb_trace_qr_code")
@ApiModel(value = "TraceQrCode对象", description = "二维码表")
@Data
public class TraceQrCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("品类溯源码表id")
    private String codeId;

    @ApiModelProperty("二维码")
    private String qrCode;

}
