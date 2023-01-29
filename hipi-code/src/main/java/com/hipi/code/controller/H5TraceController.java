package com.hipi.code.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hipi.code.config.TraceLinkConfig;
import com.hipi.code.domain.*;
import com.hipi.code.domain.vo.TraceInfoVO;
import com.hipi.code.service.*;
import com.hipi.common.annotation.Anonymous;
import com.hipi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_CATEGORY;
import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_TRACEABILITY;

/**
 * <p>
 * H5溯源页面
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/h5")
@Api(tags = "H5溯源页面")
@AllArgsConstructor
public class H5TraceController {

    private final TraceQrCodeService traceQrCodeService;

    private final TraceQrCodeLogService traceQrCodeLogService;

    private final TraceCodeService traceCodeService;

    private final TraceBatchService traceBatchService;

    private final CategoryManageService categoryManageService;

    private final CatalogueFormService catalogueFormService;

    private final TraceBatchTraceabilityService traceBatchTraceabilityService;

    private final TraceabilityManageService traceabilityManageService;

    @Autowired
    private TraceLinkConfig traceLinkConfig;

    /**
     * 根据溯源码查询品类溯源的详情
     *
     * @param code
     * @return {@link R}
     */
    @ApiOperation("根据溯源码查询品类溯源的详情")
    @GetMapping("/trace/{code}")
    @Anonymous
    public R<TraceInfoVO> trace(@PathVariable("code") String code) {
        TraceInfoVO traceInfoVO = new TraceInfoVO();
        //查询溯源码是否存在
        String traceLink = traceLinkConfig.getLink() + code;

        TraceQrCode traceQrCode = traceQrCodeService.getOne(Wrappers.<TraceQrCode>lambdaQuery()
                .eq(StrUtil.isNotBlank(traceLink), TraceQrCode::getQrCode, traceLink));
        if (ObjectUtil.isEmpty(traceQrCode)) {
            return R.fail("无效的二维码");
        }

        //通过二维码查询批次信息
        String codeId = traceQrCode.getCodeId();
        TraceCode traceCode = traceCodeService.getById(codeId);
        String batchCode = traceCode.getBatchCode();
        //查询批次信息是否冻结
        TraceBatch traceBatch = traceBatchService.getOne(Wrappers.<TraceBatch>lambdaQuery()
                .eq(StrUtil.isNotBlank(batchCode), TraceBatch::getBatchCode, batchCode));
        String batchStatus = traceBatch.getStatus();
        String batchStatusFreeze = "0";
        if (batchStatusFreeze.equals(batchStatus)) {
            return R.fail("无效的二维码");
        }
        //查询溯源码的扫码记录
        List<TraceQrCodeLog> selectList = traceQrCodeLogService.selectList(code);
        traceInfoVO.setTraceQrCodeLogList(selectList);

        //查询批次的品类信息结构和内容
        String categoryManageId = traceBatch.getCategoryManageId();
        CategoryManage categoryManage = categoryManageService.selectById(categoryManageId);
        String categoryCatalogueId = categoryManage.getCategoryCatalogueId();
        List<CatalogueForm> catalogueFormList =
                catalogueFormService.getCatalogueByDataId(categoryCatalogueId, FORM_TYPE_CATEGORY);
        categoryManage.setCatalogueFormList(catalogueFormList);
        traceInfoVO.setCategoryManage(categoryManage);

        //查询批次环节的信息结构和内容
        String batchId = traceBatch.getBatchId();
        List<TraceBatchTraceability> traceBatchTraceabilityList
                = traceBatchTraceabilityService.selectList(batchId);
        List<TraceabilityManage> traceabilityManageList = new ArrayList<>();
        for (TraceBatchTraceability batchTraceability : traceBatchTraceabilityList) {
            String traceabilityManageId = batchTraceability.getTraceabilityManageId();
            TraceabilityManage traceabilityManage
                    = traceabilityManageService.selectById(traceabilityManageId);
            String traceabilityCatalogueId = traceabilityManage.getTraceabilityCatalogueId();
            List<CatalogueForm> catalogueFormTraceList =
                    catalogueFormService.getCatalogueByDataId(traceabilityCatalogueId, FORM_TYPE_TRACEABILITY);
            traceabilityManage.setCatalogueFormList(catalogueFormTraceList);
            traceabilityManageList.add(traceabilityManage);
        }
        traceInfoVO.setTraceabilityManageList(traceabilityManageList);
        return R.ok(traceInfoVO);
    }

    /**
     * 新增溯源扫码记录
     *
     * @param entity 实体
     * @return {@link R}
     */
    @ApiOperation("新增溯源扫码记录")
    @PostMapping("/traceQrCodeLog")
    @Anonymous
    public R traceQrCodeLog(@RequestBody TraceQrCodeLog entity) {
        //查询溯源码是否存在
        String qrCode = entity.getQrCode();
        String traceLink = traceLinkConfig.getLink() + qrCode;
        TraceQrCode traceQrCode = traceQrCodeService.getOne(Wrappers.<TraceQrCode>lambdaQuery()
                .eq(StrUtil.isNotBlank(traceLink), TraceQrCode::getQrCode, traceLink));
        if (ObjectUtil.isEmpty(traceQrCode)) {
            return R.fail("无效的二维码");
        }
        return traceQrCodeLogService.insert(entity);
    }

}
