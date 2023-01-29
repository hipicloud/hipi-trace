package com.hipi.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.TraceQrCodeLog;
import com.hipi.common.core.domain.R;

import java.util.List;

/**
 * <p>
 * 二维码扫码记录 服务类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
public interface TraceQrCodeLogService extends IService<TraceQrCodeLog> {

    /**
     * selectList
     * @param code code
     * @return List<TraceQrCodeLog>
     */
    List<TraceQrCodeLog> selectList(String code);

    /**
     * insert
     * @param entity entity
     * @return R
     */
    R insert(TraceQrCodeLog entity);
}
