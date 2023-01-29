package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.DistributorCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.service.DistributorCatalogueService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 渠道商目录管理 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/code/distributorCatalogue")
@Api(tags = "渠道商目录管理")
@AllArgsConstructor
public class DistributorCatalogueController extends BaseController {
    private final DistributorCatalogueService distributorCatalogueService;

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link R< List <DistributorCatalogue>>}
     */
    @ApiOperation("树形结构")
    @GetMapping("/selectTreeList")
    public R<List<DistributorCatalogue>> selectTreeList(CataLogueQuery query) {
        return R.ok(distributorCatalogueService.selectTreeList(query));
    }

    /**
     * 根据等级查询，不传默认查询最后一级
     *
     * @param query 查询
     * @param page  页面
     * @return {@link R< IPage <DistributorCatalogue>>}
     */
    @ApiOperation("根据等级查询，不传默认查询最后一级")
    @GetMapping("/selectLastPage")
    public R<IPage<DistributorCatalogue>> selectLastPage(Page<DistributorCatalogue> page, CataLogueQuery query) {
        return R.ok(distributorCatalogueService.selectLastPage(page, query));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    public R save(@RequestBody DistributorCatalogue entity) {
        return distributorCatalogueService.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("更新")
    @PutMapping("/update")
    public R update(@RequestBody DistributorCatalogue entity) {
        return distributorCatalogueService.updateEntity(entity);
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
        return distributorCatalogueService.deleteById(id);
    }

    /**
     * 通过id
     *
     * @param id id
     * @return {@link R<DistributorCatalogue>}
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/getById/{id}")
    public R<DistributorCatalogue> getById(@PathVariable("id") String id) {
        DistributorCatalogue catalogue = distributorCatalogueService.selectById(id);
        return R.ok(catalogue);
    }

}
