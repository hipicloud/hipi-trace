package com.hipi.code.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hipi.code.domain.dto.GoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 渠道商销售区域商品表
 * </p>
 *
 * @author hipi
 * @since 2023-01-06
 */
@TableName(value = "tb_distributor_goods", autoResultMap = true)
@ApiModel(value = "DistributorGoods对象", description = "渠道商销售区域商品表")
@Data
public class DistributorGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("渠道商管理主键")
    private String distributorManageId;

    @ApiModelProperty("省代码")
    private String provinceCode;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市代码")
    private String cityCode;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("县代码")
    private String countyCode;

    @ApiModelProperty("县名称")
    private String countyName;

    @ApiModelProperty("商品ID")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer delFlag;

    @TableField(typeHandler = JacksonTypeHandler.class, value = "goods_info")
    @ApiModelProperty(value = "商品信息")
    private List<GoodsInfoDTO> goodsInfoList;

}
