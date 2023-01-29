package com.hipi.code.constant;

/**
 * @author hipi
 * ClassName:CatalogueConstant.java
 * date:2022-07-04 14:37
 * Description: 目录管理常量类
 */
public final class CatalogueConstant {
    /**
     * 目录状态：0：启用，1：关闭
     */
    public static final Integer OPEN_STATUS = 0;
    public static final Integer CLOSE_STATUS = 1;

    /**
     * 目录等级：1/2/3/4
     */
    public static final Integer GRADE_ONE = 1;
    public static final Integer GRADE_TWO = 2;
    public static final Integer GRADE_THREE = 3;
    public static final Integer GRADE_FOUR = 4;

    /**
     * 来源类型：0：模板，1：品类目录，2：供应商目录，3：渠道商目录，4：溯源节点目录
     */
    public static final String FORM_TYPE_TEMPLATE = "0";
    public static final String FORM_TYPE_CATEGORY = "1";
    public static final String FORM_TYPE_SUPPLIER = "2";
    public static final String FORM_TYPE_DISTRIBUTOR = "3";
    public static final String FORM_TYPE_TRACEABILITY = "4";

    /**
     * 管理 新增目录编码前缀
     */
    public static final String ADD_CATEGORY_CATALOGUE_PRE = "PL";
    public static final String ADD_SUPPLIER_CATALOGUE_PRE = "GYS";
    public static final String ADD_DISTRIBUTOR_CATALOGUE_PRE = "QDS";
    public static final String ADD_TRACEABILITY_CATALOGUE_PRE = "SYHJ";

}
