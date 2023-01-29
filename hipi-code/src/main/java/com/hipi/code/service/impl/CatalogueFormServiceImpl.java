package com.hipi.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.domain.CatalogueForm;
import com.hipi.code.mapper.CatalogueFormMapper;
import com.hipi.code.service.CatalogueFormService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hipi
 * ClassName:CatalogueFormServiceImpl.java
 * date:2023-01-04 16:28
 * Description:
 */
@Service
public class CatalogueFormServiceImpl extends ServiceImpl<CatalogueFormMapper, CatalogueForm> implements CatalogueFormService {
    /**
     * 根据关联主键ID数据查询表单字段
     *
     * @param dataId 数据ID
     * @return {@link List}<{@link CatalogueForm}>
     */
    @Override
    public List<CatalogueForm> getCatalogueByDataId(String dataId, String dataType) {
        List<CatalogueForm> catalogueFormList = baseMapper.selectList(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, dataId)
                .eq(StrUtil.isNotBlank(dataType), CatalogueForm::getDataType, dataType)
                .orderByAsc(CatalogueForm::getSort));
        return catalogueFormList;
    }

}
