package com.hipi.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.constant.CatalogueConstant;
import com.hipi.code.domain.CatalogueForm;
import com.hipi.code.domain.TraceabilityManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.code.mapper.TraceabilityManageMapper;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.TraceabilityCatalogueService;
import com.hipi.code.service.TraceabilityManageService;
import com.hipi.common.core.domain.R;
import com.hipi.common.utils.uuid.Seq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_TRACEABILITY;

/**
 * <p>
 * 溯源环节管理表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@Service
@AllArgsConstructor
public class TraceabilityManageServiceImpl extends ServiceImpl<TraceabilityManageMapper, TraceabilityManage> implements TraceabilityManageService {
    private final TraceabilityCatalogueService traceabilityCatalogueService;
    private final CatalogueFormService catalogueFormService;

    /**
     * 分页查询
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage <TraceabilityManage>}
     */
    @Override
    public IPage<TraceabilityManage> selectPage(Page page, ManageQuery query) {
        Page<TraceabilityManage> resultPage = baseMapper.selectPage(page, Wrappers.<TraceabilityManage>lambdaQuery()
                .like(StrUtil.isNotBlank(query.getCode()), TraceabilityManage::getTraceabilityCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), TraceabilityManage::getTraceabilityName, query.getName()));
        List<TraceabilityManage> list = resultPage.getRecords();
        for (TraceabilityManage traceabilityManage : list) {
            CatalogueVO catalogueVO = traceabilityCatalogueService.selectTreeByLastId(traceabilityManage.getTraceabilityCatalogueId());
            traceabilityManage.setTraceabilityCatalogueName(catalogueVO.getLevelFourNameJoin());
        }
        resultPage.setRecords(list);
        return resultPage;
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link R}
     */
    @Override
    public R insert(TraceabilityManage entity) {
        entity.setTraceabilityCatalogueCode(CatalogueConstant.ADD_TRACEABILITY_CATALOGUE_PRE + Seq.getId());
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
    public R updateEntity(TraceabilityManage entity) {
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
     * @return {@link TraceabilityManage}
     */
    @Override
    public TraceabilityManage selectById(String id) {
        TraceabilityManage entity = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(entity.getTraceabilityCatalogueId())) {
            CatalogueVO catalogueVO = traceabilityCatalogueService.selectTreeByLastId(entity.getTraceabilityCatalogueId());
            entity.setTraceabilityCatalogueName(catalogueVO.getLevelFourNameJoin());
            List<CatalogueForm> formList = catalogueFormService.getCatalogueByDataId(entity.getTraceabilityCatalogueId(),FORM_TYPE_TRACEABILITY);
            entity.setCatalogueFormList(formList);
        }
        return entity;
    }
}
