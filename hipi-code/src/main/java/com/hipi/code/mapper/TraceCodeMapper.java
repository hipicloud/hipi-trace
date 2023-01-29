package com.hipi.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hipi.code.domain.TraceCode;
import com.hipi.code.domain.dto.BatchCodeExportDTO;
import com.hipi.code.domain.query.TraceQuery;
import com.hipi.code.domain.vo.TraceCodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 品类溯源码表 Mapper 接口
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Mapper
public interface TraceCodeMapper extends BaseMapper<TraceCode> {

    /**
     * selectListPage
     * @param traceQuery traceQuery
     * @return List<TraceCodeVO>
     */
    List<TraceCodeVO> selectListPage( @Param("traceQuery") TraceQuery traceQuery);

    /**
     * selectTotal
     * @param traceQuery traceQuery
     * @return long
     */
    long selectTotal(@Param("traceQuery") TraceQuery traceQuery);

    /**
     * 查询二维码url
     *
     * @param batchCode 批次号
     * @return {@link List}<{@link BatchCodeExportDTO}>
     */
    List<BatchCodeExportDTO> listCode(@Param("batchCode") String batchCode);
}
