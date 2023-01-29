package com.hipi.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hipi.code.domain.TraceCode;
import com.hipi.code.domain.TraceQrCode;
import com.hipi.code.domain.query.TraceQrQuery;
import com.hipi.code.domain.vo.TraceQrLogVO;

/**
 * <p>
 * 二维码表 服务类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
public interface TraceQrCodeService extends IService<TraceQrCode> {
    /**
     * 生成二维码
     *
     * @param traceCode 实体
     */
    void genCode(TraceCode traceCode);


    /**
     * 分页查询
     *
     * @param page
     * @param traceQrQuery
     * @return
     */
    IPage<TraceQrLogVO> selectPage(Page<TraceQrLogVO> page, TraceQrQuery traceQrQuery);
}
