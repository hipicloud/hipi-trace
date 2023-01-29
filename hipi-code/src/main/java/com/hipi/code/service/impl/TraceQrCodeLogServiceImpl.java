package com.hipi.code.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.domain.TraceQrCodeLog;
import com.hipi.code.mapper.TraceQrCodeLogMapper;
import com.hipi.code.service.TraceQrCodeLogService;
import com.hipi.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 二维码扫码记录 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Service
public class TraceQrCodeLogServiceImpl extends ServiceImpl<TraceQrCodeLogMapper, TraceQrCodeLog> implements TraceQrCodeLogService {

    @Override
    public List<TraceQrCodeLog> selectList(String code) {
        List<TraceQrCodeLog> selectList = baseMapper.selectList(Wrappers.<TraceQrCodeLog>lambdaQuery()
                .eq(StrUtil.isNotBlank(code), TraceQrCodeLog::getQrCode, code));
        return selectList;
    }

    @Override
    public R insert(TraceQrCodeLog entity) {
        baseMapper.insert(entity);
        return R.ok();
    }
}
