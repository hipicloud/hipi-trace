package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.TraceCode;
import com.hipi.code.domain.query.TraceQuery;
import com.hipi.code.domain.vo.TraceCodeVO;

/**
 * <p>
 * 品类溯源码表 服务类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
public interface TraceCodeService extends IService<TraceCode> {

    /**
     * selectPage
     * @param page page
     * @param traceQuery traceQuery
     * @return IPage<TraceCodeVO>
     */
    IPage<TraceCodeVO> selectPage(Page<TraceCodeVO> page, TraceQuery traceQuery);
}
