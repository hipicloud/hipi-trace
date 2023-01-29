package com.hipi.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.constant.CatalogueConstant;
import com.hipi.code.domain.CatalogueForm;
import com.hipi.code.domain.SupplierManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.code.mapper.SupplierManageMapper;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.SupplierCatalogueService;
import com.hipi.code.service.SupplierManageService;
import com.hipi.common.core.domain.R;
import com.hipi.common.utils.uuid.Seq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_DISTRIBUTOR;

/**
 * <p>
 * 供应商管理表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@Service
@AllArgsConstructor
public class SupplierManageServiceImpl extends ServiceImpl<SupplierManageMapper, SupplierManage> implements SupplierManageService {
    private final SupplierCatalogueService supplierCatalogueService;
    private final CatalogueFormService catalogueFormService;

    /**
     * 分页查询
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage <SupplierManage>}
     */
    @Override
    public IPage<SupplierManage> selectPage(Page page, ManageQuery query) {
        Page<SupplierManage> resultPage = baseMapper.selectPage(page, Wrappers.<SupplierManage>lambdaQuery()
                .like(StrUtil.isNotBlank(query.getCode()), SupplierManage::getSupplierCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), SupplierManage::getSupplierName, query.getName()));
        List<SupplierManage> list = resultPage.getRecords();
        for (SupplierManage supplierManage : list) {
            CatalogueVO catalogueVO = supplierCatalogueService.selectTreeByLastId(supplierManage.getSupplierCatalogueId());
            supplierManage.setSupplierCatalogueName(catalogueVO.getLevelFourNameJoin());
        }
        resultPage.setRecords(list);
        return resultPage;
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    @Override
    public R insert(SupplierManage entity) {
        entity.setSupplierCatalogueCode(CatalogueConstant.ADD_SUPPLIER_CATALOGUE_PRE + Seq.getId());
        int insert = baseMapper.insert(entity);
        return R.toResult(insert);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    @Override
    public R updateEntity(SupplierManage entity) {
        int update = baseMapper.updateById(entity);
        return R.toResult(update);
    }

    /**
     * 删除
     *
     * @param id id
     * @return {@link R}
     */
    @Override
    public R deleteById(String id) {
        int delete = baseMapper.deleteById(id);
        return R.toResult(delete);
    }

    /**
     * 根据id查询
     *
     * @param id id
     * @return {@link SupplierManage}
     */
    @Override
    public SupplierManage selectById(String id) {
        SupplierManage entity = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(entity.getSupplierCatalogueId())) {
            CatalogueVO catalogueVO = supplierCatalogueService.selectTreeByLastId(entity.getSupplierCatalogueId());
            entity.setSupplierCatalogueName(catalogueVO.getLevelFourNameJoin());
            List<CatalogueForm> formList = catalogueFormService.getCatalogueByDataId(entity.getSupplierCatalogueId(),FORM_TYPE_DISTRIBUTOR);
            entity.setCatalogueFormList(formList);
        }
        return entity;
    }
}
