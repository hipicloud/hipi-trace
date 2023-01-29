package com.hipi.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hipi.code.domain.TraceBatch;
import com.hipi.code.domain.query.CategoryBatchQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品批次表 Mapper 接口
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Mapper
public interface TraceBatchMapper extends BaseMapper<TraceBatch> {

    /**
     * selectTraceBatchList
     * @param categoryBatchQuery categoryBatchQuery
     * @return List<TraceBatch>
     */
    List<TraceBatch> selectTraceBatchList(@Param("categoryBatchQuery") CategoryBatchQuery categoryBatchQuery);

    /**
     * page
     * @param page page
     * @param categoryIdList categoryIdList
     * @return Page<TraceBatch>
     */
    Page<TraceBatch> page(Page page, @Param("list") List<String> categoryIdList);
}
