package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.CategoryManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.common.core.domain.R;

import java.util.List;

/**
 * <p>
 * 品类管理表 服务类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
public interface CategoryManageService extends IService<CategoryManage> {

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    IPage<CategoryManage> selectPage(Page page, ManageQuery query);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    R insert(CategoryManage entity);

    /**
     * 更新实体
     *
     * @param entity 实体
     * @return {@link R}
     */
    R updateEntity(CategoryManage entity);

    /**
     * 删除
     *
     * @param id id
     * @return {@link R}
     */
    R deleteById(String id);

    /**
     * 根据id查询
     *
     * @param id id
     * @return {@link CategoryManage}
     */
    CategoryManage selectById(String id);

    /**
     * 查询集合
     * @param query 参数
     * @return {@link List<CategoryManage>}
     */
    List<CategoryManage> selectList(ManageQuery query);
}
