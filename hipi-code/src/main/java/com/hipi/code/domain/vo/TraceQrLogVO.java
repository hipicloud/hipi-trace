package com.hipi.code.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hipi
 * ClassName:TraceQrLogVO.java
 * date:2022-07-23 12:23
 * Description: 生码记录
 */
@Data
@NoArgsConstructor
public class TraceQrLogVO {

    @ApiModelProperty("品类名称")
    private String categoryName;

    @ApiModelProperty("批次号")
    private String batchCode;

    @ApiModelProperty("溯源码")
    private String traceCode;

    @ApiModelProperty("二维码")
    private String qrCode;

    @ApiModelProperty("状态")
    private String status;
}
