package com.hipi.code.domain.dto;

import com.hipi.code.domain.TraceBatchTraceability;
import lombok.Data;

import java.util.List;

/**
 * @author hipi
 * ClassName:BatchConfigDTO.java
 * date:2023-01-09 18:15
 * Description: 批次配置dto
 */
@Data
public class BatchConfigDTO {
    private String batchId;
    private List<TraceBatchTraceability> configList;
}
