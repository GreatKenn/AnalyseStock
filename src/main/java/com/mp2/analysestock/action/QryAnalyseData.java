package com.mp2.analysestock.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mp2.analysestock.db.model.*;
import com.mp2.analysestock.action.structure.*;
import com.mp2.util.JsonUtil;
import com.mp2.analysestock.commons.MP2BaseActionSupport;
import com.mp2.analysestock.db.service.DAnalyseStockServiceI;

public class QryAnalyseData extends MP2BaseActionSupport {
    private final static Logger logger = LoggerFactory.getLogger(QryAnalyseData_v1.class);

    private String paramValue;          //入参
    private InputStream inputStream;    //返回的数据流

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
        System.out.println("paramValue: " + paramValue);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * ECharts使用的结果数据结构
     */
    class EChartsResult_01 {
        public String[] legend_data;
        public HashMap<String, Boolean> legend_selected;
        public String[] xAxis_data;
        public int[][] series_data;
        public double[] index_data_000001;
        public double[] index_data_399001;

        public EChartsResult_01(int xCount, int yCount) {
            legend_data = new String[yCount];
            legend_selected = new HashMap();
            xAxis_data = new String[xCount];
            series_data = new int[yCount][xCount];
            index_data_000001 = new double[xCount];
            index_data_399001 = new double[xCount];
        }
    }

    class EChartsResult_02 {
        public String[] legend_data;
        public HashMap<String, Boolean> legend_selected;
        public String[] xAxis_data;
        public double[][] series_data;

        public EChartsResult_02(int xCount, int yCount) {
            legend_data = new String[yCount];
            legend_selected = new HashMap();
            xAxis_data = new String[xCount];
            series_data = new double[yCount][xCount];
        }
    }

    class ProphetData {
        public String date;
        public double value;
        public double l;
        public double u;
    }

    class EChartsResult_Prophet {
        public ProphetData[] series_data;

        private String ts_code;
        private String ts_name;

        public EChartsResult_Prophet(String ts_code, String ts_name) {
            this.ts_code = ts_code;
            this.ts_name = ts_name;
        }

        /**
         * 从数据库中读取数据
         */
        public void load() {
            int sizeItems;
            HashMap<String, String> param = new HashMap<>();
            DAnalyseStockServiceI dAnalyseStockService;

            dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

            param.clear();
            param.put("ts_code", ts_code);
            List<D2ProphetData> listAnalyseKResult = dAnalyseStockService.selectProphetData(param);
            sizeItems = listAnalyseKResult.size();
            this.series_data = new ProphetData[sizeItems];
            for (int n = 0; n < sizeItems; n++) {
                D2ProphetData item = listAnalyseKResult.get(n);
                // System.out.println(item.toString());
                ProphetData t = new ProphetData();
                t.date = item.getTrade_date();
                t.value = item.getYhat();
                t.u = item.getYhat_upper();
                t.l = item.getYhat_lower();

                this.series_data[n] = t;
            }
        }
    }

    class EChartsResult_K {
        public String[] legend_data;
        public HashMap<String, Boolean> legend_selected;
        public String[] xAxis_data;
        public double[][] series_data_k; // 开盘(open)，收盘(close)，最低(lowest)，最高(highest)
        public double[] series_data_vol;
        public double[] series_data_ema_5;
        public double[] series_data_ema_10;
        public double[] series_data_ema_20;
        public double[] series_data_ema_30;
        public double[] series_data_ema_250;
        public double[] series_data_bias_5;
        public double[] series_data_bias_20;
        public double[] series_data_macd;
        public double[] series_data_psy;
        public List<LineCoord>[] markline_data;
        public List<AreaCoord>[] markarea_data;
        public List<PointCoord> markpoint_data = new ArrayList<>();

        private String ts_code;
        private String ts_name;

        public EChartsResult_K(String ts_code, String ts_name) {
            this.ts_code = ts_code;
            this.ts_name = ts_name;
        }

        public EChartsResult_K(String ts_code, String ts_name, int xCount, int yCount) {
            this.ts_code = ts_code;
            this.ts_name = ts_name;
            this.legend_data = new String[yCount];
            this.legend_selected = new HashMap();
            this.xAxis_data = new String[xCount];
            this.series_data_k = new double[xCount][4];
            this.series_data_vol = new double[xCount];
            this.series_data_ema_5 = new double[xCount];
            this.series_data_ema_10 = new double[xCount];
            this.series_data_ema_20 = new double[xCount];
            this.series_data_ema_30 = new double[xCount];
            this.series_data_ema_250 = new double[xCount];
            this.series_data_bias_5 = new double[xCount];
            this.series_data_bias_20 = new double[xCount];
            this.series_data_macd = new double[xCount];
            this.series_data_psy = new double[xCount];
        }

        /**
         * 保存数据到数据库中
         */
        public void save() {
            DAnalyseStockServiceI dAnalyseStockService;

            dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

            // 保存每日数据
            dAnalyseStockService.deleteAnalyseKResult(ts_code);
            for (int n = 0; n < xAxis_data.length; n++) {
                D2AnalyseKResult analyseKResult = new D2AnalyseKResult();
                analyseKResult.setTs_code(ts_code);
                analyseKResult.setTrade_date(xAxis_data[n]);
                analyseKResult.setOpen_val(series_data_k[n][0]);
                analyseKResult.setClose_val(series_data_k[n][1]);
                analyseKResult.setLow_val(series_data_k[n][2]);
                analyseKResult.setHigh_val(series_data_k[n][3]);
                analyseKResult.setVol_val(series_data_vol[n]);
                analyseKResult.setEma_5_val(series_data_ema_5[n]);
                analyseKResult.setEma_10_val(series_data_ema_10[n]);
                analyseKResult.setEma_20_val(series_data_ema_20[n]);
                analyseKResult.setEma_30_val(series_data_ema_30[n]);
                analyseKResult.setEma_250_val(series_data_ema_250[n]);
                analyseKResult.setBias_5_val(series_data_bias_5[n]);
                analyseKResult.setBias_20_val(series_data_bias_20[n]);
                analyseKResult.setMacd_val(series_data_macd[n]);
                analyseKResult.setPsy_val(series_data_psy[n]);

                dAnalyseStockService.insertAnalyseKResult(analyseKResult);
            }

            // 保存线段、区域、点这类复合数据
            dAnalyseStockService.deleteAnalyseRicheResult(ts_code);
            for (int n = 0; n < markline_data.length; n++) {
                D2AnalyseRicheResult analyseRicheResult = new D2AnalyseRicheResult();
                List<LineCoord> listLine = markline_data[n];
                analyseRicheResult.setTs_code(ts_code);
                analyseRicheResult.setClass_str("markline");
                analyseRicheResult.setOrder_val(n);
                analyseRicheResult.setData_json(JsonUtil.toJSONString(listLine));

                dAnalyseStockService.insertAnalyseRicheResult(analyseRicheResult);
            }
            for (int n = 0; n < markarea_data.length; n++) {
                D2AnalyseRicheResult analyseRicheResult = new D2AnalyseRicheResult();
                List<AreaCoord> listArea = markarea_data[n];
                analyseRicheResult.setTs_code(ts_code);
                analyseRicheResult.setClass_str("markarea");
                analyseRicheResult.setOrder_val(n);
                analyseRicheResult.setData_json(JsonUtil.toJSONString(listArea));

                dAnalyseStockService.insertAnalyseRicheResult(analyseRicheResult);
            }
            for (int n = 0; n < markpoint_data.size(); n++) {
                D2AnalyseRicheResult analyseRicheResult = new D2AnalyseRicheResult();
                PointCoord point = markpoint_data.get(n);
                analyseRicheResult.setTs_code(ts_code);
                analyseRicheResult.setClass_str("markpoint");
                analyseRicheResult.setOrder_val(n);
                analyseRicheResult.setData_json(JsonUtil.toJSONString(point));

                dAnalyseStockService.insertAnalyseRicheResult(analyseRicheResult);
            }
            //单个参数
            {
                D2AnalyseRicheResult analyseRicheResult = new D2AnalyseRicheResult();
                analyseRicheResult.setTs_code(ts_code);
                analyseRicheResult.setClass_str("legend_data");
                analyseRicheResult.setOrder_val(0);
                analyseRicheResult.setData_json(JsonUtil.toJSONString(this.legend_data));

                dAnalyseStockService.insertAnalyseRicheResult(analyseRicheResult);
            }
            //单个参数
            {
                D2AnalyseRicheResult analyseRicheResult = new D2AnalyseRicheResult();
                analyseRicheResult.setTs_code(ts_code);
                analyseRicheResult.setClass_str("legend_selected");
                analyseRicheResult.setOrder_val(0);
                analyseRicheResult.setData_json(JsonUtil.toJSONString(this.legend_selected));

                dAnalyseStockService.insertAnalyseRicheResult(analyseRicheResult);
            }
        }

        /**
         * 从数据库中读取数据
         */
        public void load() {
            int sizeItems;
            HashMap<String, String> param = new HashMap<>();
            List<D2AnalyseRicheResult> listAnalyseRicheResult;
            DAnalyseStockServiceI dAnalyseStockService;

            dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

            param.clear();
            param.put("ts_code", ts_code);
            List<D2AnalyseKResult> listAnalyseKResult = dAnalyseStockService.selectAnalyseKResult(param);
            sizeItems = listAnalyseKResult.size();
            this.xAxis_data = new String[sizeItems];
            this.series_data_k = new double[sizeItems][4];
            this.series_data_vol = new double[sizeItems];
            this.series_data_ema_5 = new double[sizeItems];
            this.series_data_ema_10 = new double[sizeItems];
            this.series_data_ema_20 = new double[sizeItems];
            this.series_data_ema_30 = new double[sizeItems];
            this.series_data_ema_250 = new double[sizeItems];
            this.series_data_bias_5 = new double[sizeItems];
            this.series_data_bias_20 = new double[sizeItems];
            this.series_data_macd = new double[sizeItems];
            this.series_data_psy = new double[sizeItems];
            for (int n = 0; n < sizeItems; n++) {
                D2AnalyseKResult item = listAnalyseKResult.get(n);
                this.xAxis_data[n] = item.getTrade_date();
                series_data_k[n][0] = item.getOpen_val();
                series_data_k[n][1] = item.getClose_val();
                series_data_k[n][2] = item.getLow_val();
                series_data_k[n][3] = item.getHigh_val();
                series_data_vol[n] = item.getVol_val();
                series_data_ema_5[n] = item.getEma_5_val();
                series_data_ema_10[n] = item.getEma_10_val();
                series_data_ema_20[n] = item.getEma_20_val();
                series_data_ema_30[n] = item.getEma_30_val();
                series_data_ema_250[n] = item.getEma_250_val();
                series_data_bias_5[n] = item.getBias_5_val();
                series_data_bias_20[n] = item.getBias_20_val();
                series_data_macd[n] = item.getMacd_val();
                series_data_psy[n] = item.getPsy_val();
            }

            /*
            * public String[] legend_data;
            # public HashMap<String, Boolean> legend_selected;
            * */
            param.clear();
            param.put("ts_code", ts_code);
            param.put("class_str", "legend_data");
            listAnalyseRicheResult = dAnalyseStockService.selectAnalyseRicheResult(param);
            sizeItems = listAnalyseRicheResult.size();
            if (sizeItems > 0) {
                D2AnalyseRicheResult item = listAnalyseRicheResult.get(0);
                this.legend_data = JsonUtil.getEntity(item.getData_json(), String[].class);
            }

            param.clear();
            param.put("ts_code", ts_code);
            param.put("class_str", "legend_selected");
            listAnalyseRicheResult = dAnalyseStockService.selectAnalyseRicheResult(param);
            sizeItems = listAnalyseRicheResult.size();
            if (sizeItems > 0) {
                D2AnalyseRicheResult item = listAnalyseRicheResult.get(0);
                this.legend_selected = JsonUtil.getEntity(item.getData_json(), HashMap.class);
            }

            // markline
            param.clear();
            param.put("ts_code", ts_code);
            param.put("class_str", "markline");
            listAnalyseRicheResult = dAnalyseStockService.selectAnalyseRicheResult(param);
            sizeItems = listAnalyseRicheResult.size();
            this.markline_data = new List[sizeItems];
            for (int n = 0; n < sizeItems; n++) {
                D2AnalyseRicheResult item = listAnalyseRicheResult.get(n);
                this.markline_data[n] = JsonUtil.getEntity(item.getData_json(), List.class);
            }
            // markarea
            param.clear();
            param.put("ts_code", ts_code);
            param.put("class_str", "markarea");
            listAnalyseRicheResult = dAnalyseStockService.selectAnalyseRicheResult(param);
            sizeItems = listAnalyseRicheResult.size();
            this.markarea_data = new List[sizeItems];
            for (int n = 0; n < sizeItems; n++) {
                D2AnalyseRicheResult item = listAnalyseRicheResult.get(n);
                this.markarea_data[n] = JsonUtil.getEntity(item.getData_json(), List.class);
            }
            // markpoint
            param.clear();
            param.put("ts_code", ts_code);
            param.put("class_str", "markpoint");
            listAnalyseRicheResult = dAnalyseStockService.selectAnalyseRicheResult(param);
            sizeItems = listAnalyseRicheResult.size();
            for (int n = 0; n < sizeItems; n++) {
                D2AnalyseRicheResult item = listAnalyseRicheResult.get(n);
                this.markpoint_data.add(JsonUtil.getEntity(item.getData_json(), PointCoord.class));
            }
        }
    }

    /**
     * 获取指数信息数据
     *
     * @return
     */
    public String getIndexes() {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();
        List<D2IndexData> index_000001, index_399001, index_000852, index_399006;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("ts_code", "000001.SH"); //上证综指
            index_000001 = dAnalyseStockService.select2IndexData(param);
            param.clear();
            param.put("ts_code", "399001.SZ"); //深证成指
            index_399001 = dAnalyseStockService.select2IndexData(param);
            param.clear();
            param.put("ts_code", "000852.SH"); //中证1000
            index_000852 = dAnalyseStockService.select2IndexData(param);
            param.clear();
            param.put("ts_code", "399006.SZ"); //创业板指
            index_399006 = dAnalyseStockService.select2IndexData(param);

            String[] legend = {"上证综指", "深证成指", "中证1000", "创业板指"};
            EChartsResult_02 eChartsResult = new EChartsResult_02(index_000001.size(), legend.length);

            eChartsResult.legend_data = legend.clone();
            eChartsResult.legend_selected.clear();
            eChartsResult.legend_selected.put("上证综指", true);
            eChartsResult.legend_selected.put("深证成指", true);
            eChartsResult.legend_selected.put("中证1000", true);
            eChartsResult.legend_selected.put("创业板指", true);

            // 填充指数数据
            for (int x = 0; x < index_000001.size(); x++) {
                D2IndexData item_000001 = index_000001.get(x);
                D2IndexData item_399001 = index_399001.get(x);

                eChartsResult.xAxis_data[x] = item_000001.getTrade_date();
                eChartsResult.series_data[0][x] = item_000001.getClose_val();
                eChartsResult.series_data[1][x] = item_399001.getClose_val();
                eChartsResult.series_data[2][x] = 0;
                eChartsResult.series_data[3][x] = 0;

                int sz_000852 = index_000001.size() - index_000852.size();
                if (x >= sz_000852) {
                    D2IndexData item_000852 = index_000852.get(x - sz_000852);
                    eChartsResult.series_data[2][x] = item_000852.getClose_val();
                }
                int sz_399006 = index_000001.size() - index_399006.size();
                if (x >= sz_399006) {
                    D2IndexData item_399006 = index_399006.get(x - sz_399006);
                    eChartsResult.series_data[3][x] = item_399006.getClose_val();
                }
            }

            index_000001.clear();
            index_399001.clear();
            index_000852.clear();
            index_399006.clear();

            try {
                objMapper.writeValue(witStr, eChartsResult);
                //logger.info("toJSONString<" + witStr + ">");
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 情绪 获取涨跌停数据
     *
     * @return
     */
    public String getFeelingUpDown10() {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();
        List<DFeelingUpDown10> listData;
        List<D2IndexData> index_000001, index_399001;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            listData = dAnalyseStockService.selectFeelingUpDown10();

            int x = 0;
            String[] legend = {"非一字涨停", "一字涨停", "非一字跌停", "一字跌停", "涨停炸板", "跌停翘板", "涨停2板", "涨停3板"
                    , "涨停4板", "涨停5板", "跌停2板", "跌停3板", "跌停4板", "跌停5板", "25日翻倍", "25日折半", "上下幅度超过12%"};
            EChartsResult_02 eChartsResult = new EChartsResult_02(listData.size(), legend.length);

            eChartsResult.legend_data = legend.clone();
            eChartsResult.legend_selected.clear();
            eChartsResult.legend_selected.put("非一字涨停", true);
            eChartsResult.legend_selected.put("一字涨停", true);
            eChartsResult.legend_selected.put("非一字跌停", true);
            eChartsResult.legend_selected.put("一字跌停", true);
            eChartsResult.legend_selected.put("涨停炸板", true);
            eChartsResult.legend_selected.put("跌停翘板", true);
            eChartsResult.legend_selected.put("涨停2板", false);
            eChartsResult.legend_selected.put("涨停3板", false);
            eChartsResult.legend_selected.put("涨停4板", false);
            eChartsResult.legend_selected.put("涨停5板", false);
            eChartsResult.legend_selected.put("跌停2板", false);
            eChartsResult.legend_selected.put("跌停3板", false);
            eChartsResult.legend_selected.put("跌停4板", false);
            eChartsResult.legend_selected.put("跌停5板", false);
            eChartsResult.legend_selected.put("25日翻倍", false);
            eChartsResult.legend_selected.put("25日折半", false);
            eChartsResult.legend_selected.put("上下幅度超过12%", false);

            for (DFeelingUpDown10 item : listData) {
                eChartsResult.xAxis_data[x] = item.getTrade_date();
                eChartsResult.series_data[0][x] = item.getUp10();
                eChartsResult.series_data[1][x] = item.getUp10nc();
                eChartsResult.series_data[2][x] = -item.getDown10();
                eChartsResult.series_data[3][x] = -item.getDown10nc();
                eChartsResult.series_data[4][x] = item.getUp10down();
                eChartsResult.series_data[5][x] = -item.getDown10up();
                eChartsResult.series_data[6][x] = item.getUp10d2();
                eChartsResult.series_data[7][x] = item.getUp10d3();
                eChartsResult.series_data[8][x] = item.getUp10d4();
                eChartsResult.series_data[9][x] = item.getUp10d5();
                eChartsResult.series_data[10][x] = -item.getDown10d2();
                eChartsResult.series_data[11][x] = -item.getDown10d3();
                eChartsResult.series_data[12][x] = -item.getDown10d4();
                eChartsResult.series_data[13][x] = -item.getDown10d5();
                eChartsResult.series_data[14][x] = item.getUpdouble25d();
                eChartsResult.series_data[15][x] = -item.getDowndouble25d();
                eChartsResult.series_data[16][x] = item.getUpdown12();
                x++;
            }

            listData.clear();

            try {
                objMapper.writeValue(witStr, eChartsResult);
                //logger.info("toJSONString<" + witStr + ">");
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 获取每天总市值、换手率、收盘价的汇总数据
     *
     * @return
     */
    public String getEveryDayTotal() {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();
        List<DEveryDayTotal> listData;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            listData = dAnalyseStockService.selectEveryDayTotal();

            int x = 0;
            String[] legend = {"上市变动", "深市变动", "上证均价", "深证均价", "上证换手", "深证换手"};
            EChartsResult_02 eChartsResult = new EChartsResult_02(listData.size(), legend.length);

            eChartsResult.legend_data = legend.clone();
            eChartsResult.legend_selected.clear();
            eChartsResult.legend_selected.put("上市变动", true);
            eChartsResult.legend_selected.put("深市变动", true);
            eChartsResult.legend_selected.put("上证均价", true);
            eChartsResult.legend_selected.put("深证均价", true);
            eChartsResult.legend_selected.put("上证换手", true);
            eChartsResult.legend_selected.put("深证换手", true);

            for (DEveryDayTotal item : listData) {
                eChartsResult.xAxis_data[x] = item.getTrade_date();
                eChartsResult.series_data[0][x] = item.getSh_close();
                eChartsResult.series_data[1][x] = item.getSz_close();
                eChartsResult.series_data[2][x] = item.getSh_turnover_rate();
                eChartsResult.series_data[3][x] = item.getSz_turnover_rate();
                eChartsResult.series_data[4][x] = item.getSh_total_mv();
                eChartsResult.series_data[5][x] = item.getSz_total_mv();

                x++;
            }

            listData.clear();

            try {
                objMapper.writeValue(witStr, eChartsResult);
                //logger.info("toJSONString<" + witStr + ">");
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 获取每天融资、融券数据
     *
     * @return
     */
    public String getEveryDayMargin() {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();
        List<D2IndexData> index_000001;
        List<DEveryDayHsgt> listHsgt;
        List<DEveryDayMargin> listMargin;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("ts_code", "000001.SH"); //上证综指
            index_000001 = dAnalyseStockService.select2IndexData(param);

            listHsgt = dAnalyseStockService.selectEveryDayHsgt();
            listMargin = dAnalyseStockService.selectEveryDayMargin();

            int x = 0;
            String[] legend = {"上证融资", "深证融资", "上证融券", "深证融券", "融资余额", "北上资金", "南下资金"};
            EChartsResult_02 eChartsResult = new EChartsResult_02(index_000001.size(), legend.length);

            eChartsResult.legend_data = legend.clone();
            eChartsResult.legend_selected.clear();
            eChartsResult.legend_selected.put("上证融资", false);
            eChartsResult.legend_selected.put("深证融资", false);
            eChartsResult.legend_selected.put("上证融券", false);
            eChartsResult.legend_selected.put("深证融券", false);
            eChartsResult.legend_selected.put("融资余额", true);
            eChartsResult.legend_selected.put("北上资金", true);
            eChartsResult.legend_selected.put("南下资金", true);

            for (x = 0; x < index_000001.size(); x++) {
                D2IndexData item_000001 = index_000001.get(x);

                eChartsResult.xAxis_data[x] = item_000001.getTrade_date();
                eChartsResult.series_data[0][x] = 0;
                eChartsResult.series_data[1][x] = 0;
                eChartsResult.series_data[2][x] = 0;
                eChartsResult.series_data[3][x] = 0;
                eChartsResult.series_data[4][x] = 0;
                eChartsResult.series_data[5][x] = 0;
                eChartsResult.series_data[6][x] = 0;

                int sz_margin = index_000001.size() - listMargin.size();
                if (x >= sz_margin) {
                    DEveryDayMargin item = listMargin.get(x - sz_margin);
                    eChartsResult.series_data[0][x] = item.getSh_rzye();
                    eChartsResult.series_data[1][x] = item.getSz_rzye();
                    eChartsResult.series_data[2][x] = item.getSh_rqye();
                    eChartsResult.series_data[3][x] = item.getSz_rqye();
                    eChartsResult.series_data[4][x] = item.getAll_rzye();
                }

                int sz_hsgt = index_000001.size() - listHsgt.size();
                if (x >= sz_hsgt) {
                    DEveryDayHsgt item = listHsgt.get(x - sz_hsgt);
                    eChartsResult.series_data[5][x] = item.getNorth_money();
                    eChartsResult.series_data[6][x] = item.getSouth_money();
                }
            }

            listMargin.clear();
            index_000001.clear();

            try {
                objMapper.writeValue(witStr, eChartsResult);
                //logger.info("toJSONString<" + witStr + ">");
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    private int getChanDirection(ChanLine a, ChanLine b) {
        return getChanDirection(a.highPoint.close_val, a.lowPoint.close_val, b.highPoint.close_val, b.lowPoint.close_val);
    }

    private int getChanDirection(ChanPoint a1, ChanPoint a2, ChanPoint b1, ChanPoint b2) {
        return getChanDirection(a1.close_val, a2.close_val, b1.close_val, b2.close_val);
    }

    private int getChanDirection(double a1, double a2, double b1, double b2) {
        int re; // -1 下跌 0 盘整 1 上升
        double aH, aL, bH, bL;

        if (a1 > a2) {
            aH = a1;
            aL = a2;
        } else {
            aH = a2;
            aL = a1;
        }

        if (b1 > b2) {
            bH = b1;
            bL = b2;
        } else {
            bH = b2;
            bL = b1;
        }

        if (bH == aH || bL == aL) {
            re = 0;
        } else if (bH > aH && bL > aL) {
            re = 1;
        } else if (bH < aH && bL < aL) {
            re = -1;
        } else {
            re = 0;
        }

        return re;
    }

    private List<ChanPoint> analyseChanPoint_index(String ts_code, List<D2IndexData> rich_data) {
        List<D2BaseData> re = new ArrayList<>();
        for (int i = 0; i < rich_data.size(); i++) {
            D2BaseData item = rich_data.get(i);
            re.add(item);
        }
        return analyseChanPoint(ts_code, re);
    }

    private List<D2BaseData> clearK_index(List<D2IndexData> rich_data) {
        List<D2BaseData> re = new ArrayList<>();
        for (int i = 0; i < rich_data.size(); i++) {
            D2BaseData item = rich_data.get(i);
            re.add(item);
        }
        return clearK(re);
    }

    /**
     * 合并存在包含关系的K线
     *
     * @param rich_data
     * @return
     */
    private List<D2BaseData> mergeK(List<D2BaseData> rich_data) {
        System.out.println("Begin->mergeK.size: " + rich_data.size());

        int beforeDirection = 0, nowDirection; // -1 下跌 0 盘整 1 向上
        D2BaseData beforeItem = null;
        List<D2BaseData> clear_data = new ArrayList<>();

        if (rich_data.size() > 1) {
            D2BaseData b = rich_data.get(0);
            D2BaseData e = rich_data.get(1);
            if (b.getHigh_val() > e.getHigh_val() && b.getLow_val() > e.getLow_val()) {
                beforeDirection = -1;
            } else if (b.getHigh_val() < e.getHigh_val() && b.getLow_val() < e.getLow_val()) {
                beforeDirection = 1;
            }
        }

        // 做包含与合并操作
        for (int x = 1; x < rich_data.size(); x++) {
            double a1, a2, b1, b2;
            D2BaseData b = rich_data.get(x - 1);
            D2BaseData e = rich_data.get(x);

            if (beforeItem == null) {
                a1 = b.getLow_val();
                a2 = b.getHigh_val();
            } else {
                a1 = beforeItem.getLow_val();
                a2 = beforeItem.getHigh_val();
            }

            b1 = e.getLow_val();
            b2 = e.getHigh_val();

            nowDirection = getChanDirection(a1, a2, b1, b2);

            switch (nowDirection) {
                case 1:
                case -1:
                    if (beforeItem == null) {
                        clear_data.add(b);
                    } else {
                        clear_data.add(beforeItem);
                    }
                    beforeItem = null;
                    beforeDirection = nowDirection;
                    break;
                case 0: // 当前包含盘整
                    switch (beforeDirection) {
                        case 1: // 之前上升
                            if (beforeItem == null) {
                                beforeItem = new D2BaseData();
                                beforeItem.setHigh_val(b.getHigh_val() > e.getHigh_val() ? b.getHigh_val() : e.getHigh_val());
                                beforeItem.setLow_val(b.getLow_val() > e.getLow_val() ? b.getLow_val() : e.getLow_val());
                                beforeItem.setTrade_date(b.getHigh_val() > e.getHigh_val() ? b.getTrade_date() : e.getTrade_date());
                            } else {
                                beforeItem.setHigh_val(beforeItem.getHigh_val() > e.getHigh_val() ? beforeItem.getHigh_val() : e.getHigh_val());
                                beforeItem.setLow_val(beforeItem.getLow_val() > e.getLow_val() ? beforeItem.getLow_val() : e.getLow_val());
                                beforeItem.setTrade_date(beforeItem.getHigh_val() > e.getHigh_val() ? beforeItem.getTrade_date() : e.getTrade_date());
                            }
                            beforeItem.setOpen_val(beforeItem.getLow_val());
                            beforeItem.setClose_val(beforeItem.getHigh_val());
                            break;
                        case -1: // 之前下跌
                            if (beforeItem == null) {
                                beforeItem = new D2BaseData();
                                beforeItem.setHigh_val(b.getHigh_val() < e.getHigh_val() ? b.getHigh_val() : e.getHigh_val());
                                beforeItem.setLow_val(b.getLow_val() < e.getLow_val() ? b.getLow_val() : e.getLow_val());
                                beforeItem.setTrade_date(b.getLow_val() < e.getLow_val() ? b.getTrade_date() : e.getTrade_date());
                            } else {
                                beforeItem.setHigh_val(beforeItem.getHigh_val() < e.getHigh_val() ? beforeItem.getHigh_val() : e.getHigh_val());
                                beforeItem.setLow_val(beforeItem.getLow_val() < e.getLow_val() ? beforeItem.getLow_val() : e.getLow_val());
                                beforeItem.setTrade_date(beforeItem.getLow_val() < e.getLow_val() ? beforeItem.getTrade_date() : e.getTrade_date());
                            }
                            beforeItem.setOpen_val(beforeItem.getHigh_val());
                            beforeItem.setClose_val(beforeItem.getLow_val());
                            break;
                        case 0: // 之前盘整
                    }
            }

            if (x == rich_data.size() - 1) {
                // 最后一个
                if (beforeItem == null) {
                    clear_data.add(e);
                } else {
                    clear_data.add(beforeItem);
                }
            }
        }

        System.out.println("End->mergeK.size: " + clear_data.size());

        return clear_data;
    }

    /**
     * 做到无可合并后的K线
     *
     * @param rich_k
     * @return
     */
    private List<D2BaseData> clearK(List<D2BaseData> rich_k) {
        int oldCount, clearCount;
        List<D2BaseData> clear_data, old_data;

        // 合并包含关系的K线
        old_data = rich_k;
        while (true) {
            oldCount = old_data.size();
            clear_data = mergeK(old_data);
            clearCount = clear_data.size();
            old_data = clear_data;

            if (clearCount == oldCount) break;
        }

        return clear_data;
    }

    private ChanPoint getMaxChanPoint(ChanPoint maxP, ChanPoint a1, ChanPoint a2, ChanPoint b1, ChanPoint b2) {
        ChanPoint t = getMaxChanPoint(a1, a2, b1, b2);
        return maxP.close_val > t.close_val ? maxP : t;
    }

    private ChanPoint getMaxChanPoint(ChanPoint a1, ChanPoint a2, ChanPoint b1, ChanPoint b2) {
        ChanPoint re;

        re = a1.close_val > a2.close_val ? a1 : a2;
        re = re.close_val > b1.close_val ? re : b1;
        re = re.close_val > b2.close_val ? re : b2;

        return re;
    }

    private ChanPoint getMinChanPoint(ChanPoint minP, ChanPoint a1, ChanPoint a2, ChanPoint b1, ChanPoint b2) {
        ChanPoint t = getMinChanPoint(a1, a2, b1, b2);
        return minP.close_val < t.close_val ? minP : t;
    }

    private ChanPoint getMinChanPoint(ChanPoint a1, ChanPoint a2, ChanPoint b1, ChanPoint b2) {
        ChanPoint re;

        re = a1.close_val < a2.close_val ? a1 : a2;
        re = re.close_val < b1.close_val ? re : b1;
        re = re.close_val < b2.close_val ? re : b2;

        return re;
    }

    /**
     * 过滤不符合要求的顶底与重复数据
     *
     * @param pointList
     * @return
     */
    private List<ChanPoint> clearChanPoint(List<ChanPoint> pointList) {
        System.out.println("Begin->clearChanPoint.size: " + pointList.size());

        // 0:顶分型 1:底分型
        int direction;
        List<ChanPoint> re_pb = new ArrayList<>();
        int x = 3, beforeX = x;
        while (x < pointList.size()) {
            ChanPoint a1 = pointList.get(x - 3);
            ChanPoint a2 = pointList.get(x - 2);
            ChanPoint b1 = pointList.get(x - 1);
            ChanPoint b2 = pointList.get(x);

            beforeX = x;
            direction = getChanDirection(a1, a2, b1, b2);

            /*if (x < 20) {
                System.out.println("direction:" + direction);
                System.out.println(a1);
                System.out.println(a2);
                System.out.println(b1);
                System.out.println(b2);
                System.out.println("-----");
            }*/

            if (direction != 0) {
                // 有趋势则处理
                if (Math.abs(a2.arr_pos - b1.arr_pos) < 4 || Math.abs(a1.arr_pos - a2.arr_pos) < 4 || Math.abs(b1.arr_pos - b2.arr_pos) < 4) {
                    // 不合规
                    if ((direction == -1 && a1.close_val < a2.close_val) || (direction == 1 && a1.close_val > a2.close_val)) {
                        // 线段与趋势向逆
                        re_pb.add(a1);
                        x += 1;
                    } else {
                        // 线段与趋势向顺
                        re_pb.add(a1);
                        re_pb.add(b2);
                        x += 3;
                    }
                } else {
                    re_pb.add(a1);
                    x += 1;
                }
            } else {
                // 盘整
                re_pb.add(a1);
                x += 1;
            }

            if (x >= pointList.size()) {
                if ((x - beforeX) < 3) {
                    re_pb.add(a2);
                    re_pb.add(b1);
                    re_pb.add(b2);
                }
            }
        }

        int lastCount = (x - beforeX) - (x - (pointList.size() - 1));
        //int lastCount = (x - beforeX);
        if (lastCount > 0) {
            for (x = lastCount; x > 0; x--) {
                re_pb.add(pointList.get(pointList.size() - x));
            }
        }

        System.out.println("Middle01->clearChanPoint.size: " + re_pb.size());

        // 清除重复的分型点
        List<ChanPoint> clear_data = new ArrayList<>();
        ChanPoint beforePoint = null;
        if (re_pb.size() > 0) {
            beforePoint = re_pb.get(0);
        }
        for (x = 1; x < re_pb.size(); x++) {
            ChanPoint p = re_pb.get(x);
            // System.out.println(p);
            if (p.trade_date.equals(beforePoint.trade_date)) {
                // 相同的过滤
            } else {
                clear_data.add(beforePoint);
                beforePoint = new ChanPoint();
                beforePoint.trade_date = p.trade_date;
                beforePoint.close_val = p.close_val;
                beforePoint.arr_pos = p.arr_pos;
                beforePoint.tp = p.tp;
            }
        }

        if (re_pb.size() > 2 && !re_pb.get(re_pb.size() - 1).trade_date.equals(re_pb.get(re_pb.size() - 2).trade_date)) {
            clear_data.add(re_pb.get(re_pb.size() - 1));
        }

        System.out.println("End->clearChanPoint.size: " + clear_data.size());

        return clear_data;
    }

    /**
     * 分析顶底分型
     *
     * @param rich_data
     * @return
     */
    private List<ChanPoint> analyseChanPoint(String ts_code, List<D2BaseData> rich_data) {
        List<D2BaseData> clear_k;

        // 合并包含关系的K线
        clear_k = clearK(rich_data);

        // 获取所有顶底分型
        boolean bReady = false;
        List<ChanPoint> re_pa = new ArrayList<>();
        D2BaseData firstItem = clear_k.get(0);
        ChanPoint firstPoint = new ChanPoint();
        firstPoint.arr_pos = 0;
        firstPoint.trade_date = firstItem.getTrade_date();

        for (int x = 2; x < (clear_k.size() - 1); x++) {
            D2BaseData item = clear_k.get(x);
            D2BaseData b = clear_k.get(x - 1);
            D2BaseData e = clear_k.get(x + 1);

            //System.out.println(item.toString());
            // 0:顶分型 1:底分型
            if (item.getHigh_val() > b.getHigh_val() && item.getHigh_val() > e.getHigh_val()
                    && item.getLow_val() > b.getLow_val() && item.getLow_val() > e.getLow_val()) {
                //判断顶分型
                ChanPoint t = new ChanPoint();
                t.tp = 0;
                t.arr_pos = x;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getHigh_val();

                if (bReady == false) {
                    bReady = true;
                    firstPoint.tp = 1;
                    firstPoint.close_val = firstItem.getLow_val();
                    re_pa.add(firstPoint);
                }

                re_pa.add(t);
            } else if (item.getLow_val() < b.getLow_val() && item.getLow_val() < e.getLow_val()
                    && item.getHigh_val() < b.getHigh_val() && item.getHigh_val() < e.getHigh_val()) {
                //判断底分型
                ChanPoint t = new ChanPoint();
                t.tp = 1;
                t.arr_pos = x;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getLow_val();

                if (bReady == false) {
                    bReady = true;
                    firstPoint.tp = 0;
                    firstPoint.close_val = firstItem.getHigh_val();
                    re_pa.add(firstPoint);
                }

                re_pa.add(t);
            }
        }

        System.out.println("(清理前)顶底分型数量：" + re_pa.size());

        // 过滤线段的小微波
        int oldCount, clearCount;
        List<ChanPoint> re_pb, re_pc;
        re_pb = re_pa;
        while (true) {
            oldCount = re_pb.size();
            re_pc = clearChanPoint(re_pb);
            clearCount = re_pc.size();
            re_pb = re_pc;

            //break;
            if (clearCount == oldCount) break;
        }

        System.out.println("(清理后)顶底分型数量：" + re_pa.size());

        // 通过配置表数据，手动补充
        List<ChanPoint> re_pf = new ArrayList<>();
        List<D2CfgFixResult> listCfg = getCfgFixResult(ts_code, "point_1f");
        HashMap<String, String> hsCfg = new HashMap<>();
        for (D2CfgFixResult cfg : listCfg) {
            hsCfg.put(cfg.getMatcher(), cfg.getData_json());
            System.out.println("Key:" + cfg.getMatcher() + " -> " + cfg.getData_json());
        }
        System.out.println("待补充配置记录数：" + hsCfg.size());
        for (ChanPoint item : re_pc) {
            re_pf.add(item);
            String key = item.trade_date;
            if (hsCfg.containsKey(key)) {
                System.out.println("Found Key:" + key);
                ChanPoint[] listTem = JsonUtil.getEntity(hsCfg.get(key), ChanPoint[].class);
                for (ChanPoint cp : listTem) {
                    re_pf.add(cp);
                    System.out.println("补充：" + cp.toString());
                }
            }
        }

        System.out.println("(补充后)顶底分型数量：" + re_pa.size());

        return re_pf;
        //return re_pc;
        //return re_pa;
    }

    private ChanLine makeChanLine(ChanPoint point1, ChanPoint point2) {
        ChanLine line = new ChanLine();

        if (point1.close_val > point2.close_val) {
            line.highPoint = point1;
            line.lowPoint = point2;
        } else {
            line.highPoint = point2;
            line.lowPoint = point1;
        }

        return line;
    }

    /**
     * 分析线段
     *
     * @param data
     * @return
     */
    private List<ChanPoint> analyseChanLine_v2(List<ChanPoint> data) {
        List<ChanPoint> re = new ArrayList<>();
        ChanLine beforeItem = null;
        int beforeDirection = 0, nowDirection; // -1 下跌 0 盘整 1 向上
        List<ChanLine> rich_line = new ArrayList<>();
        List<ChanLine> clear_line = new ArrayList<>();

        //************ 做线段合并处理 ***************
        for (int n = 0; n < (data.size() - 1); n += 2) {
            ChanPoint point1 = data.get(n);
            ChanPoint point2 = data.get(n + 1);
            ChanLine line = makeChanLine(point1, point2);
            rich_line.add(line);
        }

        // System.out.println("RichLine0: " + rich_line.get(0).toString());

        if (rich_line.size() > 1) {
            ChanLine b = rich_line.get(0);
            ChanLine e = rich_line.get(1);
            if (b.highPoint.close_val > e.highPoint.close_val && b.lowPoint.close_val > e.lowPoint.close_val) {
                beforeDirection = -1;
            } else if (b.highPoint.close_val < e.highPoint.close_val && b.lowPoint.close_val < e.lowPoint.close_val) {
                beforeDirection = 1;
            }
        }

        // 做包含与合并操作
        for (int x = 1; x < rich_line.size(); x++) {
            double a1, a2, b1, b2;
            ChanLine b = rich_line.get(x - 1);
            ChanLine e = rich_line.get(x);

            if (beforeItem == null) {
                a1 = b.lowPoint.close_val;
                a2 = b.highPoint.close_val;
            } else {
                a1 = beforeItem.lowPoint.close_val;
                a2 = beforeItem.highPoint.close_val;
            }

            b1 = e.lowPoint.close_val;
            b2 = e.highPoint.close_val;

            nowDirection = getChanDirection(a1, a2, b1, b2);

            switch (nowDirection) {
                case 1:
                case -1:
                    if (beforeItem == null) {
                        clear_line.add(b);
                    } else {
                        clear_line.add(beforeItem);
                    }
                    beforeItem = null;
                    beforeDirection = nowDirection;
                    break;
                case 0: // 当前包含盘整
                    switch (beforeDirection) {
                        case 1: // 之前上升
                            if (beforeItem == null) {
                                beforeItem = new ChanLine();
                                beforeItem.highPoint = (b.highPoint.close_val > e.highPoint.close_val ? b.highPoint : e.highPoint);
                                beforeItem.lowPoint = (b.lowPoint.close_val > e.lowPoint.close_val ? b.lowPoint : e.lowPoint);
                            } else {
                                beforeItem.highPoint = (beforeItem.highPoint.close_val > e.highPoint.close_val ? beforeItem.highPoint : e.highPoint);
                                beforeItem.lowPoint = (beforeItem.lowPoint.close_val > e.lowPoint.close_val ? beforeItem.lowPoint : e.lowPoint);
                            }
                            break;
                        case -1: // 之前下跌
                            if (beforeItem == null) {
                                beforeItem = new ChanLine();
                                beforeItem.highPoint = (b.highPoint.close_val < e.highPoint.close_val ? b.highPoint : e.highPoint);
                                beforeItem.lowPoint = (b.lowPoint.close_val < e.lowPoint.close_val ? b.lowPoint : e.lowPoint);
                            } else {
                                beforeItem.highPoint = (beforeItem.highPoint.close_val < e.highPoint.close_val ? beforeItem.highPoint : e.highPoint);
                                beforeItem.lowPoint = (beforeItem.lowPoint.close_val < e.lowPoint.close_val ? beforeItem.lowPoint : e.lowPoint);
                            }
                            break;
                        case 0: // 之前盘整
                    }
            }
        }

        // System.out.println("ClearLine: " + clear_line.size());

        //************ 做线段趋势处理 ***************
        // 开头
        boolean bFindFirst = false;
        ChanLine firstLine = clear_line.get(0);
        //System.out.println("CleanLine0: " + clear_line.get(0).toString());

        firstLine.arr_pos = 0;

        // 中间 找到所有顶底分型
        for (int x = 1; x < (clear_line.size() - 1); x++) {
            ChanLine item = clear_line.get(x);
            ChanLine b = clear_line.get(x - 1);
            ChanLine e = clear_line.get(x + 1);

            if (item.highPoint.close_val > b.highPoint.close_val && item.highPoint.close_val > e.highPoint.close_val
                    && item.lowPoint.close_val > b.lowPoint.close_val && item.lowPoint.close_val > e.lowPoint.close_val) {
                //判断顶分型
                //System.out.println("UP");
                if (bFindFirst == false) {
                    bFindFirst = true;
                    re.add(firstLine.lowPoint);
                }

                ChanPoint t = item.highPoint;
                t.arr_pos = x;
                re.add(t);
            } else if (item.lowPoint.close_val < b.lowPoint.close_val && item.lowPoint.close_val < e.lowPoint.close_val
                    && item.highPoint.close_val < b.highPoint.close_val && item.highPoint.close_val < e.highPoint.close_val) {
                //判断底分型
                //System.out.println("DOWN");
                if (bFindFirst == false) {
                    bFindFirst = true;
                    re.add(firstLine.highPoint);
                }

                ChanPoint t = item.lowPoint;
                t.arr_pos = x;
                re.add(t);
            }
        }

        System.out.println("ChanLine0: " + re.get(0).toString());

        return re;
    }

    /**
     * 计算乖离率
     *
     * @param ma_value
     * @param close_value
     * @return
     */
    private static double bias(double ma_value, double close_value) {
        double re = 0;

        if (ma_value > 0 && close_value > 0) {
            re = (close_value - ma_value) / ma_value * 100;
            re = (double) Math.round(re * 100) / 100;
        }

        return re;
    }

    private class Point {
        public String trade_date;
        public double close_val;

        public String toString() {
            return "P: " + trade_date + "|" + close_val;
        }
    }

    private Point coord2Point(String[] coord) {
        Point re = new Point();

        re.trade_date = coord[0];
        re.close_val = Double.parseDouble(coord[1]);

        return re;
    }

    private void getAddArea(Point beforeZD, Point beforeZG, List<AreaCoord> re) {
        if (beforeZD != null && beforeZG != null) {
            if (Integer.parseInt(beforeZD.trade_date) > Integer.parseInt(beforeZG.trade_date)) {
                AreaCoord area1 = new AreaCoord();
                area1.coord[0] = beforeZG.trade_date;
                area1.coord[1] = beforeZG.close_val + "";
                re.add(area1);
                AreaCoord area2 = new AreaCoord();
                area2.coord[0] = beforeZD.trade_date;
                area2.coord[1] = beforeZD.close_val + "";
                re.add(area2);
            } else {
                AreaCoord area1 = new AreaCoord();
                area1.coord[0] = beforeZD.trade_date;
                area1.coord[1] = beforeZD.close_val + "";
                re.add(area1);
                AreaCoord area2 = new AreaCoord();
                area2.coord[0] = beforeZG.trade_date;
                area2.coord[1] = beforeZG.close_val + "";
                re.add(area2);
            }
        }
    }

    /**
     * 线段 分析 区域
     *
     * @param arrLine
     * @return
     */
    private List<AreaCoord> analyseChanArea(List<LineCoord>[] arrLine) {
        int continueCount = 0;
        String beforeLastTradeDate = "", nowFirstTradeDate = "";
        Point t, a1, a2, b1, b2, c1, c2, d1, d2, zd, zg, beforeZD = null, beforeZG = null;
        List<AreaCoord> re = new ArrayList<>();

        /*for (int n = 0; n < arrLine.length; n++) {
            List<LineCoord> aL = arrLine[n];
            a1 = coord2Point(aL.get(0).coord);
            a2 = coord2Point(aL.get(1).coord);
            System.out.println("Line: " + a1 + " " + a2);
        }*/

        for (int n = 3; n < arrLine.length; n++) {
            List<LineCoord> aL = arrLine[n - 3];
            List<LineCoord> bL = arrLine[n - 2];
            List<LineCoord> cL = arrLine[n - 1];
            List<LineCoord> dL = arrLine[n];

            a1 = coord2Point(aL.get(0).coord);
            a2 = coord2Point(aL.get(1).coord);
            b1 = coord2Point(bL.get(0).coord);
            b2 = coord2Point(bL.get(1).coord);
            c1 = coord2Point(cL.get(0).coord);
            c2 = coord2Point(cL.get(1).coord);
            d1 = coord2Point(dL.get(0).coord);
            d2 = coord2Point(dL.get(1).coord);

            if (a1.close_val > a2.close_val) {
                t = a1;
                a1 = a2;
                a2 = t;
            }
            if (b1.close_val > b2.close_val) {
                t = b1;
                b1 = b2;
                b2 = t;
            }
            if (c1.close_val > c2.close_val) {
                t = c1;
                c1 = c2;
                c2 = t;
            }
            if (d1.close_val > d2.close_val) {
                t = d1;
                d1 = d2;
                d2 = t;
            }

            if (beforeLastTradeDate.length() == 0) {
                beforeLastTradeDate = a1.trade_date;
            }

            if (Integer.parseInt(a1.trade_date) > Integer.parseInt(a2.trade_date)) {
                nowFirstTradeDate = a2.trade_date;
            } else {
                nowFirstTradeDate = a1.trade_date;
            }

            if (continueCount == 0 && Integer.parseInt(beforeLastTradeDate) <= Integer.parseInt(nowFirstTradeDate) &&
                    ((a1.close_val == b1.close_val && b2.close_val == c2.close_val && c1.close_val == d1.close_val && a1.close_val < d2.close_val && a2.close_val > d1.close_val)
                            || (a2.close_val == b2.close_val && b1.close_val == c1.close_val && c2.close_val == d2.close_val && a2.close_val > d1.close_val && a1.close_val < d2.close_val))) {
                // 低点,高点,低点 或者 高点,低点,高点 并且有重合
                zd = a1.close_val > b1.close_val ? a1 : b1;
                zd = zd.close_val > c1.close_val ? zd : c1;
                zd = zd.close_val > d1.close_val ? zd : d1;

                zg = a2.close_val > b2.close_val ? b2 : a2;
                zg = zg.close_val > c2.close_val ? c2 : zg;
                zg = zg.close_val > d2.close_val ? d2 : zg;

                if (Integer.parseInt(a1.trade_date) > Integer.parseInt(a2.trade_date)) {
                    zd.trade_date = a1.trade_date;
                    zg.trade_date = d1.trade_date;
                } else {
                    zd.trade_date = a2.trade_date;
                    zg.trade_date = d2.trade_date;
                }

                continueCount = 1;
                beforeZD = zd;
                beforeZG = zg;

            } else if (continueCount > 0) {
                // 之前有中枢，判断当前线段是否进入中枢
                if (d1.close_val <= beforeZG.close_val && d2.close_val >= beforeZD.close_val) {
                    if (Integer.parseInt(d1.trade_date) > Integer.parseInt(d2.trade_date)) {
                        beforeZG.trade_date = d2.trade_date;
                    } else {
                        beforeZG.trade_date = d1.trade_date;
                    }
                    beforeLastTradeDate = beforeZG.trade_date;
                    continueCount++;
                } else {
                    getAddArea(beforeZD, beforeZG, re);
                    continueCount = 0;
                    beforeZD = null;
                    beforeZG = null;
                }
            } else {
                getAddArea(beforeZD, beforeZG, re);
                continueCount = 0;
                beforeZD = null;
                beforeZG = null;
            }
        }

        if (continueCount > 0 && beforeZD != null && beforeZG != null) {
            getAddArea(beforeZD, beforeZG, re);
        }

        return re;
    }

    class TotalBetweenDateData {
        public int dayCount = 0;
        public double sumVol = 0;
        public double chgRate = 0;

        public String toString() {
            return "周期数：" + dayCount + "<br>成交量：" + sumVol + "<br>振幅：" + chgRate;
        }
    }

    private TotalBetweenDateData getTotalIndexBetweenDateInfo(DAnalyseStockServiceI dAnalyseStockService, String ts_code, String begin_date, String end_date) {
        TotalBetweenDateData re = new TotalBetweenDateData();
        HashMap<String, String> param = new HashMap<>();
        D2TotalIndexBetweenDate data;

        param.put("ts_code", ts_code);
        if (Integer.parseInt(begin_date) > Integer.parseInt(end_date)) {
            param.put("begin_date", end_date);
            param.put("end_date", begin_date);
        } else {
            param.put("begin_date", begin_date);
            param.put("end_date", end_date);
        }

        data = dAnalyseStockService.select2TotalIndexBetweenDate(param);
        if (data != null) {
            re.dayCount = data.getDay_count();
            re.sumVol = (double) Math.round(data.getSum_vol() / 10000 * 100) / 100;
            re.chgRate = data.getChg_rate();
        }

        // System.out.println("getTotalIndexBetweenDateInfo(" + ts_code + ", " + begin_date + ", " + end_date + ")-> " + data);

        return re;
    }

    /**
     * 根据编码、类别获取补充的配置数据
     *
     * @param ts_code
     * @param class_str
     * @return
     */
    private List<D2CfgFixResult> getCfgFixResult(String ts_code, String class_str) {
        List<D2CfgFixResult> re = null;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            HashMap<String, String> param = new HashMap<>();

            param.put("ts_code", ts_code);
            param.put("class_str", class_str);
            re = dAnalyseStockService.selectCfgFixResult(param);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return re;
    }

    /**
     * 获取指数的技术分析数据
     *
     * @return
     */
    private void getIndexesTech(String ts_code, String ts_name) {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();

        try {
            EChartsResult_K eChartsResult = new EChartsResult_K(ts_code, ts_name);

            // 装载数据库里的数据
            eChartsResult.load();

            try {
                objMapper.writeValue(witStr, eChartsResult);
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据指数编码获取指数预测数据
     *
     * @param ts_code
     * @param ts_name
     */
    private void getIndexesProphet(String ts_code, String ts_name) {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();

        try {
            EChartsResult_Prophet eChartsResult = new EChartsResult_Prophet(ts_code, ts_name);

            // 装载数据库里的数据
            eChartsResult.load();

            try {
                objMapper.writeValue(witStr, eChartsResult.series_data);
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * 分析指数的技术分析数据并保存到表中
     *
     * @return
     */
    private void anaIndexesTech(String ts_code, String ts_name) {
        List<D2IndexData> index_data, index_mindate_year;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            HashMap<String, String> param = new HashMap<>();

            param.put("ts_code", ts_code);
            index_data = dAnalyseStockService.select2IndexData(param);
            index_mindate_year = dAnalyseStockService.select2IndexMinDateByYear(param);

            String[] legend = {ts_name, "成交量", "EMA:5", "EMA:10", "EMA:20", "EMA:30", "EMA:250", "BIAS:5", "BIAS:20", "MACD", "PSY"};
            EChartsResult_K eChartsResult = new EChartsResult_K(ts_code, ts_name, index_data.size(), legend.length - 1);

            eChartsResult.legend_data = legend.clone();
            eChartsResult.legend_selected.clear();
            eChartsResult.legend_selected.put(ts_name, true);
            eChartsResult.legend_selected.put("成交量", false);
            eChartsResult.legend_selected.put("EMA:5", false);
            eChartsResult.legend_selected.put("EMA:10", false);
            eChartsResult.legend_selected.put("EMA:20", false);
            eChartsResult.legend_selected.put("EMA:30", true);
            eChartsResult.legend_selected.put("EMA:250", false);
            eChartsResult.legend_selected.put("BIAS:5", false);
            eChartsResult.legend_selected.put("BIAS:20", false);
            eChartsResult.legend_selected.put("MACD", false);
            eChartsResult.legend_selected.put("PSY", false);

            // 填充指数数据、计算PSY
            int psy_n = 12;
            for (int x = 0; x < index_data.size(); x++) {
                D2IndexData item = index_data.get(x);

                eChartsResult.xAxis_data[x] = item.getTrade_date();
                eChartsResult.series_data_k[x][0] = item.getOpen_val();
                eChartsResult.series_data_k[x][1] = item.getClose_val();
                eChartsResult.series_data_k[x][2] = item.getLow_val();
                eChartsResult.series_data_k[x][3] = item.getHigh_val();
                eChartsResult.series_data_vol[x] = item.getVolume_val();

                // PSY = N日内上涨天数/N*100 参数N设置为12日
                if (x >= psy_n) {
                    int t1 = 0;
                    for (int n = 0; n < psy_n; n++) {
                        if (eChartsResult.series_data_k[x - n][0] < eChartsResult.series_data_k[x - n][1]
                                && eChartsResult.series_data_k[x - n][1] > eChartsResult.series_data_k[x - n - 1][1]) {
                            t1++;
                        }
                    }
                    eChartsResult.series_data_psy[x] = (double) Math.round((double) t1 / (double) psy_n * 100 * 10) / 10;
                }
            }

            // 计算均线、BIAS、MACD  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            Core taLibCore = new Core();
            MInteger taLibBegin = new MInteger();
            MInteger taLibLength = new MInteger();
            double[] close_val_data = new double[eChartsResult.series_data_k.length];

            for (int n = 0; n < close_val_data.length; n++) {
                close_val_data[n] = eChartsResult.series_data_k[n][1];
            }

            int cha;
            double[] taLibOut = new double[eChartsResult.series_data_k.length];
            taLibCore.ema(0, close_val_data.length - 1, close_val_data, 5, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data_ema_5.length - taLibLength.value;
                eChartsResult.series_data_ema_5[x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
                // BIAS 5 Day
                eChartsResult.series_data_bias_5[x + cha] = bias(eChartsResult.series_data_ema_5[x + cha], eChartsResult.series_data_k[x + cha][2]);
            }
            taLibCore.ema(0, close_val_data.length - 1, close_val_data, 20, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data_ema_20.length - taLibLength.value;
                eChartsResult.series_data_ema_20[x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
                // BIAS
                eChartsResult.series_data_bias_20[x + cha] = bias(eChartsResult.series_data_ema_20[x + cha], eChartsResult.series_data_k[x + cha][2]);
            }
            taLibCore.ema(0, close_val_data.length - 1, close_val_data, 10, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data_ema_10.length - taLibLength.value;
                eChartsResult.series_data_ema_10[x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
            }
            taLibCore.ema(0, close_val_data.length - 1, close_val_data, 30, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data_ema_30.length - taLibLength.value;
                eChartsResult.series_data_ema_30[x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
            }
            taLibCore.ema(0, close_val_data.length - 1, close_val_data, 250, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data_ema_250.length - taLibLength.value;
                eChartsResult.series_data_ema_250[x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
            }
            double[] taLibOutMACD = new double[close_val_data.length];
            double[] taLibOutMACDSignal = new double[close_val_data.length];
            double[] taLibOutMACDHist = new double[close_val_data.length];
            taLibCore.macd(0, close_val_data.length - 1, close_val_data, 12, 26, 9, taLibBegin, taLibLength, taLibOutMACD, taLibOutMACDSignal, taLibOutMACDHist);
            for (int x = 0; x < taLibLength.value; x++) {
                eChartsResult.series_data_macd[x + (eChartsResult.series_data_macd.length - taLibLength.value)] = (double) Math.round(taLibOutMACDHist[x] * 100) / 100;
            }

            // 标记超涨、超跌
            double bias5_down, bias20_up, bias20_down, bias5_20_down, bias5_20_up;
            switch (ts_code) {
                case "000852.SH": //"000852.SH", "中证1000"
                    bias20_up = 8;
                    bias5_20_up = 11.6;
                    bias5_down = -3.2;
                    bias20_down = -8.7;
                    bias5_20_down = -13.8;
                    break;
                case "399001.SZ": //"399001.SZ", "深证成指"
                    bias20_up = 5.5;
                    bias5_20_up = 16.8;
                    bias5_down = -2.4;
                    bias20_down = -5.8;
                    bias5_20_down = -12.6;
                    break;
                case "000001.SH": //"000001.SH", "上证综指"
                    bias20_up = 4.6;
                    bias5_20_up = 12.8;
                    bias5_down = -2.0;
                    bias20_down = -4.5;
                    bias5_20_down = -11.6;
                    break;
                default:
                    bias20_up = 8;
                    bias5_20_up = 12.8;
                    bias5_down = -3.2;
                    bias20_down = -8.7;
                    bias5_20_down = -10;
            }
            for (int x = 0; x < eChartsResult.xAxis_data.length; x++) {
                if ((eChartsResult.series_data_bias_5[x] + eChartsResult.series_data_bias_20[x]) <= bias5_20_down) {
                    PointCoord p = new PointCoord();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data_k[x][2] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#660000"; // 红
                    eChartsResult.markpoint_data.add(p);
                } else if (eChartsResult.series_data_bias_5[x] <= bias5_down && eChartsResult.series_data_bias_20[x] < bias20_down) {
                    PointCoord p = new PointCoord();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data_k[x][2] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#FFCC66"; // 橙
                    eChartsResult.markpoint_data.add(p);
                } /*else if ((eChartsResult.series_data_bias_5[x] + eChartsResult.series_data_bias_20[x]) >= bias5_20_up) {
                    PointCoord_base p = new PointCoord_base();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data_k[x][1] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#009966"; // 绿
                    eChartsResult.markpoint_data.add(p);
                } else if (eChartsResult.series_data_bias_20[x] >= bias20_up) {
                    PointCoord_base p = new PointCoord_base();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data_k[x][1] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#CCFF66"; // 浅绿
                    eChartsResult.markpoint_data.add(p);
                }*/
            }

            // 标记每年开始的第一个交易日 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            for (int x = 0; x < index_mindate_year.size(); x++) {
                D2IndexData item = index_mindate_year.get(x);
                PointCoord p = new PointCoord();
                p.coord[0] = item.getTrade_date();
                p.coord[1] = item.getHigh_val() + "";
                p.symbol = "pin";
                p.symbolSize = 20;
                p.value = item.getTrade_date().substring(0, 4);
                p.itemStyle.color = "red"; // 红
                eChartsResult.markpoint_data.add(p);
            }

            // 处理线段 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            List<ChanPoint> listChanLine_1f = analyseChanPoint_index(ts_code, index_data);
            System.out.println("ChanLine_1f.size: " + listChanLine_1f.size());
            List<ChanPoint> listChanLine_5f = analyseChanLine_v2(listChanLine_1f);
            System.out.println("ChanLine_5f.size: " + listChanLine_5f.size());
            //List<ChanPoint> listChanLine_30f = analyseChanLine_v2(listChanLine_5f);
            //System.out.println("ChanLine_30f.size: " + listChanLine_30f.size());

            index_data.clear();

            // 标注 顶底 分型
            for (int x = 0; x < listChanLine_1f.size(); x++) {
                ChanPoint item = listChanLine_1f.get(x);
                PointCoord p = new PointCoord();
                p.symbolSize = 6;
                p.itemStyle.color = "#FF3399";
                p.coord[0] = item.trade_date;
                p.coord[1] = item.close_val + "";
                eChartsResult.markpoint_data.add(p);
            }

            // 清除预测准备数据
            param.clear();
            param.put("ts_code", ts_code);
            param.put("class_str", "startLine");
            dAnalyseStockService.deleteProphetReady(param);

            int index;
            String msg;
            double e_v, b_v;
            List<LineCoord>[] markline_1f = new List[listChanLine_1f.size() - 1];
            List<LineCoord>[] markline_5f = new List[listChanLine_5f.size() - 1];
            eChartsResult.markline_data = new List[markline_5f.length + markline_1f.length];
            // 1F级线段
            index = 0;
            for (int x = 1; x < listChanLine_1f.size(); x++) {
                ChanPoint itemEnd = listChanLine_1f.get(x);
                ChanPoint itemFrom = listChanLine_1f.get(x - 1);
                List<LineCoord> listP = new ArrayList<>();
                LineCoord p1 = new LineCoord();
                LineCoord p2 = new LineCoord();

                p1.coord[0] = itemFrom.trade_date;
                p1.coord[1] = itemFrom.close_val + "";
                p1.lineStyle.color = "#CCCCCC";
                p2.coord[0] = itemEnd.trade_date;
                p2.coord[1] = itemEnd.close_val + "";
                p2.lineStyle.color = "#CCCCCC";

                TotalBetweenDateData totalData = getTotalIndexBetweenDateInfo(dAnalyseStockService, ts_code, p1.coord[0], p2.coord[0]);
                msg = totalData.toString();
                b_v = Double.parseDouble(p1.coord[1]);
                e_v = Double.parseDouble(p2.coord[1]);
                if (b_v > e_v) {
                    msg += "<br>50%位置：" + (double) Math.round(((b_v - e_v) / 2 + e_v) * 100) / 100;
                    msg += "<br>61.8%位置：" + (double) Math.round(((b_v - e_v) * 0.618 + e_v) * 100) / 100;
                } else {
                    msg += "<br>50%位置：" + (double) Math.round(((e_v - b_v) / 2 + b_v) * 100) / 100;
                    msg += "<br>61.8%位置：" + (double) Math.round(((e_v - b_v) * 0.618 + b_v) * 100) / 100;
                }
                p1.value = msg;
                p2.value = p1.value;
                listP.add(p1);
                listP.add(p2);
                markline_1f[index] = listP;
                eChartsResult.markline_data[index] = listP;

                //if (x < listChanLine_1f.size() - 1) {
                D2ProphetReady prophetReady = new D2ProphetReady();
                prophetReady.setTs_code(ts_code);
                prophetReady.setTrade_date(p1.coord[0].substring(0, 4) + "-" + p1.coord[0].substring(4, 6) + "-" + p1.coord[0].substring(6, 8));
                prophetReady.setClass_str("startLine");
                prophetReady.setY(totalData.dayCount);
                dAnalyseStockService.insertProphetReady(prophetReady);
                //}

                index++;
            }
            listChanLine_1f.clear();

            // 5F级线段
            for (int x = 1; x < listChanLine_5f.size(); x++) {
                ChanPoint itemEnd = listChanLine_5f.get(x);
                ChanPoint itemFrom = listChanLine_5f.get(x - 1);
                // System.out.println("L_f5: " + itemFrom.trade_date + " => " + itemEnd.trade_date);
                List<LineCoord> listP = new ArrayList<>();
                LineCoord p1 = new LineCoord();
                LineCoord p2 = new LineCoord();

                p1.coord[0] = itemFrom.trade_date;
                p1.coord[1] = itemFrom.close_val + "";
                p1.lineStyle.color = "#9999ff";
                p2.coord[0] = itemEnd.trade_date;
                p2.coord[1] = itemEnd.close_val + "";
                p2.lineStyle.color = "#9999ff";

                TotalBetweenDateData totalData = getTotalIndexBetweenDateInfo(dAnalyseStockService, ts_code, p1.coord[0], p2.coord[0]);
                msg = totalData.toString();
                b_v = Double.parseDouble(p1.coord[1]);
                e_v = Double.parseDouble(p2.coord[1]);
                if (b_v > e_v) {
                    msg += "<br>50%位置：" + (double) Math.round(((b_v - e_v) / 2 + e_v) * 100) / 100;
                    msg += "<br>61.8%位置：" + (double) Math.round(((b_v - e_v) * 0.618 + e_v) * 100) / 100;
                } else {
                    msg += "<br>61.8%位置：" + (double) Math.round(((e_v - b_v) * 0.618 + b_v) * 100) / 100;
                }
                p1.value = msg;
                p2.value = p1.value;
                listP.add(p1);
                listP.add(p2);
                markline_5f[index - markline_1f.length] = listP;
                eChartsResult.markline_data[index] = listP;
                index++;
            }
            listChanLine_5f.clear();

            /*// 30F级线段
            for (int x = 1; x < listChanLine_30f.size(); x++) {
                ChanPoint itemEnd = listChanLine_30f.get(x);
                ChanPoint itemFrom = listChanLine_30f.get(x - 1);
                List<LineCoord> listP = new ArrayList<>();
                LineCoord p1 = new LineCoord();
                LineCoord p2 = new LineCoord();

                p1.coord[0] = itemFrom.trade_date;
                p1.coord[1] = itemFrom.close_val + "";
                p1.lineStyle.color = "#FF3333";
                p2.coord[0] = itemEnd.trade_date;
                p2.coord[1] = itemEnd.close_val + "";
                p2.lineStyle.color = "#FF3333";

                listP.add(p1);
                listP.add(p2);
                eChartsResult.markline_data[index++] = listP;
            }
            listChanLine_30f.clear();*/

            // 处理区域 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            index = 0;
            List<AreaCoord> listAreaCoord_1f = analyseChanArea(markline_1f);
            List<AreaCoord> listAreaCoord_5f = analyseChanArea(markline_5f);
            System.out.println("listAreaCoord_1f.size(): " + listAreaCoord_1f.size());
            System.out.println("listAreaCoord_5f.size(): " + listAreaCoord_5f.size());
            eChartsResult.markarea_data = new List[listAreaCoord_1f.size() / 2 + listAreaCoord_5f.size() / 2];
            for (int x = 0; x < listAreaCoord_1f.size() - 1; x += 2) {
                List<AreaCoord> listP = new ArrayList<>();
                AreaCoord p1 = listAreaCoord_1f.get(x);
                AreaCoord p2 = listAreaCoord_1f.get(x + 1);
                p1.itemStyle.color = "rgba(255, 204, 204, 0.8)";
                p2.itemStyle.color = "rgba(255, 204, 204, 0.8)";
                TotalBetweenDateData totalData = getTotalIndexBetweenDateInfo(dAnalyseStockService, ts_code, p1.coord[0], p2.coord[0]);
                msg = totalData.toString();
                b_v = Double.parseDouble(p1.coord[1]);
                e_v = Double.parseDouble(p2.coord[1]);
                if (b_v > e_v) {
                    msg += "<br>50%位置：" + (double) Math.round(((b_v - e_v) / 2 + e_v) * 100) / 100;
                } else {
                    msg += "<br>50%位置：" + (double) Math.round(((e_v - b_v) / 2 + b_v) * 100) / 100;
                }
                p1.value = msg;
                p2.value = p1.value;
                listP.add(p1);
                listP.add(p2);
                eChartsResult.markarea_data[index++] = listP;
            }
            listAreaCoord_1f.clear();
            for (int x = 0; x < listAreaCoord_5f.size() - 1; x += 2) {
                List<AreaCoord> listP = new ArrayList<>();
                AreaCoord p1 = listAreaCoord_5f.get(x);
                AreaCoord p2 = listAreaCoord_5f.get(x + 1);
                p1.itemStyle.color = "rgba(255, 51, 153, 0.05)";
                p2.itemStyle.color = "rgba(255, 51, 153, 0.05)";
                listP.add(p1);
                listP.add(p2);
                eChartsResult.markarea_data[index++] = listP;
            }
            listAreaCoord_5f.clear();

            // 保存数据到数据库中
            eChartsResult.save();
            inputStream = new ByteArrayInputStream("Good".getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * 大盘指数每日指标
     *
     * @param ts_code
     * @param ts_name
     */
    private void getIndexesDailyBasic(String ts_code, String ts_name) {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();
        HashMap<String, String> param = new HashMap<>();
        DAnalyseStockServiceI dAnalyseStockService;
        List<D2IndexesDailyBasicResult> listData;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            param.put("ts_code", ts_code);
            listData = dAnalyseStockService.select2IndexesDailyBasic(param);

            int x = 0;
            String[] legend = {"总市值(亿元)", "流通市值(亿元)", "总股本(亿股)", "流通股本", "自由流通股本", "换手率", "换手率(基于自由流通股本)", "市盈率", "市盈率TTM", "市净率"};
            EChartsResult_02 eChartsResult = new EChartsResult_02(listData.size(), legend.length);

            eChartsResult.legend_data = legend.clone();
            eChartsResult.legend_selected.clear();
            eChartsResult.legend_selected.put("总市值(亿元)", false);
            eChartsResult.legend_selected.put("流通市值(亿元)", false);
            eChartsResult.legend_selected.put("总股本(亿股)", false);
            eChartsResult.legend_selected.put("流通股本", false);
            eChartsResult.legend_selected.put("自由流通股本", false);
            eChartsResult.legend_selected.put("换手率", false);
            eChartsResult.legend_selected.put("换手率(基于自由流通股本)", false);
            eChartsResult.legend_selected.put("市盈率", true);
            eChartsResult.legend_selected.put("市盈率TTM", false);
            eChartsResult.legend_selected.put("市净率", false);

            for (D2IndexesDailyBasicResult item : listData) {
                /*
                total_mv	float	Y	当日总市值（元）
                float_mv	float	Y	当日流通市值（元）
                total_share	float	Y	当日总股本（股）
                float_share	float	Y	当日流通股本（股）
                free_share	float	Y	当日自由流通股本（股）
                turnover_rate	float	Y	换手率
                turnover_rate_f	float	Y	换手率(基于自由流通股本)
                pe	float	Y	市盈率
                pe_ttm	float	Y	市盈率TTM
                pb	float	Y	市净率
                * */
                eChartsResult.xAxis_data[x] = item.getTrade_date();
                eChartsResult.series_data[0][x] = item.getTotal_mv();
                eChartsResult.series_data[1][x] = item.getFloat_mv();
                eChartsResult.series_data[2][x] = item.getTotal_share();
                eChartsResult.series_data[3][x] = item.getFloat_share();
                eChartsResult.series_data[4][x] = item.getFree_share();
                eChartsResult.series_data[5][x] = item.getTurnover_rate();
                eChartsResult.series_data[6][x] = item.getTurnover_rate_f();
                eChartsResult.series_data[7][x] = item.getPe();
                eChartsResult.series_data[8][x] = item.getPe_ttm();
                eChartsResult.series_data[9][x] = item.getPb();

                x++;
            }

            listData.clear();

            try {
                objMapper.writeValue(witStr, eChartsResult);
                //logger.info("toJSONString<" + witStr + ">");
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public String getIndexesTech_000852SH() {
        getIndexesTech("000852.SH", "中证1000");
        return SUCCESS;
    }

    public String getIndexesTech_000016SH() {
        getIndexesTech("000016.SH", "上证50");
        return SUCCESS;
    }

    public String getIndexesTech_000001SH() {
        getIndexesTech("000001.SH", "上证综指");
        return SUCCESS;
    }

    public String getIndexesTech_399001SZ() {
        getIndexesTech("399001.SZ", "深证成指");
        return SUCCESS;
    }

    public String getIndexesTech_399006SZ() {
        getIndexesTech("399006.SZ", "创业板指");
        return SUCCESS;
    }

    /**
     * 分析并保存数据
     *
     * @return
     */
    public String anaIndexesTech() {
        anaIndexesTech("000001.SH", "上证综指");
        anaIndexesTech("399001.SZ", "深证成指");
        anaIndexesTech("000852.SH", "中证1000");
        anaIndexesTech("399006.SZ", "创业板指");
        anaIndexesTech("000016.SH", "上证50");
        return SUCCESS;
    }

    public String getIndexesProphet_000001SH() {
        getIndexesProphet("000001.SH", "上证综指");
        return SUCCESS;
    }

    public String getIndexesDailyBasic_000001SH() {
        getIndexesDailyBasic("000001.SH", "上证综指");
        return SUCCESS;
    }

}
