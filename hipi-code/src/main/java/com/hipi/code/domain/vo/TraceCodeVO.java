package com.hipi.code.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hipi
 * ClassName:TraceCodeVO.java
 * date:2022-07-23 12:23
 * Description: 生码记录
 */
@Data
@NoArgsConstructor
public class TraceCodeVO {

    @ApiModelProperty("品类名称")
    private String categoryName;

    @ApiModelProperty("批次号")
    private String batchCode;

    @ApiModelProperty("渠道商")
    private String distributorName;

    @ApiModelProperty("码类型")
    private String codeType;

    @ApiModelProperty("字符长度")
    private String charLength;

    @ApiModelProperty("字符类型")
    private String charType;

    @ApiModelProperty("生成数量")
    private String genNumber;

}
