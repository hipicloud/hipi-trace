package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.TraceabilityCatalogue;
import com.hipi.code.domain.query.CataLogueQuery;
import com.hipi.code.service.TraceabilityCatalogueService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 溯源环节管理 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/code/traceabilityCatalogue")
@Api(tags = "溯源环节目录管理")
@AllArgsConstructor
public class TraceabilityCatalogueController extends BaseController {
    private final TraceabilityCatalogueService traceabilityCatalogueService;

    /**
     * 树形结构
     *
     * @param query 查询
     * @return {@link R <  List  <TraceabilityCatalogue>>}
     */
    @ApiOperation("树形结构")
    @GetMapping("/selectTreeList")
    public R<List<TraceabilityCatalogue>> selectTreeList(CataLogueQuery query) {
        return R.ok(traceabilityCatalogueService.selectTreeList(query));
    }

    /**
     * 根据等级查询，不传默认查询最后一级
     *
     * @param query 查询
     * @param page  页面
     * @return {@link R<   IPage   <TraceabilityCatalogue>>}
     */
    @ApiOperation("根据等级查询，不传默认查询最后一级")
    @GetMapping("/selectLastPage")
    public R<IPage<TraceabilityCatalogue>> selectLastPage(Page<TraceabilityCatalogue> page, CataLogueQuery query) {
        return R.ok(traceabilityCatalogueService.selectLastPage(page, query));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    public R save(@RequestBody TraceabilityCatalogue entity) {
        return traceabilityCatalogueService.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("更新")
    @PutMapping("/update")
    public R update(@RequestBody TraceabilityCatalogue entity) {
        return traceabilityCatalogueService.updateEntity(entity);
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
        return traceabilityCatalogueService.deleteById(id);
    }

    /**
     * 通过id
     *
     * @param id id
     * @return {@link R<TraceabilityCatalogue>}
     */
    @ApiOperation("根据ID查询")
    @GetMapping("/getById/{id}")
    public R<TraceabilityCatalogue> getById(@PathVariable("id") String id) {
        TraceabilityCatalogue catalogue = traceabilityCatalogueService.selectById(id);
        return R.ok(catalogue);
    }
}
