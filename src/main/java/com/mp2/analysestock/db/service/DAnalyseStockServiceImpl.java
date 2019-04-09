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
    public List<DBaseData> selectIndex399006(HashMap<String, Object> param) {
        return mapper.selectIndex399006(param);
    }

    @Override
    public List<DEventData> selectEventData() {
        return mapper.selectEventData();
    }

    @Override
    public List<DYearWeekCount> selectNewStockCountByYearWeek() {
        return mapper.selectNewStockCountByYearWeek();
    }

    @Override
    public List<DYearWeekCountByType> selectNewStockCountByYearWeekType() {
        return mapper.selectNewStockCountByYearWeekType();
    }

    @Override
    public List<DFeelingUpDown10> selectFeelingUpDown10() {
        return mapper.selectFeelingUpDown10();
    }

    @Override
    public List<D2IndexData> select2IndexData(HashMap<String, String> param) {
        return mapper.select2IndexData(param);
    }

    @Override
    public List<D2ProphetData> selectProphetData(HashMap<String, String> param) {
        return mapper.selectProphetData(param);
    }

    @Override
    public List<D2FutData> select2FutData(HashMap<String, Object> param) {
        return mapper.select2FutData(param);
    }

    @Override
    public List<D2IndexData> select2IndexMinDateByYear(HashMap<String, String> param) {
        return mapper.select2IndexMinDateByYear(param);
    }

    @Override
    public List<DEveryDayTotal> selectEveryDayTotal() {
        return mapper.selectEveryDayTotal();
    }

    @Override
    public List<DEveryDayMargin> selectEveryDayMargin() {
        return mapper.selectEveryDayMargin();
    }

    @Override
    public List<DEveryDayHsgt> selectEveryDayHsgt() {
        return mapper.selectEveryDayHsgt();
    }

    @Override
    public List<SelectItem> selectFutInfoByKey(HashMap<String, String> param) {
        return mapper.selectFutInfoByKey(param);
    }

    @Override
    public D2TotalIndexBetweenDate select2TotalIndexBetweenDate(HashMap<String, String> param) {
        return mapper.select2TotalIndexBetweenDate(param);
    }

    @Override
    public void deleteAnalyseKResult(String ts_code) {
        mapper.deleteAnalyseKResult(ts_code);
    }

    @Override
    public void insertAnalyseKResult(com.mp2.analysestock.db.model.D2AnalyseKResult analyseKResult) {
        mapper.insertAnalyseKResult(analyseKResult);
    }

    @Override
    public void deleteAnalyseRicheResult(String ts_code) {
        mapper.deleteAnalyseRicheResult(ts_code);
    }

    @Override
    public void insertAnalyseRicheResult(com.mp2.analysestock.db.model.D2AnalyseRicheResult analyseRicheResult) {
        mapper.insertAnalyseRicheResult(analyseRicheResult);
    }

    @Override
    public List<D2AnalyseKResult> selectAnalyseKResult(HashMap<String, String> param) { return mapper.selectAnalyseKResult(param); }

    @Override
    public List<D2AnalyseRicheResult> selectAnalyseRicheResult(HashMap<String, String> param) { return mapper.selectAnalyseRicheResult(param); }

    @Override
    public List<D2CfgFixResult> selectCfgFixResult(HashMap<String, String> param) { return mapper.selectCfgFixResult(param); }

    @Override
    public void deleteProphetReady(HashMap<String, String> param) { mapper.deleteProphetReady(param); }

    @Override
    public void insertProphetReady(D2ProphetReady prophetReady) { mapper.insertProphetReady(prophetReady); }

    @Override
    public List<D2IndexesDailyBasicResult> select2IndexesDailyBasic(HashMap<String, String> param) { return mapper.select2IndexesDailyBasic(param); }
}
