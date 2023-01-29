package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.CategoryCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.domain.vo.CatalogueVO;
import com.hipi.common.core.domain.R;

import java.util.List;

/**
 * <p>
 * 品类目录管理 服务类
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
public interface CategoryCatalogueService extends IService<CategoryCatalogue> {

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link List<CategoryCatalogue>}
     */
    List<CategoryCatalogue> selectTreeList(CataLogueQuery query);

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    R insert(CategoryCatalogue entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    R updateEntity(CategoryCatalogue entity);

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
     * @param query 查询
     * @param page  页面
     * @return {@link IPage<CategoryCatalogue>}
     */
    IPage<CategoryCatalogue> selectLastPage(Page<CategoryCatalogue> page, CataLogueQuery query);

    /**
     * 选择通过id
     *
     * @param id id
     * @return {@link CategoryCatalogue}
     */
    CategoryCatalogue selectById(String id);

    /**
     * 根据最后一个节点id查询对应树形结构
     *
     * @param id id
     * @return {@link CatalogueVO}
     */
    CatalogueVO selectTreeByLastId(String id);
}
