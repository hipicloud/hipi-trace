package com.hipi.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.constant.CatalogueConstant;
import com.hipi.code.domain.CatalogueForm;
import com.hipi.code.domain.DistributorGoods;
import com.hipi.code.domain.DistributorManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.code.mapper.DistributorManageMapper;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.DistributorCatalogueService;
import com.hipi.code.service.DistributorGoodsService;
import com.hipi.code.service.DistributorManageService;
import com.hipi.common.core.domain.R;
import com.hipi.common.utils.uuid.Seq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_DISTRIBUTOR;

/**
 * <p>
 * 渠道商管理表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@Service
@AllArgsConstructor
public class DistributorManageServiceImpl extends ServiceImpl<DistributorManageMapper, DistributorManage> implements DistributorManageService {
    private final DistributorCatalogueService distributorCatalogueService;
    private final DistributorGoodsService distributorGoodsService;
    private final CatalogueFormService catalogueFormService;

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    @Override
    public IPage<DistributorManage> selectPage(Page page, ManageQuery query) {
        Page<DistributorManage> resultPage = baseMapper.selectPage(page, Wrappers.<DistributorManage>lambdaQuery()
                .like(StrUtil.isNotBlank(query.getCode()), DistributorManage::getDistributorCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getName()), DistributorManage::getDistributorName, query.getName()));
        List<DistributorManage> list = resultPage.getRecords();
        for (DistributorManage distributorManage : list) {
            CatalogueVO catalogueVO = distributorCatalogueService.selectTreeByLastId(distributorManage.getDistributorCatalogueId());
            distributorManage.setDistributorCatalogueName(catalogueVO.getLevelFourNameJoin());
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
    public R insert(DistributorManage entity) {
        entity.setDistributorCatalogueCode(CatalogueConstant.ADD_DISTRIBUTOR_CATALOGUE_PRE + Seq.getId());
        int insert = baseMapper.insert(entity);
        List<DistributorGoods> goodsList = entity.getGoodsList();
        for (DistributorGoods distributorGoods : goodsList) {
            // 新增销售渠道信息
            distributorGoods.setDistributorManageId(entity.getId());
            distributorGoodsService.saveOrUpdate(distributorGoods);
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
    public R updateEntity(DistributorManage entity) {
        int update = baseMapper.updateById(entity);
        List<DistributorGoods> goodsList = entity.getGoodsList();
        // 删除销售渠道信息
        distributorGoodsService.remove(Wrappers.<DistributorGoods>lambdaQuery().eq(DistributorGoods::getDistributorManageId, entity.getId()));
        for (DistributorGoods distributorGoods : goodsList) {
            // 新增销售渠道信息
            distributorGoods.setDistributorManageId(entity.getId());
            distributorGoodsService.saveOrUpdate(distributorGoods);
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
        int delete = baseMapper.deleteById(id);
        // 删除销售渠道信息
        distributorGoodsService.remove(Wrappers.<DistributorGoods>lambdaQuery().eq(DistributorGoods::getDistributorManageId, id));
        return R.toResult(delete);
    }

    /**
     * 根据id查询
     *
     * @param id id
     * @return {@link DistributorManage}
     */
    @Override
    public DistributorManage selectById(String id) {
        DistributorManage entity = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(entity.getDistributorCatalogueId())) {
            CatalogueVO catalogueVO = distributorCatalogueService.selectTreeByLastId(entity.getDistributorCatalogueId());
            entity.setDistributorCatalogueName(catalogueVO.getLevelFourNameJoin());
            List<CatalogueForm> formList = catalogueFormService.getCatalogueByDataId(entity.getDistributorCatalogueId(),FORM_TYPE_DISTRIBUTOR);
            entity.setCatalogueFormList(formList);
        }
        List<DistributorGoods> goodsList = distributorGoodsService.list(Wrappers.<DistributorGoods>lambdaQuery().eq(DistributorGoods::getDistributorManageId, id));
        entity.setGoodsList(goodsList);
        return entity;
    }
}
