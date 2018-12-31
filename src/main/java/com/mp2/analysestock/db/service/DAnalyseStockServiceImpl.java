package com.mp2.analysestock.db.service;

import com.mp2.analysestock.db.dao.DAnalyseStockMapper;
import com.mp2.analysestock.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kenn on 16/6/1.
 */
@Service("dAnalyseStockService")
public class DAnalyseStockServiceImpl implements DAnalyseStockServiceI {

    @Autowired
    public DAnalyseStockMapper mapper;

    @Override
    public List<DAnalyseStockLimit10> selectAllData(HashMap<String, Object> param) {
        return mapper.selectAllData(param);
    }

    @Override
    public List<DIndexData> selectIndexData(HashMap<String, Object> param) {
        return mapper.selectIndexData(param);
    }

    @Override
    public List<DBaseData> selectIndex399006(HashMap<String, Object> param) { return mapper.selectIndex399006(param); }

    @Override
    public List<DEventData> selectEventData() { return mapper.selectEventData(); }

    @Override
    public List<DYearWeekCount> selectNewStockCountByYearWeek() { return mapper.selectNewStockCountByYearWeek(); }

    @Override
    public List<DYearWeekCountByType> selectNewStockCountByYearWeekType() { return mapper.selectNewStockCountByYearWeekType(); }

    @Override
    public List<DFeelingUpDown10> selectFeelingUpDown10() { return mapper.selectFeelingUpDown10(); }

    @Override
    public List<D2IndexData> select2IndexData(HashMap<String, Object> param) { return mapper.select2IndexData(param); }

    @Override
    public List<D2IndexData> select2IndexMinDateByYear(HashMap<String, Object> param) { return mapper.select2IndexMinDateByYear(param); }

    @Override
    public List<DEveryDayTotal> selectEveryDayTotal() { return mapper.selectEveryDayTotal(); }

    @Override
    public List<DEveryDayMargin> selectEveryDayMargin() { return mapper.selectEveryDayMargin(); }

    @Override
    public List<DEveryDayHsgt> selectEveryDayHsgt() { return mapper.selectEveryDayHsgt(); }
}
