package com.hipi.code.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.constant.CatalogueConstant;
import com.hipi.code.domain.CatalogueForm;
import com.hipi.code.domain.CategoryCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.code.mapper.CategoryCatalogueMapper;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.CategoryCatalogueService;
import com.hipi.common.core.domain.R;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_CATEGORY;

/**
 * <p>
 * 品类目录管理 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@Service
@AllArgsConstructor
public class CategoryCatalogueServiceImpl extends ServiceImpl<CategoryCatalogueMapper, CategoryCatalogue> implements CategoryCatalogueService {

    private CatalogueFormService catalogueFormService;

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link List <CategoryCatalogue>}
     */
    @Override
    public List<CategoryCatalogue> selectTreeList(CataLogueQuery query) {
        // 排除第四级
        List<CategoryCatalogue> catalogueList = baseMapper.selectList(Wrappers.lambdaQuery());
        // 父及
        List<CategoryCatalogue> firstList = new ArrayList<>(50);
        // 子集
        List<CategoryCatalogue> otherList = new ArrayList<>(200);
        String root = "0";
        catalogueList.stream().forEach(item -> {
            if (root.equals(item.getParentId())) {
                firstList.add(item);
                return;
            }
            otherList.add(item);
        });
        // 递归获取子集
        recursionTreeList(firstList, otherList);
        return firstList;
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    @Override
    public R insert(CategoryCatalogue entity) {
        // 新增
        int insert = baseMapper.insert(entity);
        List<CatalogueForm> catalogueFormList = entity.getCatalogueFormList();
        if (CollUtil.isNotEmpty(catalogueFormList)) {
            for (CatalogueForm catalogueForm : catalogueFormList) {
                catalogueForm.setDataId(entity.getId());
                catalogueForm.setDataType(FORM_TYPE_CATEGORY);
            }
            catalogueFormService.saveBatch(catalogueFormList);
        }
        return R.toResult(insert);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    @Override
    public R updateEntity(CategoryCatalogue entity) {
        int update = baseMapper.updateById(entity);
        // 删除旧表单
        catalogueFormService.remove(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, entity.getId())
                .eq(CatalogueForm::getDataType, FORM_TYPE_CATEGORY));
        List<CatalogueForm> catalogueFormList = entity.getCatalogueFormList();
        if (CollUtil.isNotEmpty(catalogueFormList)) {
            for (CatalogueForm catalogueForm : catalogueFormList) {
                catalogueForm.setId(null);
                catalogueForm.setDataId(entity.getId());
                catalogueForm.setDataType(FORM_TYPE_CATEGORY);
            }
            catalogueFormService.saveBatch(catalogueFormList);
        }
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
        CategoryCatalogue entity = baseMapper.selectById(id);
        // 存在子集禁止删除
        CategoryCatalogue childrenEntity = baseMapper.selectOne(Wrappers.<CategoryCatalogue>lambdaQuery()
                .eq(CategoryCatalogue::getParentId, id)
                .last("limit 1"));
        if (ObjectUtil.isNotEmpty(childrenEntity)) {
            return R.fail("当前节点存在子集，禁止删除");
        }
        int delete = baseMapper.deleteById(id);
        // 删除表单
        catalogueFormService.remove(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, entity.getId())
                .eq(CatalogueForm::getDataType, FORM_TYPE_CATEGORY));
        return R.toResult(delete);
    }

    /**
     * 查询最后一级
     *
     * @param query 查询
     * @param page  页面
     * @return {@link IPage<CategoryCatalogue>}
     */
    @Override
    public IPage<CategoryCatalogue> selectLastPage(Page<CategoryCatalogue> page, CataLogueQuery query) {
        Page<CategoryCatalogue> resultPage = baseMapper.selectPage(page, Wrappers.<CategoryCatalogue>lambdaQuery()
                .eq(StrUtil.isBlank(query.getGrade()), CategoryCatalogue::getGrade, CatalogueConstant.GRADE_FOUR)
                .eq(StrUtil.isNotBlank(query.getGrade()), CategoryCatalogue::getGrade, query.getGrade())
                .like(StrUtil.isNotBlank(query.getCode()), CategoryCatalogue::getCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), CategoryCatalogue::getName, query.getName())
                .eq(CategoryCatalogue::getParentId, query.getParentId()));
        return resultPage;
    }

    /**
     * 选择通过id
     *
     * @param id id
     * @return {@link CategoryCatalogue}
     */
    @Override
    public CategoryCatalogue selectById(String id) {
        CategoryCatalogue catalogue = baseMapper.selectById(id);
        List<CatalogueForm> catalogueFormList = catalogueFormService.getCatalogueByDataId(id,FORM_TYPE_CATEGORY);
        catalogue.setCatalogueFormList(catalogueFormList);
        return catalogue;
    }

    /**
     * 根据最后一个节点id查询对应树形结构
     *
     * @param id id
     * @return {@link CatalogueVO}
     */
    @Override
    public CatalogueVO selectTreeByLastId(String id) {
        CatalogueVO vo = new CatalogueVO();
        recursionTreeByLastId(vo, id);
        return vo;
    }

    /**
     * 根据子集ID查询上级信息——递归
     *
     * @param vo
     * @param parentId 父及ID
     */
    private void recursionTreeByLastId(CatalogueVO vo, String parentId) {
        CategoryCatalogue entity = baseMapper.selectById(parentId);
        if (ObjectUtil.isEmpty(entity)) {
            return;
        }
        if (CatalogueConstant.GRADE_ONE.equals(entity.getGrade())) {
            vo.setFirstId(entity.getId());
            vo.setLevelFourCodeJoin(entity.getCode() + (StrUtil.isNotBlank(vo.getLevelFourCodeJoin()) ? vo.getLevelFourCodeJoin() : ""));
            vo.setLevelFourNameJoin(entity.getName() + (StrUtil.isNotBlank(vo.getLevelFourNameJoin()) ? vo.getLevelFourNameJoin() : ""));
            return;
        }
        vo.setLevelFourCodeJoin(CharSequenceUtil.isNotBlank(vo.getLevelFourCodeJoin()) ? "-" + entity.getCode() + vo.getLevelFourCodeJoin() : "-" + entity.getCode());
        vo.setLevelFourNameJoin(CharSequenceUtil.isNotBlank(vo.getLevelFourNameJoin()) ? "-" + entity.getName() + vo.getLevelFourNameJoin() : "-" + entity.getName());
        if (CatalogueConstant.GRADE_TWO.equals(entity.getGrade())) {
            vo.setSecondId(entity.getId());
        } else if (CatalogueConstant.GRADE_THREE.equals(entity.getGrade())) {
            vo.setThreeId(entity.getId());
        } else if (CatalogueConstant.GRADE_FOUR.equals(entity.getGrade())) {
            vo.setFourceId(entity.getId());
        }
        recursionTreeByLastId(vo, entity.getParentId());
    }

    /**
     * 递归树列表
     *
     * @param firstList 第一个列表
     * @param otherList 其他列表
     */
    private void recursionTreeList(List<CategoryCatalogue> firstList, List<CategoryCatalogue> otherList) {
        firstList.forEach(entity -> {
            // 获取子集
            List<CategoryCatalogue> childrenList = otherList.stream()
                    .filter(child -> entity.getId().equals(child.getParentId()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(childrenList)) {
                entity.setChildren(childrenList);
                recursionTreeList(childrenList, otherList);
            }
        });
    }
}
