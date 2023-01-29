package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.CategoryManage;
import com.hipi.code.domain.query.ManageQuery;
import com.hipi.code.service.CategoryManageService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品类管理表 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2022-07-19
 */
@RestController
@RequestMapping("/code/categoryManage")
@Api(tags = "品类管理")
@AllArgsConstructor
public class CategoryManageController extends BaseController {
    private final CategoryManageService service;

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public R<IPage<CategoryManage>> page(Page page, ManageQuery query) {
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
    public R save(@RequestBody CategoryManage entity) {
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
    public R update(@RequestBody CategoryManage entity) {
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
     * @return {@link R<CategoryManage>}
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/getById/{id}")
    public R<CategoryManage> getById(@PathVariable("id") String id) {
        CategoryManage entity = service.selectById(id);
        return R.ok(entity);
    }

    @ApiOperation("列表")
    @GetMapping("/selectList")
    public R<List<CategoryManage>> selectList(ManageQuery query) {
        return R.ok(service.selectList(query));
    }

}
