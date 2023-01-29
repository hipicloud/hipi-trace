package com.hipi.code.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.Data;

/**
 * @author hipi
 * ClassName:BatchCodeExportDTO.java
 * date:2023-01-17 14:54
 * Description: 批次品类二维码导出
 */
@Data
@ContentRowHeight(100)
public class BatchCodeExportDTO {

    @ColumnWidth(70)
    @ExcelProperty("溯源码")
    private String codeUrl;

    @ColumnWidth(20)
    @ExcelProperty("二维码")
    private byte[] byteArray;
}
