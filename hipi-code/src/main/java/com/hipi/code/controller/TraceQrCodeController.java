package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.query.TraceQrQuery;
import com.hipi.code.domain.vo.TraceQrLogVO;
import com.hipi.code.service.TraceQrCodeService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 二维码表 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/code/traceQrCode")
@Api(tags = "生码记录api")
@AllArgsConstructor
public class TraceQrCodeController extends BaseController {

    private final TraceQrCodeService traceQrCodeService;

    /**
     * 分页查询
     *
     * @param page
     * @param traceQrQuery
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public R<IPage<TraceQrLogVO>> page(Page<TraceQrLogVO> page, TraceQrQuery traceQrQuery) {
        return R.ok(traceQrCodeService.selectPage(page, traceQrQuery));
    }

}
