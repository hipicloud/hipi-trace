package com.hipi.web.controller.tool;

import com.hipi.common.constant.Constants;
import com.hipi.common.core.domain.AjaxResult;
import com.hipi.common.core.domain.model.LoginBody;
import com.hipi.framework.web.service.SysLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cj
 */
@Api(tags = "获取 token")
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private SysLoginService loginService;

    /**
     * 获取 token
     *
     * @return 结果
     */
    @ApiOperation("获取网页 token")
    @PostMapping("/web")
    public AjaxResult token(@ApiParam(name = "user", value = "user can get token", required = true) @RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        System.out.println(loginBody);
        // 生成令牌
        String token = loginService.token(loginBody.getUsername(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }
}
