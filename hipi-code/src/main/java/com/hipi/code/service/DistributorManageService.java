package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.DistributorManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.common.core.domain.R;

/**
 * <p>
 * 渠道商管理表 服务类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
public interface DistributorManageService extends IService<DistributorManage> {

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    IPage<DistributorManage> selectPage(Page page, ManageQuery query);

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    R insert(DistributorManage entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    R updateEntity(DistributorManage entity);

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
     * @return {@link DistributorManage}
     */
    DistributorManage selectById(String id);
}
