package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.DistributorManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.code.service.DistributorManageService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 渠道商管理表 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@RestController
@RequestMapping("/code/distributorManage")
@Api(tags = "渠道商管理")
@AllArgsConstructor
public class DistributorManageController  extends BaseController {
    private final DistributorManageService service;

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public R<IPage<DistributorManage>> page(Page page, ManageQuery query) {
        return R.ok(service.selectPage(page, query));
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    public R save(@RequestBody DistributorManage entity) {
        return service.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @ApiOperation("更新")
    @PutMapping("/update")
    public R update(@RequestBody DistributorManage entity) {
        return service.updateEntity(entity);
    }

    /**
     * 删除
     *
     * @param id id
     * @return {@link R}
     */
    @ApiOperation("删除")
    @DeleteMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id) {
        return service.deleteById(id);
    }

    /**
     * 通过id
     *
     * @param id id
     * @return {@link R<DistributorManage>}
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/getById/{id}")
    public R<DistributorManage> getById(@PathVariable("id") String id) {
        DistributorManage entity = service.selectById(id);
        return R.ok(entity);
    }

}
