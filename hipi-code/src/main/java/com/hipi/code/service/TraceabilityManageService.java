package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.TraceabilityManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.common.core.domain.R;

/**
 * <p>
 * 溯源环节管理表 服务类
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
public interface TraceabilityManageService extends IService<TraceabilityManage> {

    /**
     * 分页查询
     *
     * @param page  页面
     * @param query 查询
     * @return {@link IPage<TraceabilityManage>}
     */
    IPage<TraceabilityManage> selectPage(Page page, ManageQuery query);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link R}
     */
    R insert(TraceabilityManage entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    R updateEntity(TraceabilityManage entity);

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
     * @return {@link TraceabilityManage}
     */
    TraceabilityManage selectById(String id);
}
