package com.hipi.code.controller;

import com.hipi.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 品类批次溯源环节表 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/code/traceBatchTraceability")
@Api(tags = "品类批次溯源环节管理")
@AllArgsConstructor
public class TraceBatchTraceabilityController extends BaseController {

}
