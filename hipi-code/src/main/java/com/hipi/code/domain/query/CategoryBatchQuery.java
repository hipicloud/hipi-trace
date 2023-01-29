package com.hipi.code.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hipi
 * ClassName:CategoryBatchQuery.java
 * date:2022-07-19 16:59
 * Description: 查询二维码记录
 */
@Data
@ApiModel(value = "品类批次", description = "品类批次")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBatchQuery {

    @ApiModelProperty("品类管理ID")
    private String categoryManageId;

}
