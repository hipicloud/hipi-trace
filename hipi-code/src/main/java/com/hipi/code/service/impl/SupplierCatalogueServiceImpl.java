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
import com.hipi.code.domain.SupplierCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.code.mapper.SupplierCatalogueMapper;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.SupplierCatalogueService;
import com.hipi.common.core.domain.R;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_SUPPLIER;

/**
 * <p>
 * 供应商目录管理 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@Service
@AllArgsConstructor
public class SupplierCatalogueServiceImpl extends ServiceImpl<SupplierCatalogueMapper, SupplierCatalogue> implements SupplierCatalogueService {
    private final CatalogueFormService catalogueFormService;

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link List <SupplierCatalogue>}
     */
    @Override
    public List<SupplierCatalogue> selectTreeList(CataLogueQuery query) {
        // 排除第四级
        List<SupplierCatalogue> catalogueList = baseMapper.selectList(Wrappers.lambdaQuery());
        // 父及
        List<SupplierCatalogue> firstList = new ArrayList<>(50);
        // 子集
        List<SupplierCatalogue> otherList = new ArrayList<>(200);
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
    public R insert(SupplierCatalogue entity) {
        int insert = baseMapper.insert(entity);

        List<CatalogueForm> catalogueFormList = entity.getCatalogueFormList();
        if (CollUtil.isNotEmpty(catalogueFormList)) {
            for (CatalogueForm catalogueForm : catalogueFormList) {
                catalogueForm.setDataId(entity.getId());
                catalogueForm.setDataType(FORM_TYPE_SUPPLIER);
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
    public R updateEntity(SupplierCatalogue entity) {
        int update = baseMapper.updateById(entity);
        // 删除旧表单
        catalogueFormService.remove(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, entity.getId())
                .eq(CatalogueForm::getDataType, FORM_TYPE_SUPPLIER));
        List<CatalogueForm> catalogueFormList = entity.getCatalogueFormList();
        if (CollUtil.isNotEmpty(catalogueFormList)) {
            for (CatalogueForm catalogueForm : catalogueFormList) {
                catalogueForm.setId(null);
                catalogueForm.setDataId(entity.getId());
                catalogueForm.setDataType(FORM_TYPE_SUPPLIER);
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
        SupplierCatalogue entity = baseMapper.selectById(id);
        // 存在子集禁止删除
        SupplierCatalogue childrenEntity = baseMapper.selectOne(Wrappers.<SupplierCatalogue>lambdaQuery()
                .eq(SupplierCatalogue::getParentId, id)
                .last("limit 1"));
        if (ObjectUtil.isNotEmpty(childrenEntity)) {
            return R.fail("当前节点存在子集，禁止删除");
        }
        // 第四级则删除表单信息
        int delete = baseMapper.deleteById(id);
        // 删除旧表单
        catalogueFormService.remove(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, entity.getId())
                .eq(CatalogueForm::getDataType, FORM_TYPE_SUPPLIER));
        return R.toResult(delete);
    }

    /**
     * 查询最后一级
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage <SupplierCatalogue>}
     */
    @Override
    public IPage<SupplierCatalogue> selectLastPage(Page<SupplierCatalogue> page, CataLogueQuery query) {
        Page<SupplierCatalogue> resultPage = baseMapper.selectPage(page, Wrappers.<SupplierCatalogue>lambdaQuery()
                .eq(StrUtil.isBlank(query.getGrade()), SupplierCatalogue::getGrade, CatalogueConstant.GRADE_FOUR)
                .eq(StrUtil.isNotBlank(query.getGrade()), SupplierCatalogue::getGrade, query.getGrade())
                .like(StrUtil.isNotBlank(query.getCode()), SupplierCatalogue::getCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), SupplierCatalogue::getName, query.getName())
                .eq(SupplierCatalogue::getParentId, query.getParentId()));
        return resultPage;
    }

    /**
     * 选择通过id
     *
     * @param id id
     * @return {@link SupplierCatalogue}
     */
    @Override
    public SupplierCatalogue selectById(String id) {
        SupplierCatalogue catalogue = baseMapper.selectById(id);
        List<CatalogueForm> catalogueFormList = catalogueFormService.getCatalogueByDataId(id,FORM_TYPE_SUPPLIER);
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
        SupplierCatalogue entity = baseMapper.selectById(parentId);
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
    private void recursionTreeList(List<SupplierCatalogue> firstList, List<SupplierCatalogue> otherList) {
        firstList.forEach(entity -> {
            // 获取子集
            List<SupplierCatalogue> childrenList = otherList.stream()
                    .filter(child -> entity.getId().equals(child.getParentId()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(childrenList)) {
                entity.setChildren(childrenList);
                recursionTreeList(childrenList, otherList);
            }
        });
    }
}
