package com.hipi.code.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hipi
 * ClassName:GoodsInfoDTO.java
 * date:2023-01-08 10:08
 * Description:
 */
@Data
public class GoodsInfoDTO {
    @ApiModelProperty("商品ID")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;
}
