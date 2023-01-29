package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.SupplierCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.service.SupplierCatalogueService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 供应商目录管理 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/code/supplierCatalogue")
@Api(tags = "供应商目录管理")
@AllArgsConstructor
public class SupplierCatalogueController extends BaseController {
    private final SupplierCatalogueService supplierCatalogueService;

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link R <  List  <SupplierCatalogue>>}
     */
    @ApiOperation("树形结构")
    @GetMapping("/selectTreeList")
    public R<List<SupplierCatalogue>> selectTreeList(CataLogueQuery query) {
        return R.ok(supplierCatalogueService.selectTreeList(query));
    }

    /**
     * 根据等级查询，不传默认查询最后一级
     *
     * @param query 查询
     * @param page  页面
     * @return {@link R<  IPage  <SupplierCatalogue>>}
     */
    @ApiOperation("根据等级查询，不传默认查询最后一级")
    @GetMapping("/selectLastPage")
    public R<IPage<SupplierCatalogue>> selectLastPage(Page<SupplierCatalogue> page, CataLogueQuery query) {
        return R.ok(supplierCatalogueService.selectLastPage(page, query));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    public R save(@RequestBody SupplierCatalogue entity) {
        return supplierCatalogueService.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("更新")
    @PutMapping("/update")
    public R update(@RequestBody SupplierCatalogue entity) {
        return supplierCatalogueService.updateEntity(entity);
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
        return supplierCatalogueService.deleteById(id);
    }

    /**
     * 通过id
     *
     * @param id id
     * @return {@link R<SupplierCatalogue>}
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/getById/{id}")
    public R<SupplierCatalogue> getById(@PathVariable("id") String id) {
        SupplierCatalogue catalogue = supplierCatalogueService.selectById(id);

        return R.ok(catalogue);
    }

}
