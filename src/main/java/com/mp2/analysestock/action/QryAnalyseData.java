package com.mp2.analysestock.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import com.mp2.analysestock.db.model.*;
import com.mp2.analysestock.commons.MP2BaseActionSupport;
import com.mp2.analysestock.db.service.DAnalyseStockServiceI;

public class QryAnalyseData extends MP2BaseActionSupport {
    private final static Logger logger = LoggerFactory.getLogger(QryAnalyseData_v1.class);

    private InputStream inputStream;    //返回的数据流

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

    class ItemStyle {
        public String color = "red";
    }

    class ChanPoint {
        public short tp; // 0:顶分型 1:底分型
        public int arr_pos;
        public String trade_date;
        public double close_val;

        public String toString() {
            String re = "tp:" + tp + " trade_date:" + trade_date + " close_val:" + close_val;
            return re;
        }
    }

    class MarkLine {
        public String type = "";
        public String name = "";
    }

    class LineStyle {
        public String color = "blue";
        public String type = "solid";
    }

    class Label {
        public boolean show = false;
        public String position = "middle";
    }

    class PointCoord_base {
        public String symbol = "circle";
        public int symbolSize = 8;
        public ItemStyle itemStyle = new ItemStyle();
        public String[] coord = new String[2];
    }

    class PointCoord_t1 extends PointCoord_base {
        public double value = 0;
    }

    class LineCoord {
        public double value = 0;
        public Label label = new Label();
        public LineStyle lineStyle = new LineStyle();
        public String[] coord = new String[2];
    }

    class AreaCoord {
        public String[] coord = new String[2];
    }

    class EChartsResult_03 {
        public String[] legend_data;
        public HashMap<String, Boolean> legend_selected;
        public String[] xAxis_data;
        public double[][] series_data;
        public List<LineCoord>[] markline_data;
        public List<PointCoord_base> markpoint_data = new ArrayList<>();
        public List<AreaCoord>[] markarea_data;

        public EChartsResult_03(int xCount, int yCount) {
            legend_data = new String[yCount];
            legend_selected = new HashMap();
            xAxis_data = new String[xCount];
            series_data = new double[yCount][xCount];
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
            HashMap<String, Object> param = new HashMap<>();
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
            HashMap<String, Object> param = new HashMap<>();
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

    private int getChanDirection(double a1, double a2, double b1, double b2) {
        int re = 0; // -1 下跌 0 盘整 1 上升

        // System.out.println(a1 + " " + a2 + " " + b1 + " " + b2);

        if (b2 > b1) { // B 朝上
            if (a2 > a1) { // A 朝上， B 朝上
                if (b2 == a2) {
                    re = 0;
                } else if (b2 > a2) {
                    re = 1;
                } else {
                    if (b1 < a1) {
                        re = -1;
                    } else {
                        re = 0;
                    }
                }
            } else { // A 朝下， B 朝上
                if (b2 == a1) {
                    re = 0;
                } else if (b2 > a1) {
                    re = 1;
                } else {
                    if (b1 < a2) {
                        re = -1;
                    } else {
                        re = 0;
                    }
                }
            }
        } else { // B 朝下
            if (a2 < a1) { // A 朝下， B 朝下
                if (b2 == a2) {
                    re = 0;
                } else if (b2 > a2) {
                    if (b1 < a1) {
                        re = 0;
                    } else {
                        re = 1;
                    }
                } else {
                    re = -1;
                }
            } else { // A 朝上， B 朝下
                if (b2 == a1) {
                    re = 0;
                } else if (b2 > a1) {
                    if (b1 < a2) {
                        re = 0;
                    } else {
                        re = 1;
                    }
                } else {
                    re = -1;
                }
            }
        }

        return re;
    }

    private ChanPoint getChanMaxGG(ChanPoint a1, ChanPoint a2, ChanPoint b1, ChanPoint b2) {
        ChanPoint t;

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

        if (a1.close_val > a2.close_val) {
            if (a1.close_val > b1.close_val)
                return a1;
            else
                return b1;
        } else {
            if (a2.close_val > b2.close_val)
                return a2;
            else
                return b2;
        }
    }

    private ChanPoint getChanMaxDD(ChanPoint a1, ChanPoint a2, ChanPoint b1, ChanPoint b2) {
        ChanPoint t;

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

        if (a1.close_val < a2.close_val) {
            if (a1.close_val < b1.close_val)
                return a1;
            else
                return b1;
        } else {
            if (a2.close_val < b2.close_val)
                return a2;
            else
                return b2;
        }
    }

    private List<ChanPoint> analyseChanPoint(List<D2IndexData> data) {
        List<ChanPoint> re = new ArrayList<>();

        // 开头
        if (data.size() > 1) {
            D2IndexData item = data.get(0);
            D2IndexData e = data.get(1);

            if (item.getClose_val() > e.getClose_val()) {
                //判断顶分型
                ChanPoint t = new ChanPoint();
                t.tp = 0;
                t.arr_pos = 0;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getClose_val();
                re.add(t);
            } else {
                //判断底分型
                ChanPoint t = new ChanPoint();
                t.tp = 1;
                t.arr_pos = 0;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getClose_val();
                re.add(t);
            }
        }

        // 中间 找到所有顶底分型
        for (int x = 1; x < (data.size() - 1); x++) {
            D2IndexData item = data.get(x);
            D2IndexData b = data.get(x - 1);
            D2IndexData e = data.get(x + 1);

            if (item.getClose_val() > b.getClose_val() && item.getClose_val() > e.getClose_val()) {
                //判断顶分型
                ChanPoint t = new ChanPoint();
                t.tp = 0;
                t.arr_pos = x;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getClose_val();
                re.add(t);
            } else if (item.getClose_val() < b.getClose_val() && item.getClose_val() < e.getClose_val()) {
                //判断底分型
                ChanPoint t = new ChanPoint();
                t.tp = 1;
                t.arr_pos = x;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getClose_val();
                re.add(t);
            }
        }

        // 结尾
        if (data.size() > 1) {
            D2IndexData item = data.get(data.size() - 1);
            D2IndexData b = data.get(data.size() - 2);

            if (item.getClose_val() > b.getClose_val()) {
                //判断顶分型
                ChanPoint t = new ChanPoint();
                t.tp = 0;
                t.arr_pos = data.size() - 1;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getClose_val();
                re.add(t);
            } else if (item.getClose_val() < b.getClose_val()) {
                //判断底分型
                ChanPoint t = new ChanPoint();
                t.tp = 1;
                t.arr_pos = data.size() - 1;
                t.trade_date = item.getTrade_date();
                t.close_val = item.getClose_val();
                re.add(t);
            }
        }

        return re;
    }

    /**
     * 分析线段
     *
     * @param data
     * @return
     */
    private List<ChanPoint> analyseChanLine(List<ChanPoint> data) {
        List<ChanPoint> ok = new ArrayList<>();

        if (data.size() > 4) {
            ChanPoint beforePA1 = data.get(0);
            ChanPoint beforePA2 = data.get(1);
            ChanPoint beforePB1 = data.get(2);
            ChanPoint beforePB2 = data.get(3);
            ChanPoint nowPA1, nowPA2, nowPB1, nowPB2;
            ChanPoint dd, gg;
            int parentDirection, oldDirection, beforeDirection, nowDirection;
            beforeDirection = getChanDirection(beforePA1.close_val, beforePA2.close_val, beforePB1.close_val, beforePB2.close_val);
            oldDirection = beforeDirection;
            parentDirection = beforeDirection;
            gg = getChanMaxGG(beforePA1, beforePA2, beforePB1, beforePB2);
            dd = getChanMaxDD(beforePA1, beforePA2, beforePB1, beforePB2);
            for (int x = 4; x < (data.size() - 1); x += 2) {
                nowPA1 = data.get(x - 2);
                nowPA2 = data.get(x - 1);
                nowPB1 = data.get(x);
                nowPB2 = data.get(x + 1);
                nowDirection = getChanDirection(nowPA1.close_val, nowPA2.close_val, nowPB1.close_val, nowPB2.close_val);
                //System.out.println("gg_1:" + gg.close_val + " dd:" + dd.close_val);

                if (beforeDirection != nowDirection) {
                    //方向不一致
                    if (beforeDirection == 0 && nowDirection == 1) {
                        //盘整 -> 上升
                        switch (oldDirection) {
                            case 1:
                                //System.out.println("上升 -> 盘整 -> 上升");
                                break;
                            case 0:
                                if (parentDirection == 1) {
                                    //System.out.println("上升 -> 盘整... 盘整... -> 上升");
                                    ok.add(gg);
                                    //System.out.println("AddP:" + gg.close_val);
                                    gg = getChanMaxGG(nowPA1, nowPA2, nowPB1, nowPB2);
                                    dd = getChanMaxDD(nowPB1, nowPB2, nowPB1, nowPB2);
                                    ok.add(dd);
                                    //System.out.println("AddP:" + dd.close_val);
                                } else {
                                    //System.out.println("下跌 -> 盘整... 盘整... -> 上升");
                                    ok.add(dd);
                                    //System.out.println("AddP:" + dd.close_val);
                                    gg = getChanMaxGG(nowPA1, nowPA2, nowPB1, nowPB2);
                                    dd = getChanMaxDD(nowPA1, nowPA2, nowPB1, nowPB2);
                                }
                                break;
                            case -1:
                                //System.out.println("下跌 -> 盘整 -> 上升");
                                ok.add(dd);
                                //System.out.println("AddP:" + dd.close_val);
                                break;
                        }
                    } else if (beforeDirection == 0 && nowDirection == -1) {
                        //盘整 -> 下跌
                        switch (oldDirection) {
                            case 1:
                                //System.out.println("上升 -> 盘整 -> 下跌");
                                ok.add(gg);
                                //System.out.println("AddP:" + gg.close_val);
                                break;
                            case 0:
                                if (parentDirection == 1) {
                                    //System.out.println("上升 -> 盘整... 盘整... -> 下跌");
                                    ok.add(gg);
                                    //System.out.println("AddP:" + gg.close_val);
                                    gg = getChanMaxGG(nowPA1, nowPA2, nowPB1, nowPB2);
                                    dd = getChanMaxDD(nowPA1, nowPA2, nowPB1, nowPB2);
                                } else {
                                    //System.out.println("下跌 -> 盘整... 盘整... -> 下跌");
                                    ok.add(dd);
                                    //System.out.println("AddP:" + dd.close_val);
                                    gg = getChanMaxGG(nowPB1, nowPB2, nowPB1, nowPB2);
                                    dd = getChanMaxDD(nowPA1, nowPA2, nowPB1, nowPB2);
                                    ok.add(gg);
                                    //System.out.println("AddP:" + gg.close_val);
                                }
                                break;
                            case -1:
                                //System.out.println("下跌 -> 盘整 -> 下跌");
                                break;
                        }
                    } else if (beforeDirection == 1 && nowDirection == 0) {
                        //上升 -> 盘整
                        //System.out.println("上升 -> 盘整");
                        ok.add(dd);
                        //System.out.println("AddP:" + dd.close_val);
                        gg = getChanMaxGG(nowPA1, nowPA2, nowPB1, nowPB2);
                    } else if (beforeDirection == 1 && nowDirection == -1) {
                        //上升 -> 下跌
                        //System.out.println("上升 -> 下跌");
                        ok.add(dd);
                        //System.out.println("AddP:" + dd.close_val);
                        gg = getChanMaxGG(nowPA1, nowPA2, nowPB1, nowPB2);
                        dd = getChanMaxDD(nowPA1, nowPA2, nowPB1, nowPB2);
                        ok.add(gg);
                        //System.out.println("AddP:" + gg.close_val);
                    } else if (beforeDirection == -1 && nowDirection == 0) {
                        //下跌 -> 盘整
                        //System.out.println("下跌 -> 盘整");
                        ok.add(gg);
                        //System.out.println("AddP:" + gg.close_val);
                        dd = getChanMaxDD(nowPA1, nowPA2, nowPB1, nowPB2);
                    } else if (beforeDirection == -1 && nowDirection == 1) {
                        //下跌 -> 上升
                        //System.out.println("下跌 -> 上升");
                        ok.add(gg);
                        //System.out.println("AddP:" + gg.close_val);
                        gg = getChanMaxGG(nowPA1, nowPA2, nowPB1, nowPB2);
                        dd = getChanMaxDD(nowPA1, nowPA2, nowPB1, nowPB2);
                        ok.add(dd);
                        //System.out.println("AddP:" + dd.close_val);
                    }

                    if (nowDirection != 0) {
                        parentDirection = nowDirection;
                    }
                } else {
                    //方向一致
                    if (nowDirection == 0) {
                        //盘整
                        //System.out.println("盘整 -> 盘整");
                        gg = getChanMaxGG(gg, dd, nowPB1.close_val > nowPB2.close_val ? nowPB1 : nowPB2, nowPB1.close_val > nowPB2.close_val ? nowPB2 : nowPB1);
                        dd = getChanMaxDD(gg, dd, nowPB1.close_val > nowPB2.close_val ? nowPB1 : nowPB2, nowPB1.close_val > nowPB2.close_val ? nowPB2 : nowPB1);
                    } else if (nowDirection == -1) {
                        //System.out.println("下跌 -> 下跌");
                    } else if (nowDirection == 1) {
                        //System.out.println("上升 -> 上升");
                    }
                }
                //System.out.println("gg_2:" + gg.close_val + " dd:" + dd.close_val + "\n");

                oldDirection = beforeDirection;
                beforeDirection = nowDirection;
            }

            // 临时用于计算，加入最后一个点
            if (data != null && data.size() > 0) {
                ok.add(data.get(data.size() - 1));
            }
        }

        // 清理重复数据
        List<ChanPoint> cl = new ArrayList<>();
        ChanPoint temP = null;
        for (int n = 0; n < ok.size(); n++) {
            if (n == 0) {
                temP = new ChanPoint();
                temP.trade_date = ok.get(0).trade_date;
                temP.close_val = ok.get(0).close_val;
                temP.arr_pos = ok.get(0).arr_pos;
                temP.tp = ok.get(0).tp;
            } else if (n < (ok.size() - 1)) {
                if (temP != null && temP.trade_date != ok.get(n).trade_date) {
                    cl.add(temP);
                    //System.out.println("Add " + temP.trade_date);
                    temP = new ChanPoint();
                    temP.trade_date = ok.get(n).trade_date;
                    temP.close_val = ok.get(n).close_val;
                    temP.arr_pos = ok.get(n).arr_pos;
                    temP.tp = ok.get(n).tp;
                }
            } else if (n == (ok.size() - 1)) {
                temP = new ChanPoint();
                temP.trade_date = ok.get(n).trade_date;
                temP.close_val = ok.get(n).close_val;
                temP.arr_pos = ok.get(n).arr_pos;
                temP.tp = ok.get(n).tp;
                cl.add(temP);
                // System.out.println("Add " + temP.trade_date);
            }
        }

        // 过滤掉不符合一笔的数据(1.两个顶底之间小于3个，并且前后方向一致)
        List<ChanPoint> re = new ArrayList<>();
        if (cl.size() > 0) {
            re.add(cl.get(0));
        }
        int ratio = 2;
        if ((cl.size() / 2) == 1) {// 是否为"奇数"
            ratio = 3;
        }
        for (int n = 1; n < (cl.size() - ratio); n++) {
            ChanPoint mP1 = cl.get(n);
            ChanPoint mP2 = cl.get(n + 1);
            ChanPoint bP = cl.get(n - 1);
            ChanPoint eP = cl.get(n + 2);
            if (Math.abs(mP1.arr_pos - mP2.arr_pos) < 4
                    && Math.abs(mP2.arr_pos - eP.arr_pos) >= 4
                    && Math.abs(mP1.arr_pos - bP.arr_pos) >= 4) {
                if ((bP.close_val < mP1.close_val && bP.close_val < mP2.close_val
                        && eP.close_val > mP1.close_val && eP.close_val > mP2.close_val)
                        || (bP.close_val > mP1.close_val && bP.close_val > mP2.close_val
                        && eP.close_val < mP1.close_val && eP.close_val < mP2.close_val)) {
                    // 符合简化过滤条件：两个顶底之间小于3个，并且前后方向一致
                    n++;
                } else {
                    re.add(cl.get(n));
                }
            } else {
                re.add(cl.get(n));
            }
        }
        for (int n = (cl.size() - ratio); n < cl.size(); n++) {
            re.add(cl.get(n));
        }

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
    }

    private Point coord2Point(String[] coord) {
        Point re = new Point();

        re.trade_date = coord[0];
        re.close_val = Double.parseDouble(coord[1]);

        return re;
    }

    /**
     * 线段 分析 区域
     *
     * @param arrLine
     * @return
     */
    private List<AreaCoord> analyseChanArea(List<LineCoord>[] arrLine) {
        int continueCount = 0;
        Point t, a1, a2, b1, b2, c1, c2, d1, d2, zd, zg;
        List<AreaCoord> re = new ArrayList<>();

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

            if ((a1.close_val == b1.close_val && b2.close_val == c2.close_val && c1.close_val == d1.close_val && a1.close_val < d2.close_val && a2.close_val > d1.close_val)
                    || (a2.close_val == b2.close_val && b1.close_val == c1.close_val && c2.close_val == d2.close_val && a2.close_val > d1.close_val && a1.close_val < d2.close_val)) {
                // 低点,高点,低点 或者 高点,低点,高点 并且有重合
                zd = a1.close_val > b1.close_val ? a1 : b1;
                zg = a2.close_val > b2.close_val ? b2 : a2;
                zd = zd.close_val > c1.close_val ? zd : c1;
                zg = zg.close_val > c2.close_val ? c2 : zg;
                zd = zd.close_val > d1.close_val ? zd : d1;
                zg = zg.close_val > d2.close_val ? d2 : zg;
                continueCount++;

                if (continueCount == 1) {
                    if (Integer.parseInt(zd.trade_date) > Integer.parseInt(zg.trade_date)) {
                        AreaCoord area1 = new AreaCoord();
                        area1.coord[0] = zg.trade_date;
                        area1.coord[1] = zg.close_val + "";
                        re.add(area1);
                        AreaCoord area2 = new AreaCoord();
                        area2.coord[0] = zd.trade_date;
                        area2.coord[1] = zd.close_val + "";
                        re.add(area2);
                    } else {
                        AreaCoord area1 = new AreaCoord();
                        area1.coord[0] = zd.trade_date;
                        area1.coord[1] = zd.close_val + "";
                        re.add(area1);
                        AreaCoord area2 = new AreaCoord();
                        area2.coord[0] = zg.trade_date;
                        area2.coord[1] = zg.close_val + "";
                        re.add(area2);
                    }
                }
            } else {
                continueCount = 0;
            }

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
        List<D2IndexData> index_data, index_mindate_year;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            HashMap<String, Object> param = new HashMap<>();

            param.put("ts_code", ts_code);
            index_data = dAnalyseStockService.select2IndexData(param);
            index_mindate_year = dAnalyseStockService.select2IndexMinDateByYear(param);

            String[] legend = {ts_name, "EMA:5", "EMA:20", "EMA:250", "BIAS:5", "BIAS:20", "MACD"};
            EChartsResult_03 eChartsResult = new EChartsResult_03(index_data.size(), legend.length);

            eChartsResult.legend_data = legend.clone();
            eChartsResult.legend_selected.clear();
            eChartsResult.legend_selected.put(ts_name, true);
            eChartsResult.legend_selected.put("EMA:5", false);
            eChartsResult.legend_selected.put("EMA:20", false);
            eChartsResult.legend_selected.put("EMA:250", false);
            eChartsResult.legend_selected.put("BIAS:5", false);
            eChartsResult.legend_selected.put("BIAS:20", false);
            eChartsResult.legend_selected.put("MACD", false);

            // 填充指数数据
            for (int x = 0; x < index_data.size(); x++) {
                D2IndexData item_000852 = index_data.get(x);

                eChartsResult.xAxis_data[x] = item_000852.getTrade_date();
                eChartsResult.series_data[0][x] = item_000852.getClose_val();
            }

            // 计算均线、BIAS、MACD  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            Core taLibCore = new Core();
            MInteger taLibBegin = new MInteger();
            MInteger taLibLength = new MInteger();

            int cha;
            double[] taLibOut = new double[eChartsResult.series_data[0].length];
            taLibCore.ema(0, eChartsResult.series_data[0].length - 1, eChartsResult.series_data[0], 5, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data[1].length - taLibLength.value;
                eChartsResult.series_data[1][x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
                // BIAS
                eChartsResult.series_data[4][x + cha] = bias(eChartsResult.series_data[1][x + cha], eChartsResult.series_data[0][x + cha]);
            }
            taLibCore.ema(0, eChartsResult.series_data[0].length - 1, eChartsResult.series_data[0], 20, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data[2].length - taLibLength.value;
                eChartsResult.series_data[2][x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
                // BIAS
                eChartsResult.series_data[5][x + cha] = bias(eChartsResult.series_data[2][x + cha], eChartsResult.series_data[0][x + cha]);
            }
            taLibCore.ema(0, eChartsResult.series_data[0].length - 1, eChartsResult.series_data[0], 250, taLibBegin, taLibLength, taLibOut);
            for (int x = 0; x < taLibLength.value; x++) {
                cha = eChartsResult.series_data[3].length - taLibLength.value;
                eChartsResult.series_data[3][x + cha] = (double) Math.round(taLibOut[x] * 100) / 100;
            }
            double[] taLibOutMACD = new double[eChartsResult.series_data[0].length];
            double[] taLibOutMACDSignal = new double[eChartsResult.series_data[0].length];
            double[] taLibOutMACDHist = new double[eChartsResult.series_data[0].length];
            taLibCore.macd(0, eChartsResult.series_data[0].length - 1, eChartsResult.series_data[0], 12, 26, 9, taLibBegin, taLibLength, taLibOutMACD, taLibOutMACDSignal, taLibOutMACDHist);
            for (int x = 0; x < taLibLength.value; x++) {
                eChartsResult.series_data[6][x + (eChartsResult.series_data[6].length - taLibLength.value)] = (double) Math.round(taLibOutMACDHist[x] * 100) / 100;
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
                if ((eChartsResult.series_data[4][x] + eChartsResult.series_data[5][x]) <= bias5_20_down) {
                    PointCoord_base p = new PointCoord_base();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data[0][x] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#660000"; // 红
                    eChartsResult.markpoint_data.add(p);
                } else if (eChartsResult.series_data[4][x] <= bias5_down && eChartsResult.series_data[5][x] < bias20_down) {
                    PointCoord_base p = new PointCoord_base();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data[0][x] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#FFCC66"; // 橙
                    eChartsResult.markpoint_data.add(p);
                } else if ((eChartsResult.series_data[4][x] + eChartsResult.series_data[5][x]) >= bias5_20_up) {
                    PointCoord_base p = new PointCoord_base();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data[0][x] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#009966"; // 绿
                    eChartsResult.markpoint_data.add(p);
                } else if (eChartsResult.series_data[5][x] >= bias20_up) {
                    PointCoord_base p = new PointCoord_base();
                    p.coord[0] = eChartsResult.xAxis_data[x];
                    p.coord[1] = eChartsResult.series_data[0][x] + "";
                    p.symbol = "arrow";
                    p.itemStyle.color = "#CCFF66"; // 浅绿
                    eChartsResult.markpoint_data.add(p);
                }
            }

            // 标记每年开始的第一个交易日 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            for (int x = 0; x < index_mindate_year.size(); x++) {
                D2IndexData item = index_mindate_year.get(x);
                PointCoord_t1 p = new PointCoord_t1();
                p.coord[0] = item.getTrade_date();
                p.coord[1] = item.getClose_val() + "";
                p.symbol = "pin";
                p.symbolSize = 20;
                p.value = Double.parseDouble(item.getTrade_date().substring(0, 4));
                p.itemStyle.color = "red"; // 红
                eChartsResult.markpoint_data.add(p);
            }

            // 处理线段 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            List<ChanPoint> listChanPoint = analyseChanPoint(index_data);
            List<ChanPoint> listChanLine_1 = analyseChanLine(listChanPoint);
            // List<ChanPoint> listChanLine_2 = analyseChanLine(listChanLine_1);
            index_data.clear();
            listChanPoint.clear();
            /*
            for (int x = (listChanPoint.size() - 8); x < listChanPoint.size(); x++) {
                ChanPoint item = listChanPoint.get(x);
                PointCoord p = new PointCoord();
                //p.value = (double) Math.round(item.close_val * 100) / 100;
                p.coord[0] = item.trade_date;
                p.coord[1] = item.close_val + "";
                eChartsResult.markpoint_data.add(p);
            }*/

            int index = 0;
            double sum_val;
            // eChartsResult.markline_data = new List[listChanLine_1.size() - 1 + listChanLine_2.size() - 1];
            eChartsResult.markline_data = new List[listChanLine_1.size() - 1];
            // System.out.println("listChanLine.size()=" + listChanLine.size());
            // 一级线段
            for (int x = 1; x < listChanLine_1.size(); x++) {
                ChanPoint itemEnd = listChanLine_1.get(x);
                ChanPoint itemFrom = listChanLine_1.get(x - 1);
                // System.out.println("L1: " + itemFrom.trade_date + " => " + itemEnd.trade_date);
                List<LineCoord> listP = new ArrayList<>();
                LineCoord p1 = new LineCoord();
                LineCoord p2 = new LineCoord();
                p1.coord[0] = itemFrom.trade_date;
                p1.coord[1] = itemFrom.close_val + "";
                p1.lineStyle.color = "#9999ff";
                p2.coord[0] = itemEnd.trade_date;
                p2.coord[1] = itemEnd.close_val + "";
                p2.lineStyle.color = "#9999ff";
                sum_val = 0;
                for (int t = itemFrom.arr_pos; t < itemEnd.arr_pos; t++) {
                    sum_val += eChartsResult.series_data[6][t];
                }
                p1.value = (double) Math.round(sum_val * 100) / 100;
                p2.value = p1.value;
                if (x >= listChanLine_1.size() - 6) {
                    p1.label.show = true;
                    p2.label.show = true;
                }

                listP.add(p1);
                listP.add(p2);
                eChartsResult.markline_data[index++] = listP;
            }
            listChanLine_1.clear();
            // 二级线段
            /*
            for (int x = 1; x < listChanLine_2.size(); x++) {
                ChanPoint itemEnd = listChanLine_2.get(x);
                ChanPoint itemFrom = listChanLine_2.get(x - 1);
                // System.out.println("L2: " + itemFrom.trade_date + " => " + itemEnd.trade_date);
                List<LineCoord> listP = new ArrayList<>();
                LineCoord p1 = new LineCoord();
                LineCoord p2 = new LineCoord();
                p1.coord[0] = itemFrom.trade_date;
                p1.coord[1] = itemFrom.close_val + "";
                p1.lineStyle.color = "#ffcc33";
                p2.coord[0] = itemEnd.trade_date;
                p2.coord[1] = itemEnd.close_val + "";
                p2.lineStyle.color = "#ffcc33";
                sum_val = 0;
                for (int t = itemFrom.arr_pos; t < itemEnd.arr_pos; t++) {
                    sum_val += eChartsResult.series_data[6][t];
                }
                p1.value = (double) Math.round(sum_val * 100) / 100;
                p2.value = p1.value;

                listP.add(p1);
                listP.add(p2);
                eChartsResult.markline_data[index++] = listP;
            }
            listChanLine_2.clear();
            */

            // 处理区域 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            index = 0;
            List<AreaCoord> listAreaCoord = analyseChanArea(eChartsResult.markline_data);
            System.out.println("listAreaCoord.size(): " + listAreaCoord.size());
            eChartsResult.markarea_data = new List[listAreaCoord.size() / 2];
            for (int x = 0; x < listAreaCoord.size() - 1; x += 2) {
                List<AreaCoord> listP = new ArrayList<>();
                AreaCoord p1 = listAreaCoord.get(x);
                AreaCoord p2 = listAreaCoord.get(x + 1);
                listP.add(p1);
                listP.add(p2);
                eChartsResult.markarea_data[index++] = listP;
            }

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

    public String getIndexesTech_000852SH() {
        getIndexesTech("000852.SH", "中证1000");
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
}
