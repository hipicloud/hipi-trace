package com.hipi.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.domain.TraceBatchTraceability;
import com.hipi.code.mapper.TraceBatchTraceabilityMapper;
import com.hipi.code.service.TraceBatchTraceabilityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 品类批次溯源环节表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Service
public class TraceBatchTraceabilityServiceImpl extends ServiceImpl<TraceBatchTraceabilityMapper, TraceBatchTraceability> implements TraceBatchTraceabilityService {

    @Override
    public List<TraceBatchTraceability> selectList(String getBatchId) {
        List<TraceBatchTraceability> selectList = baseMapper.selectList(Wrappers.<TraceBatchTraceability>lambdaQuery()
                .eq(StrUtil.isNotBlank(getBatchId), TraceBatchTraceability::getBatchId, getBatchId));
        return selectList;
    }
}
