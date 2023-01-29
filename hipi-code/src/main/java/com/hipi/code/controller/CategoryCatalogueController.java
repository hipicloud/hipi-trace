package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.CategoryCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.service.CategoryCatalogueService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品类目录管理 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/code/categoryCatalogue")
@Api(tags = "品类目录管理")
@AllArgsConstructor
public class CategoryCatalogueController extends BaseController {
    private final CategoryCatalogueService categoryCatalogueService;

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link R<List<CategoryCatalogue>>}
     */
    @ApiOperation("树形结构")
    @GetMapping("/selectTreeList")
    public R<List<CategoryCatalogue>> selectTreeList(CataLogueQuery query) {
        return R.ok(categoryCatalogueService.selectTreeList(query));
    }

    /**
     * 根据等级查询，不传默认查询最后一级
     *
     * @param query 查询
     * @param page  页面
     * @return {@link R<IPage<CategoryCatalogue>>}
     */
    @ApiOperation("根据等级查询，不传默认查询最后一级")
    @GetMapping("/selectLastPage")
    public R<IPage<CategoryCatalogue>> selectLastPage(Page<CategoryCatalogue> page, CataLogueQuery query) {
        return R.ok(categoryCatalogueService.selectLastPage(page, query));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    public R save(@RequestBody CategoryCatalogue entity) {
        return categoryCatalogueService.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("更新")
    @PutMapping("/update")
    public R update(@RequestBody CategoryCatalogue entity) {
        return categoryCatalogueService.updateEntity(entity);
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
        return categoryCatalogueService.deleteById(id);
    }

    /**
     * 通过id
     *
     * @param id id
     * @return {@link R<CategoryCatalogue>}
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/getById/{id}")
    public R<CategoryCatalogue> getById(@PathVariable("id") String id) {
        CategoryCatalogue catalogue = categoryCatalogueService.selectById(id);
        return R.ok(catalogue);
    }

}
