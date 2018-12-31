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
}