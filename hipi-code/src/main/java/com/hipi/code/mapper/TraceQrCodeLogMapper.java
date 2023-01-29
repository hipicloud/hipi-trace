package com.hipi.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hipi.code.domain.TraceQrCodeLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 二维码扫码记录 Mapper 接口
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Mapper
public interface TraceQrCodeLogMapper extends BaseMapper<TraceQrCodeLog> {

}
