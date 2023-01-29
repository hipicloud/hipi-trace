package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.SupplierManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.common.core.domain.R;

/**
 * <p>
 * 供应商管理表 服务类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
public interface SupplierManageService extends IService<SupplierManage> {

    /**
     * 分页查询
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage<SupplierManage>}
     */
    IPage<SupplierManage> selectPage(Page page, ManageQuery query);

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    R insert(SupplierManage entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    R updateEntity(SupplierManage entity);

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
     * @return {@link SupplierManage}
     */
    SupplierManage selectById(String id);
}
