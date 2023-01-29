package com.hipi.code.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hipi.code.config.TraceLinkConfig;
import com.hipi.code.constant.BatchConstant;
import com.hipi.code.domain.TraceCode;
import com.hipi.code.domain.TraceQrCode;
import com.hipi.code.domain.query.TraceQrQuery;
import com.hipi.code.domain.vo.TraceQrLogVO;
import com.hipi.code.mapper.TraceQrCodeMapper;
import com.hipi.code.service.TraceQrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 二维码表 服务实现类
 * </p>
 *
 * @author hipi
 * @since 2023-01-08
 */
@Service
public class TraceQrCodeServiceImpl extends ServiceImpl<TraceQrCodeMapper, TraceQrCode> implements TraceQrCodeService {

    @Autowired
    private TraceQrCodeMapper traceQrCodeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TraceLinkConfig traceLinkConfig;

    @Override
    public IPage<TraceQrLogVO> selectPage(Page<TraceQrLogVO> page, TraceQrQuery traceQrQuery) {

        Page<TraceQrLogVO> resultPage = new Page<TraceQrLogVO>();
        long currentPage = page.getCurrent();
        page.setCurrent(currentPage - 1);
        traceQrQuery.setCurrent(page.getCurrent());
        traceQrQuery.setSize(page.getSize());
        List<TraceQrLogVO> traceQrLogVOList = traceQrCodeMapper.selectListPage(traceQrQuery);
        long total = traceQrCodeMapper.selectTotal(traceQrQuery);
        resultPage.setRecords(traceQrLogVOList);
        resultPage.setTotal(total);
        return resultPage;
    }

    /**
     * 生成二维码
     *
     * @param traceCode 实体
     */
    @Override
    @Transactional
    public void genCode(TraceCode traceCode) {
        String genCodeRule = traceCode.getGenCodeRule();
        Boolean letters = BatchConstant.GEN_CODE_RULE_MIX.equals(genCodeRule) ? Boolean.TRUE : BatchConstant.GEN_CODE_RULE_LETTER.equals(genCodeRule);
        Boolean numbers = BatchConstant.GEN_CODE_RULE_MIX.equals(genCodeRule) ? Boolean.TRUE : BatchConstant.GEN_CODE_RULE_NUMBER.equals(genCodeRule);
        Integer genNumber = traceCode.getGenNumber();
        TraceQrCode traceQrCode = new TraceQrCode();
        traceQrCode.setCodeId(traceCode.getCodeId());
        for (Integer i = 0; i < genNumber; i++) {
            // 根据生码规则进行生成
            String code = genRuleCode(letters, numbers, traceCode.getCharLength());
            traceQrCode.setQrCode(traceLinkConfig.getLink() + code);
            baseMapper.insert(traceQrCode);
        }

    }

    /**
     * 生成码
     *
     * @param letters 是否包含字母
     * @param numbers 是否包含数字
     * @param length  生成长度
     * @return {@link String}
     */
    public String genRuleCode(Boolean letters, Boolean numbers, Integer length) {
        //13位
        String resultCode = String.valueOf(System.currentTimeMillis());
        //19
        String idWork = String.valueOf(IdWorker.getId());
        if (letters && numbers) {
            if (32 == length) {
                // 包含数字、字母且长度为32位的字符串使用uuid
                resultCode = com.hipi.common.utils.uuid.UUID.randomUUID().toString().replaceAll("-", "");
            } else if (18 == length || 19 == length) {
                resultCode = idWork.substring(4);
            } else {
                int remainLength = length.intValue() - resultCode.length();
                resultCode = idWork + getRandomCodeByDigitAndType(remainLength, 7);
            }
        }
        if (numbers && !letters) {
            // 纯数字类型
            if (32 == length) {
                resultCode += idWork;
            } else if (18 == length) {
                resultCode = idWork.substring(1);
            } else {
                int remainLength = length.intValue() - idWork.length();
                resultCode = idWork + resultCode.substring(0, remainLength);
            }

        }
        if (letters && !numbers) {
            resultCode = getRandomCodeByDigitAndType(length, 7);
        }
        Boolean genCodeFlag = redisTemplate.opsForHash().hasKey("GEN_CODE", resultCode);
        if (genCodeFlag) {
            genRuleCode(letters, numbers, length);
        }
        redisTemplate.opsForHash().put("code", resultCode, resultCode);
        return resultCode;
    }

    /**
     * 根据位数和类型获取验证码
     *
     * @param codeDigit 随机数位数
     * @param codeType  随机数类型:1:数字;2:大写字母;3:小写字母;4:数字+大写字母;5:数字+小写字母;6:数字+大小写字母;7:大小写字母
     * @return
     */
    public static String getRandomCodeByDigitAndType(Integer codeDigit, Integer codeType) {
        String string = "";
        String numStr = "0123456789";
        String lowerCaseLetter = "abcdefghijklmnopqrstuvwxyz";
        String upperCaseLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        switch (codeType) {
            case 1:
                string = numStr;
                break;
            case 2:
                string = upperCaseLetter;
                break;
            case 3:
                string = lowerCaseLetter;
                break;
            case 4:
                string = numStr + upperCaseLetter;
                break;
            case 5:
                string = numStr + lowerCaseLetter;
                break;
            case 6:
                string = numStr + lowerCaseLetter + upperCaseLetter;
                break;
            case 7:
                string = lowerCaseLetter + upperCaseLetter;
                break;
        }
        string = getStringForDisorderSorting(string);//将字符串的顺序打乱
        StringBuilder stringBuilder = new StringBuilder();//声明一个StringBuffer对象保存验证码
        for (int i = 0; i < codeDigit; i++) {
            Random random = new Random();//创建随机数生成器
            int index = random.nextInt(string.length());//返回[0,string.length)范围的int值,保存下标
            char charAt = string.charAt(index);//charAt(),返回指定索引处的char值
            stringBuilder.append(charAt);//将charAt字符串追加到此序列
        }
        return stringBuilder.toString();//返回字符串
    }

    /**
     * 随机打乱一个字符串的排序方式
     *
     * @param string
     * @return
     */
    private static String getStringForDisorderSorting(String string) {
        char[] chars = string.toCharArray();
        List<Character> characterList = new ArrayList<Character>();
        for (char ch : chars) {
            characterList.add(ch);
        }
        Collections.shuffle(characterList);
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : characterList) {
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据位数获取数字验证码
     *
     * @param codeDigit
     * @return
     */
    private static String getRandomNum(Integer codeDigit) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < codeDigit; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
