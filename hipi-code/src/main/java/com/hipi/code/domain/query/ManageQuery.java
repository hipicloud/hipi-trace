package com.hipi.code.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hipi
 * ClassName:ManageQuery.java
 * date:2022-07-19 16:59
 * Description: 管理查询
 */
@Data
@ApiModel(value = "ManageQuery对象", description = "管理查询封装")
public class ManageQuery {
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;

}
