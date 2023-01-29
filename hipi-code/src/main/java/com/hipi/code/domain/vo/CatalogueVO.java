package com.hipi.code.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hipi
 * ClassName:CatalogueVO.java
 * date:2022-07-23 12:23
 * Description:
 */
@Data
public class CatalogueVO {
    @ApiModelProperty("一级id")
    private String firstId;
    @ApiModelProperty("二级id")
    private String secondId;
    @ApiModelProperty("三级id")
    private String threeId;
    @ApiModelProperty("四级id")
    private String fourceId;
    @ApiModelProperty("四级名称拼接")
    private String levelFourNameJoin;
    @ApiModelProperty("四级编号拼接")
    private String levelFourCodeJoin;

}
