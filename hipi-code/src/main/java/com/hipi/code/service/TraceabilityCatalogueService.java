package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.TraceabilityCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.common.core.domain.R;

import java.util.List;

/**
 * <p>
 * 溯源环节管理 服务类
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
public interface TraceabilityCatalogueService extends IService<TraceabilityCatalogue> {

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link List<TraceabilityCatalogue>}
     */
    List<TraceabilityCatalogue> selectTreeList(CataLogueQuery query);

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    R insert(TraceabilityCatalogue entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    R updateEntity(TraceabilityCatalogue entity);

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
     * @return {@link IPage<TraceabilityCatalogue>}
     */
    IPage<TraceabilityCatalogue> selectLastPage(Page<TraceabilityCatalogue> page, CataLogueQuery query);

    /**
     * 选择通过id
     *
     * @param id id
     * @return {@link TraceabilityCatalogue}
     */
    TraceabilityCatalogue selectById(String id);

    /**
     * 根据最后一个节点id查询对应树形结构
     *
     * @param id id
     * @return {@link CatalogueVO}
     */
    CatalogueVO selectTreeByLastId(String id);
}
