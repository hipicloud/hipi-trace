package com.hipi.code.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hipi
 * ClassName:CataLogueQuery.java
 * date:2022-07-04 14:25
 * Description: 目录查询封装
 */
@Data
@ApiModel(value = "CataLogueQuery对象", description = "目录查询封装")
public class CataLogueQuery {
    @ApiModelProperty("上级ID")
    private String parentId;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("查询等级,不传默认查询最后一级")
    private String grade;

}
