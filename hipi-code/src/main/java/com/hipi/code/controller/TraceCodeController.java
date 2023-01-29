package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.query.TraceQuery;
import com.hipi.code.domain.vo.TraceCodeVO;
import com.hipi.code.service.TraceCodeService;
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
 * 品类溯源码表 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/code/traceCode")
@Api(tags = "生码管理api")
@AllArgsConstructor
public class TraceCodeController extends BaseController {

    private final TraceCodeService traceCodeService;

    /**
     * 分页查询
     *
     * @param page
     * @param traceQuery
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public R<IPage<TraceCodeVO>> page(Page<TraceCodeVO> page, TraceQuery traceQuery) {
        return R.ok(traceCodeService.selectPage(page, traceQuery));
    }

}
