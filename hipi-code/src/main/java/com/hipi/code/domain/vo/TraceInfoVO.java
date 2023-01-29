package com.hipi.code.domain.vo;

import com.hipi.code.domain.CategoryManage;
import com.hipi.code.domain.TraceQrCodeLog;
import com.hipi.code.domain.TraceabilityManage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 终端溯源信息
 *
 * @author hipi
 * ClassName:TraceInfoVO.java
 * date:2022-07-23 12:23
 * Description:
 */
@Data
public class TraceInfoVO {

    @ApiModelProperty("扫码记录")
    private List<TraceQrCodeLog> traceQrCodeLogList;

    @ApiModelProperty("品类信息")
    private CategoryManage categoryManage;

    @ApiModelProperty("环节信息")
    private List<TraceabilityManage> traceabilityManageList;

}
