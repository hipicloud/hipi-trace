package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.TraceBatch;
import com.hipi.code.domain.TraceCode;
import com.hipi.code.domain.dto.BatchConfigDTO;
import com.hipi.code.domain.query.CategoryBatchQuery;
import com.hipi.common.core.domain.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 商品批次表 服务类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
public interface TraceBatchService extends IService<TraceBatch> {

    /**
     * 分页列表
     *
     * @param page       页面
     * @param traceBatch 批次
     * @return {@link IPage}<{@link TraceBatch}>
     */
    IPage<TraceBatch> selectPage(Page page, TraceBatch traceBatch);

    /**
     * genCode
     * @param entity entity
     * @return R
     */
    R genCode(TraceCode entity);

    /**
     * updateEntity
     * @param entity entity
     * @return R
     */
    R updateEntity(TraceCode entity);

    /**
     * updateStatus
     * @param id id
     * @return R
     */
    R updateStatus(String id);

    /**
     * configure
     * @param dto dto
     * @return R
     */
    R configure(BatchConfigDTO dto);

    /**
     * selectById
     * @param id id
     * @return TraceBatch
     */
    TraceBatch selectById(String id);

    /**
     * 保存批次
     * @param entity entity
     * @return R
     */
    R saveTraceBatch(TraceBatch entity);

    /**
     * selectTraceBatchList
     * @param categoryBatchQuery categoryBatchQuery
     * @return List<TraceBatch>
     */
    List<TraceBatch> selectTraceBatchList(CategoryBatchQuery categoryBatchQuery);

    /**
     * 下载二维码
     *
     * @param response  响应
     * @param batchCode 批处理代码
     */
    void downloadCode(HttpServletResponse response, String batchCode);
}
