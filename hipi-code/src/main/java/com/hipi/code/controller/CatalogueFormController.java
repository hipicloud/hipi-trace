package com.hipi.code.controller;

import com.hipi.code.domain.CatalogueForm;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.common.core.controller.BaseController;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hipi
 * ClassName:CatalogueFormController.java
 * date:2023-01-04 16:29
 * Description: 目录表单字段模板
 */
@RestController
@RequestMapping("/code/catalogueForm")
@Api(tags = "目录表单字段模板")
@AllArgsConstructor
public class CatalogueFormController extends BaseController {
    private final CatalogueFormService catalogueFormService;

    /**
     * 根据关联主键ID数据查询表单字段
     *
     * @param dataId 数据ID
     * @return {@link R}<{@link List}<{@link CatalogueForm}>>
     */
    @ApiOperation("根据关联主键ID数据查询表单字段")
    @GetMapping("/getCatalogueByDataId/{dataId}")
    public R<List<CatalogueForm>> getCatalogueByDataId(@PathVariable("dataId") String dataId) {
        return R.ok(catalogueFormService.getCatalogueByDataId(dataId,""));
    }

}
