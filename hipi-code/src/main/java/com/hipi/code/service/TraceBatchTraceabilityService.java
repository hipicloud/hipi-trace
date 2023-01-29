package com.hipi.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.TraceBatchTraceability;

import java.util.List;

/**
 * <p>
 * 品类批次溯源环节表 服务类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
public interface TraceBatchTraceabilityService extends IService<TraceBatchTraceability> {

    /**
     * selectList
     * @param getBatchId getBatchId
     * @return List<TraceBatchTraceability>
     */
    List<TraceBatchTraceability> selectList(String getBatchId);

}
