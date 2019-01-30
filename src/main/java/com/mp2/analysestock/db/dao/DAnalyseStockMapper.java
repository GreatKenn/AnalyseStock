package com.mp2.analysestock.db.dao;

import java.util.HashMap;
import java.util.List;

import com.mp2.analysestock.db.model.*;

public interface DAnalyseStockMapper {

    List<DAnalyseStockLimit10> selectAllData(HashMap<String, Object> param);

    List<DIndexData> selectIndexData(HashMap<String, Object> param);

    List<DBaseData> selectIndex399006(HashMap<String, Object> param);

    List<DEventData> selectEventData();

    List<DYearWeekCount> selectNewStockCountByYearWeek();

    List<DYearWeekCountByType> selectNewStockCountByYearWeekType();

    /**
     * 获取涨跌停数据
     * @return
     */
    List<DFeelingUpDown10> selectFeelingUpDown10();

    /**
     * 根据 ts_code 参数获取每日指数数据
     * @param param
     * @return
     */
    List<D2IndexData> select2IndexData(HashMap<String, Object> param);

    /**
     * 根据 ts_code 获取每日期货数据
     * @param param
     * @return
     */
    List<D2FutData> select2FutData(HashMap<String, Object> param);

    /**
     * 根据 ts_code 参数获取每年最初指数数据
     * @param param
     * @return
     */
    List<D2IndexData> select2IndexMinDateByYear(HashMap<String, Object> param);

    /**
     * 获取市值数据
     * @return
     */
    List<DEveryDayTotal> selectEveryDayTotal();

    /**
     * 获取融资融券
     * @return
     */
    List<DEveryDayMargin> selectEveryDayMargin();

    /**
     * 沪股通、深股通、港股通每日资金流向数据
     * @return
     */
    List<DEveryDayHsgt> selectEveryDayHsgt();

    /**
     * 根据关键字搜索期货信息
     * @param param
     * @return
     */
    List<SelectItem> selectFutInfoByKey(HashMap<String, String> param);

    /**
     * 根据指数编码和日期范围，统计数据
     * @param param
     * @return
     */
    D2TotalIndexBetweenDate select2TotalIndexBetweenDate(HashMap<String, String> param);

    /**
     * 清楚指定编码的分析结果数据
     * @param ts_code
     */
    void deleteAnalyseKResult(String ts_code);

    /**
     * 插入分析结果数据
     * @param analyseKResult
     */
    void insertAnalyseKResult(com.mp2.analysestock.db.model.D2AnalyseKResult analyseKResult);
    /**
     * 清楚指定编码的分析结果数据
     * @param ts_code
     */
    void deleteAnalyseRicheResult(String ts_code);

    /**
     * 插入分析结果数据
     * @param analyseRicheResult
     */
    void insertAnalyseRicheResult(com.mp2.analysestock.db.model.D2AnalyseRicheResult analyseRicheResult);

    /**
     * 获取指定编码的分析后K线数据
     * @param param
     * @return
     */
    List<D2AnalyseKResult> selectAnalyseKResult(HashMap<String, String> param);

    /**
     * 获取指定编码的分析技术数据
     * @param param
     * @return
     */
    List<D2AnalyseRicheResult> selectAnalyseRicheResult(HashMap<String, String> param);

    /**
     * 通过配置表手动补充分析的结果不足数据
     * @param param
     * @return
     */
    List<D2CfgFixResult> selectCfgFixResult(HashMap<String, String> param);
}