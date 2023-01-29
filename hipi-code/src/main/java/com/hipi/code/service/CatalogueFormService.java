package com.hipi.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.CatalogueForm;

import java.util.List;

/**
 * @author hipi
 * ClassName:CatalogueFormService.java
 * date:2023-01-04 16:28
 * Description:
 */
public interface CatalogueFormService extends IService<CatalogueForm> {
    /**
     * 根据关联主键ID数据查询表单字段
     * @param dataId 主键
     * @param dataType 类型
     * @return 结构
     */
    List<CatalogueForm> getCatalogueByDataId(String dataId, String dataType);

}
