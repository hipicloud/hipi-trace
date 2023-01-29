package com.hipi.code.controller;

import com.hipi.code.service.DistributorGoodsService;
import com.hipi.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 渠道商销售区域商品表 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2023-01-06
 */
@Controller
@RequestMapping("/code/distributorGoods")
@AllArgsConstructor
@Api(tags = "渠道商销售渠道管理")
public class DistributorGoodsController extends BaseController {
    private final DistributorGoodsService distributorGoodsService;

}
