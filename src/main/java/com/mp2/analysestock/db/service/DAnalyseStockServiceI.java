package com.mp2.analysestock.db.service;

import com.mp2.analysestock.db.model.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kenn on 16/6/1.
 */
public interface DAnalyseStockServiceI {

    List<DAnalyseStockLimit10> selectAllData(HashMap<String, Object> param);

    List<DIndexData> selectIndexData(HashMap<String, Object> param);

    List<DBaseData> selectIndex399006(HashMap<String, Object> param);

    List<DEventData> selectEventData();

    List<DYearWeekCount> selectNewStockCountByYearWeek();

    List<DYearWeekCountByType> selectNewStockCountByYearWeekType();

    List<DFeelingUpDown10> selectFeelingUpDown10();

    List<D2IndexData> select2IndexData(HashMap<String, Object> param);

    List<D2IndexData> select2IndexMinDateByYear(HashMap<String, Object> param);

    List<DEveryDayTotal> selectEveryDayTotal();

    List<DEveryDayMargin> selectEveryDayMargin();

    List<DEveryDayHsgt> selectEveryDayHsgt();
}
