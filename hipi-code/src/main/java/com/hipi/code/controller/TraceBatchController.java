package com.hipi.code.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.TraceBatch;
import com.hipi.code.domain.TraceCode;
import com.hipi.code.domain.dto.BatchConfigDTO;
import com.hipi.code.domain.query.CategoryBatchQuery;
import com.hipi.code.service.TraceBatchService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 商品批次表 前端控制器
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/code/traceBatch")
@Api(tags = "商品批次管理")
@AllArgsConstructor
public class TraceBatchController extends BaseController {
    private final TraceBatchService traceBatchService;

    /**
     * 分页查询
     *
     * @param page       页面
     * @param traceBatch 批次
     * @return {@link R}<{@link IPage}<{@link TraceBatch}>>
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public R<IPage<TraceBatch>> page(Page page, TraceBatch traceBatch) {
        IPage<TraceBatch> resultPage = traceBatchService.selectPage(page, traceBatch);
        return R.ok(resultPage);
    }

    /**
     * 获取所有批次信息
     *
     * @param categoryBatchQuery 批次
     * @return {@link R}<{@link IPage}<{@link TraceBatch}>>
     */
    @ApiOperation("获取品类的批次信息")
    @GetMapping("/getCategoryBatch")
    public R<List<TraceBatch>> selectList(CategoryBatchQuery categoryBatchQuery) {
        List<TraceBatch> resultPage = traceBatchService.selectTraceBatchList(categoryBatchQuery);
        return R.ok(resultPage);
    }

    /**
     * 保存
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("新增")
    @PostMapping("/save")
    public R save(@RequestBody TraceBatch entity) {
        return traceBatchService.saveTraceBatch(entity);
    }

    /**
     * 生码
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("生码")
    @PostMapping("/genCode")
    public R genCode(@RequestBody TraceCode entity) {
        return traceBatchService.genCode(entity);
    }

    /**
     * 更新状态
     *
     * @param id id
     * @return {@link R}
     */
    @ApiOperation("冻结、激活")
    @PutMapping("/updateStatus/{id}")
    public R updateStatus(@PathVariable String id) {
        return traceBatchService.updateStatus(id);
    }

    /**
     * 配置
     *
     * @param dto dto
     * @return {@link R}
     */
    @ApiOperation("配置")
    @PostMapping("/configure")
    public R configure(@RequestBody BatchConfigDTO dto) {
        return traceBatchService.configure(dto);
    }

    /**
     * 详情
     *
     * @param id id
     * @return {@link R}
     */
    @ApiOperation("详情")
    @GetMapping("/getById/{id}")
    public R<TraceBatch> getById(@PathVariable String id) {
        return R.ok(traceBatchService.selectById(id));
    }

    /**
     * 下载二维码
     *
     * @param response  响应
     * @param batchCode 批处理代码
     */
    @ApiOperation("下载二维码")
    @GetMapping("/downloadCode/{batchCode}")
    public void downloadCode(HttpServletResponse response, @PathVariable("batchCode") String batchCode) {
        traceBatchService.downloadCode(response, batchCode);
    }

}
