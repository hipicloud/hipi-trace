package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.DistributorCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.common.core.domain.R;

import java.util.List;

/**
 * <p>
 * 渠道商目录管理 服务类
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
public interface DistributorCatalogueService extends IService<DistributorCatalogue> {

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link List<DistributorCatalogue>}
     */
    List<DistributorCatalogue> selectTreeList(CataLogueQuery query);

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    R insert(DistributorCatalogue entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    R updateEntity(DistributorCatalogue entity);

    /**
     * 删除
     *
     * @param id id
     * @return {@link R}
     */
    R deleteById(String id);

    /**
     * 查询最后一级
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage<DistributorCatalogue>}
     */
    IPage<DistributorCatalogue> selectLastPage(Page<DistributorCatalogue> page, CataLogueQuery query);

    /**
     * 选择通过id
     *
     * @param id id
     * @return {@link DistributorCatalogue}
     */
    DistributorCatalogue selectById(String id);

    /**
     * 根据最后一个节点id查询对应树形结构
     *
     * @param id id
     * @return {@link CatalogueVO}
     */
    CatalogueVO selectTreeByLastId(String id);
}
