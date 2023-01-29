package com.hipi.code.controller;

import com.hipi.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 二维码扫码记录 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/code/traceQrCodeLog")
@Api(tags = "二维码记录管理")
@AllArgsConstructor
public class TraceQrCodeLogController extends BaseController {

}
