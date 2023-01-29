package com.hipi.code.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.hipi.code.constant.BatchConstant;
import com.hipi.code.domain.*;
import com.hipi.code.domain.dto.BatchCodeExportDTO;
import com.hipi.code.domain.dto.BatchConfigDTO;
import com.hipi.code.domain.query.CategoryBatchQuery;
import com.hipi.code.mapper.*;
import com.hipi.code.service.CatalogueFormService;
import com.hipi.code.service.TraceBatchService;
import com.hipi.code.service.TraceQrCodeService;
import com.hipi.common.core.domain.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hipi.code.constant.CatalogueConstant.FORM_TYPE_TRACEABILITY;

/**
 * <p>
 * 商品批次表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Service
@AllArgsConstructor
public class TraceBatchServiceImpl extends ServiceImpl<TraceBatchMapper, TraceBatch> implements TraceBatchService {
    private final CategoryManageMapper categoryManageMapper;
    private final TraceCodeMapper traceCodeMapper;
    private final TraceBatchTraceabilityMapper traceBatchTraceabilityMapper;
    private final TraceabilityManageMapper traceabilityManageMapper;
    private final CatalogueFormService catalogueFormService;
    private final TraceQrCodeService traceQrCodeService;
    private final TraceBatchMapper traceBatchMapper;

    /**
     * 分页查询
     *
     * @param page       页面
     * @param traceBatch 批次
     * @return {@link IPage}<{@link TraceBatch}>
     */
    @Override
    public IPage<TraceBatch> selectPage(Page page, TraceBatch traceBatch) {
        boolean categoryManageName = StrUtil.isNotBlank(traceBatch.getCategoryManageName());
        List<String> categoryIdList = new ArrayList<>();
        if (categoryManageName) {
            // 出现品类名称
            List<CategoryManage> selectList = categoryManageMapper.selectList(Wrappers.<CategoryManage>lambdaQuery()
                    .select(CategoryManage::getId)
                    .like(CategoryManage::getCategoryName, traceBatch.getCategoryManageName()));
            categoryIdList = selectList.stream().map(CategoryManage::getId).collect(Collectors.toList());
        }
        // 分页查询
        Page<TraceBatch> resultPage = traceBatchMapper.page(page, categoryIdList);
        return resultPage;
    }

    @Override
    public R genCode(TraceCode entity) {
        int insert = traceCodeMapper.insert(entity);
        // 生码
        traceQrCodeService.genCode(entity);
        return R.ok();
    }

    @Override
    public R updateEntity(TraceCode entity) {
        int update = traceCodeMapper.updateById(entity);
        return R.toResult(update);
    }

    @Override
    public R updateStatus(String id) {
        TraceBatch traceBatch = baseMapper.selectById(id);
        if (BatchConstant.BATCH_STATUS_ACTIVE.equals(traceBatch.getStatus())) {
            traceBatch.setStatus(BatchConstant.BATCH_STATUS_FREEZE);
        } else {
            traceBatch.setStatus(BatchConstant.BATCH_STATUS_ACTIVE);
        }
        int update = baseMapper.updateById(traceBatch);
        return R.toResult(update);
    }

    @Override
    public R configure(BatchConfigDTO dto) {
        String batchId = dto.getBatchId();
        List<TraceBatchTraceability> configList = dto.getConfigList();
        traceBatchTraceabilityMapper.delete(Wrappers.<TraceBatchTraceability>lambdaQuery().eq(TraceBatchTraceability::getBatchId, batchId));
        for (TraceBatchTraceability traceability : configList) {
            traceBatchTraceabilityMapper.insert(traceability);
        }
        return R.ok();
    }

    @Override
    public TraceBatch selectById(String id) {
        TraceBatch traceBatch = baseMapper.selectById(id);
        List<TraceBatchTraceability> traceabilityList = traceBatchTraceabilityMapper.selectList(Wrappers.<TraceBatchTraceability>lambdaQuery()
                .eq(TraceBatchTraceability::getBatchId, id)
                .orderByAsc(TraceBatchTraceability::getSort));
        for (TraceBatchTraceability traceability : traceabilityList) {
            TraceabilityManage traceabilityManage = traceabilityManageMapper.selectById(traceability.getTraceabilityManageId());
            traceability.setTraceabilityManageName(traceabilityManage.getTraceabilityName());
            List<CatalogueForm> formList = catalogueFormService.getCatalogueByDataId(traceabilityManage.getTraceabilityCatalogueId(), FORM_TYPE_TRACEABILITY);
            traceability.setCatalogueFormList(formList);
        }
        traceBatch.setConfigList(traceabilityList);
        return traceBatch;
    }

    @Override
    public R saveTraceBatch(TraceBatch entity) {
        TraceBatch batch = traceBatchMapper.selectOne(Wrappers.<TraceBatch>lambdaQuery()
                .eq(TraceBatch::getBatchCode, entity.getBatchCode()));
        if (ObjectUtil.isNotEmpty(batch)) {
            return R.fail("批次号已存在");
        }
        int insert = baseMapper.insert(entity);
        return R.toResult(insert);
    }

    @Override
    public List<TraceBatch> selectTraceBatchList(CategoryBatchQuery categoryBatchQuery) {

        return traceBatchMapper.selectTraceBatchList(categoryBatchQuery);
    }

    /**
     * 下载二维码
     *
     * @param response  响应
     * @param batchCode 批处理代码
     */
    @Override
    @SneakyThrows
    public void downloadCode(HttpServletResponse response, String batchCode) {

        List<BatchCodeExportDTO> list = traceCodeMapper.listCode(batchCode);

        String fileName = "二维码-" + batchCode + ".xlsx";
        String sheetName = "二维码";

        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        // 设置防止中文名乱码
        String exportName = URLEncoder.encode(fileName, "utf-8");
        String sheetSubName = URLEncoder.encode(sheetName, "utf-8");

        // 文件下载方式(附件下载还是在当前浏览器打开)
        response.setHeader("Content-disposition", "attachment;filename=" +
                exportName + ".xlsx");

        for (BatchCodeExportDTO dto : list) {
            String codeUrl = dto.getCodeUrl();
            //destPath生成的二维码的路径及名称
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.MARGIN, 0);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            MatrixToImageWriter.writeToStream(
                    new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 50, 50, hints), "PNG",
                    outputStream);

            dto.setByteArray(outputStream.toByteArray());

        }
        EasyExcel.write(response.getOutputStream(), BatchCodeExportDTO.class).sheet(sheetSubName).doWrite(list);
    }
}
