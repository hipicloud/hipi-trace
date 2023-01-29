package com.hipi.code.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.domain.TraceCode;
import com.hipi.code.domain.query.TraceQuery;
import com.hipi.code.domain.vo.TraceCodeVO;
import com.hipi.code.mapper.TraceCodeMapper;
import com.hipi.code.service.TraceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 品类溯源码表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Service
public class TraceCodeServiceImpl extends ServiceImpl<TraceCodeMapper, TraceCode> implements TraceCodeService {

    @Autowired
    private TraceCodeMapper traceCodeMapper;

    @Override
    public IPage<TraceCodeVO> selectPage(Page<TraceCodeVO> page, TraceQuery traceQuery) {
        Page<TraceCodeVO> resultPage = new Page<TraceCodeVO>();
        long currentPage = page.getCurrent();
        page.setCurrent(currentPage-1);
        traceQuery.setCurrent(page.getCurrent());
        traceQuery.setSize(page.getSize());
        List<TraceCodeVO> traceCodeVOList =
                traceCodeMapper.selectListPage(traceQuery);

        long total =
                traceCodeMapper.selectTotal(traceQuery);
        resultPage.setTotal(total);
        resultPage.setRecords(traceCodeVOList);
        return resultPage;
    }
}
