package com.hipi.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.constant.CatalogueConstant;
import com.hipi.code.domain.CatalogueForm;
import com.hipi.code.domain.CategoryManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.code.mapper.CategoryManageMapper;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.CategoryCatalogueService;
import com.hipi.code.service.CategoryManageService;
import com.hipi.common.core.domain.R;
import com.hipi.common.utils.uuid.Seq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_CATEGORY;

/**
 * <p>
 * 品类管理表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@Service
@AllArgsConstructor
public class CategoryManageServiceImpl extends ServiceImpl<CategoryManageMapper, CategoryManage> implements CategoryManageService {
    private final CategoryCatalogueService categoryCatalogueService;
    private final CatalogueFormService catalogueFormService;

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    @Override
    public IPage<CategoryManage> selectPage(Page page, ManageQuery query) {
        Page<CategoryManage> resultPage = baseMapper.selectPage(page, Wrappers.<CategoryManage>lambdaQuery()
                .like(StrUtil.isNotBlank(query.getCode()), CategoryManage::getCategoryCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), CategoryManage::getCategoryName, query.getName()));
        List<CategoryManage> list = resultPage.getRecords();
        for (CategoryManage categoryManage : list) {
            CatalogueVO catalogueVO = categoryCatalogueService.selectTreeByLastId(categoryManage.getCategoryCatalogueId());
            categoryManage.setCategoryCatalogueName(catalogueVO.getLevelFourNameJoin());
        }
        resultPage.setRecords(list);
        return resultPage;
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @Override
    public R insert(CategoryManage entity) {
        entity.setCategoryCatalogueCode(CatalogueConstant.ADD_CATEGORY_CATALOGUE_PRE + Seq.getId());
        int insert = baseMapper.insert(entity);
        return R.toResult(insert);
    }

    /**
     * 更新实体
     *
     * @param entity 实体
     * @return {@link R}
     */
    @Override
    public R updateEntity(CategoryManage entity) {
        int update = baseMapper.updateById(entity);
        return R.toResult(update);
    }

    /**
     * 删除
     *
     * @param id
     * @return
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
     * @return {@link CategoryManage}
     */
    @Override
    public CategoryManage selectById(String id) {
        CategoryManage entity = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(entity.getCategoryCatalogueId())) {
            CatalogueVO catalogueVO = categoryCatalogueService.selectTreeByLastId(entity.getCategoryCatalogueId());
            entity.setCategoryCatalogueName(catalogueVO.getLevelFourNameJoin());
            List<CatalogueForm> formList = catalogueFormService.getCatalogueByDataId(entity.getCategoryCatalogueId(),FORM_TYPE_CATEGORY);
            entity.setCatalogueFormList(formList);
        }
        return entity;
    }

    @Override
    public List<CategoryManage> selectList(ManageQuery query) {
        List<CategoryManage> selectList = baseMapper.selectList(Wrappers.<CategoryManage>lambdaQuery()
                .like(StrUtil.isNotBlank(query.getCode()), CategoryManage::getCategoryCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), CategoryManage::getCategoryName, query.getName()));
        return selectList;
    }
}
