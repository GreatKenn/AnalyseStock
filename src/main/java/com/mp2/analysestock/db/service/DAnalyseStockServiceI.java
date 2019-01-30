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

    List<D2FutData> select2FutData(HashMap<String, Object> param);

    List<D2IndexData> select2IndexMinDateByYear(HashMap<String, Object> param);

    List<DEveryDayTotal> selectEveryDayTotal();

    List<DEveryDayMargin> selectEveryDayMargin();

    List<DEveryDayHsgt> selectEveryDayHsgt();

    List<SelectItem> selectFutInfoByKey(HashMap<String, String> param);

    D2TotalIndexBetweenDate select2TotalIndexBetweenDate(HashMap<String, String> param);

    void deleteAnalyseKResult(String ts_code);

    void insertAnalyseKResult(com.mp2.analysestock.db.model.D2AnalyseKResult analyseKResult);

    void deleteAnalyseRicheResult(String ts_code);

    void insertAnalyseRicheResult(com.mp2.analysestock.db.model.D2AnalyseRicheResult analyseRicheResult);

    List<D2AnalyseKResult> selectAnalyseKResult(HashMap<String, String> param);

    List<D2AnalyseRicheResult> selectAnalyseRicheResult(HashMap<String, String> param);

    List<D2CfgFixResult> selectCfgFixResult(HashMap<String, String> param);
}
