package com.hipi.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hipi.code.domain.TraceQrCode;
import com.hipi.code.domain.query.TraceQrQuery;
import com.hipi.code.domain.vo.TraceQrLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 二维码表 Mapper 接口
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Mapper
public interface TraceQrCodeMapper extends BaseMapper<TraceQrCode> {

    /**
     * 分页查询
     ** @param traceQrQuery
     * @return
     */
    List<TraceQrLogVO> selectListPage(@Param("traceQrQuery") TraceQrQuery traceQrQuery);

    /**
     * 查询数量
     * @param traceQrQuery
     * @return
     */
    long selectTotal(@Param("traceQrQuery") TraceQrQuery traceQrQuery);
}
