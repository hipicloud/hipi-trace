package com.hipi.code.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hipi
 * ClassName:ManageQuery.java
 * date:2022-07-19 16:59
 * Description: 查询二维码记录
 */
@Data
@ApiModel(value = "生码记录", description = "生码记录")
@NoArgsConstructor
@AllArgsConstructor
public class TraceQuery {
    @ApiModelProperty("品类名称")
    private String categoryName;
    @ApiModelProperty("批次号")
    private String batchCode;

    private long current;

    private long size;

}
