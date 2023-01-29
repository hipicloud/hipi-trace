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
import com.hipi.code.domain.TraceabilityCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.code.mapper.TraceabilityCatalogueMapper;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.TraceabilityCatalogueService;
import com.hipi.common.core.domain.R;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_TRACEABILITY;

/**
 * <p>
 * 溯源环节管理 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@Service
@AllArgsConstructor
public class TraceabilityCatalogueServiceImpl extends ServiceImpl<TraceabilityCatalogueMapper, TraceabilityCatalogue> implements TraceabilityCatalogueService {

    private final CatalogueFormService catalogueFormService;

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link List <TraceabilityCatalogue>}
     */
    @Override
    public List<TraceabilityCatalogue> selectTreeList(CataLogueQuery query) {
        // 排除第四级
        List<TraceabilityCatalogue> catalogueList = baseMapper.selectList(Wrappers.lambdaQuery());
        // 父及
        List<TraceabilityCatalogue> firstList = new ArrayList<>(50);
        // 子集
        List<TraceabilityCatalogue> otherList = new ArrayList<>(200);
        String root = "0";
        catalogueList.stream().forEach(item -> {
            if (Objects.equals(item.getParentId(), root)) {
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
    public R insert(TraceabilityCatalogue entity) {
        int insert = baseMapper.insert(entity);
        // 删除旧表单
        catalogueFormService.remove(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, entity.getId())
                .eq(CatalogueForm::getDataType, FORM_TYPE_TRACEABILITY));
        List<CatalogueForm> catalogueFormList = entity.getCatalogueFormList();
        if (CollUtil.isNotEmpty(catalogueFormList)) {
            for (CatalogueForm catalogueForm : catalogueFormList) {
                catalogueForm.setDataId(entity.getId());
                catalogueForm.setDataType(FORM_TYPE_TRACEABILITY);
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
    public R updateEntity(TraceabilityCatalogue entity) {
        int update = baseMapper.updateById(entity);
        // 删除旧表单
        catalogueFormService.remove(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, entity.getId())
                .eq(CatalogueForm::getDataType, FORM_TYPE_TRACEABILITY));
        List<CatalogueForm> catalogueFormList = entity.getCatalogueFormList();
        if (CollUtil.isNotEmpty(catalogueFormList)) {
            for (CatalogueForm catalogueForm : catalogueFormList) {
                catalogueForm.setId(null);
                catalogueForm.setDataId(entity.getId());
                catalogueForm.setDataType(FORM_TYPE_TRACEABILITY);
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
        TraceabilityCatalogue entity = baseMapper.selectById(id);
        // 存在子集禁止删除
        TraceabilityCatalogue childrenEntity = baseMapper.selectOne(Wrappers.<TraceabilityCatalogue>lambdaQuery()
                .eq(TraceabilityCatalogue::getParentId, id)
                .last("limit 1"));
        if (ObjectUtil.isNotEmpty(childrenEntity)) {
            return R.fail("当前节点存在子集，禁止删除");
        }
        // 第四级则删除表单信息
        int delete = baseMapper.deleteById(id);
        // 删除旧表单
        catalogueFormService.remove(Wrappers.<CatalogueForm>lambdaQuery()
                .eq(CatalogueForm::getDataId, entity.getId())
                .eq(CatalogueForm::getDataType, FORM_TYPE_TRACEABILITY));
        return R.toResult(delete);
    }

    /**
     * 查询最后一级
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage <TraceabilityCatalogue>}
     */
    @Override
    public IPage<TraceabilityCatalogue> selectLastPage(Page<TraceabilityCatalogue> page, CataLogueQuery query) {
        Page<TraceabilityCatalogue> resultPage = baseMapper.selectPage(page, Wrappers.<TraceabilityCatalogue>lambdaQuery()
                .eq(StrUtil.isBlank(query.getGrade()), TraceabilityCatalogue::getGrade, CatalogueConstant.GRADE_FOUR)
                .eq(StrUtil.isNotBlank(query.getGrade()), TraceabilityCatalogue::getGrade, query.getGrade())
                .like(StrUtil.isNotBlank(query.getCode()), TraceabilityCatalogue::getCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), TraceabilityCatalogue::getName, query.getName())
                .eq(TraceabilityCatalogue::getParentId, query.getParentId()));
        return resultPage;
    }

    /**
     * 选择通过id
     *
     * @param id id
     * @return {@link TraceabilityCatalogue}
     */
    @Override
    public TraceabilityCatalogue selectById(String id) {
        TraceabilityCatalogue catalogue = baseMapper.selectById(id);
        List<CatalogueForm> catalogueFormList = catalogueFormService.getCatalogueByDataId(id,FORM_TYPE_TRACEABILITY);
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
        TraceabilityCatalogue entity = baseMapper.selectById(parentId);
        if (ObjectUtil.isEmpty(entity) || CatalogueConstant.GRADE_ONE.equals(entity.getGrade())) {
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
    private void recursionTreeList(List<TraceabilityCatalogue> firstList, List<TraceabilityCatalogue> otherList) {
        firstList.forEach(entity -> {
            // 获取子集
            List<TraceabilityCatalogue> childrenList = otherList.stream()
                    .filter(child -> entity.getId().equals(child.getParentId()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(childrenList)) {
                entity.setChildren(childrenList);
                recursionTreeList(childrenList, otherList);
            }
        });
    }
}
