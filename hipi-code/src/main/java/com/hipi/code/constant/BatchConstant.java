package com.hipi.code.constant;

/**
 * @author hipi
 * ClassName:BatchConstant.java
 * date:2023-01-09 17:57
 * Description: 批次常量
 */
public final class BatchConstant {
    /**
     * 状态：1：激活，2：冻结
     */
    public static final String BATCH_STATUS_ACTIVE = "1";
    public static final String BATCH_STATUS_FREEZE = "2";

    /**
     * 生码规则：1：纯数字，2：纯字母，3：混合
     */
    public static final String GEN_CODE_RULE_NUMBER = "1";
    public static final String GEN_CODE_RULE_LETTER = "2";
    public static final String GEN_CODE_RULE_MIX = "3";
}
