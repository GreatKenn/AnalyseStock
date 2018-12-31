package com.mp2.analysestock.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mp2.analysestock.db.model.*;
import com.mp2.analysestock.db.service.DAnalyseStockServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp2.analysestock.commons.MP2BaseActionSupport;

public class QryAnalyseData_v1 extends MP2BaseActionSupport {
    private final static Logger logger = LoggerFactory.getLogger(QryAnalyseData_v1.class);

    private InputStream inputStream;    //返回的数据流

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * 获取指数数据信息
     *
     * @return
     */
    public String getIndexData_sh() {
        int sh_base_vol = 2200000;
        int lup1, lup2, ldw1, ldw2;
        double sh_rate, sh_vol;
        String line;
        StringBuffer str = new StringBuffer("日期,上证指数,成交量,涨幅2%,涨幅过3%,跌幅2%,跌幅过3%");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DIndexData> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectIndexData(mapQryKey);
                //dataList = null;

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DIndexData item : dataList) {
                    sh_rate = (float) (item.getSh_volume() - sh_base_vol) / (float) sh_base_vol;
                    sh_vol = 4000 + 10 * sh_rate / 2;

                    lup1 = 1;
                    lup2 = 1;
                    ldw1 = 1;
                    ldw2 = 1;
                    if (item.getSh_change() >= 0.02 && item.getSh_change() < 0.03) {
                        lup1 = 800;
                    }
                    if (item.getSh_change() >= 0.03) {
                        lup2 = (int) (400 * item.getSh_change() * 100);
                    }
                    if (item.getSh_change() <= -0.02 && item.getSh_change() > -0.03) {
                        ldw1 = 800;
                    }
                    if (item.getSh_change() <= -0.03) {
                        ldw2 = (int) (400 * (-item.getSh_change() * 100));
                    }

                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSh_close() + ","
                            + sh_vol + ","
                            + lup1 + ","
                            + lup2 + ","
                            + ldw1 + ","
                            + ldw2;
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    public String getIndexData_sz() {
        int sz_base_vol = 1200000;
        int lup1, lup2, ldw1, ldw2;
        double sz_rate, sz_vol;
        String line;
        StringBuffer str = new StringBuffer("日期,深证指数,成交量,涨幅2%,涨幅过3%,跌幅2%,跌幅过3%");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DIndexData> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectIndexData(mapQryKey);
                //dataList = null;

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DIndexData item : dataList) {
                    sz_rate = (float) (item.getSz_volume() - sz_base_vol) / (float) sz_base_vol;
                    sz_vol = 10000 + 20 * sz_rate;

                    lup1 = 1;
                    lup2 = 1;
                    ldw1 = 1;
                    ldw2 = 1;
                    if (item.getSz_change() >= 0.02 && item.getSz_change() < 0.03) {
                        lup1 = 2000;
                    }
                    if (item.getSz_change() >= 0.03) {
                        lup2 = (int) (1000 * item.getSz_change() * 100);
                    }
                    if (item.getSz_change() <= -0.02 && item.getSz_change() > -0.03) {
                        ldw1 = 2000;
                    }
                    if (item.getSz_change() <= -0.03) {
                        ldw2 = (int) (1000 * (-item.getSz_change() * 100));
                    }

                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSz_close() + ","
                            + sz_vol + ","
                            + lup1 + ","
                            + lup2 + ","
                            + ldw1 + ","
                            + ldw2;
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    public String getIndexData_399006() {
        int cy_base_vol = 1300000;
        int lup1, lup2, ldw1, ldw2;
        double cy_rate, cy_vol;
        String line;
        StringBuffer str = new StringBuffer("日期,创业板指数,成交量,涨幅2%,涨幅过3%,跌幅2%,跌幅过3%");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DBaseData> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectIndex399006(mapQryKey);
                //dataList = null;

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DBaseData item : dataList) {
                    cy_rate = (float) (item.getVolume_val() - cy_base_vol) / (float) cy_base_vol;
                    cy_vol = 1500 + 35 * cy_rate;

                    lup1 = 1;
                    lup2 = 1;
                    ldw1 = 1;
                    ldw2 = 1;
                    if (item.getChange_val() >= 0.02 && item.getChange_val() < 0.03) {
                        lup1 = 200;
                    }
                    if (item.getChange_val() >= 0.03) {
                        lup2 = (int) (100 * item.getChange_val() * 100);
                    }
                    if (item.getChange_val() <= -0.02 && item.getChange_val() > -0.03) {
                        ldw1 = 200;
                    }
                    if (item.getChange_val() <= -0.03) {
                        ldw2 = (int) (100 * (-item.getChange_val() * 100));
                    }

                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getClose_val() + ","
                            + cy_vol + ","
                            + lup1 + ","
                            + lup2 + ","
                            + ldw1 + ","
                            + ldw2;
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    public String getEventData() {
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();
        List<DEventData> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            dataList = dAnalyseStockService.selectEventData();
            //dataList = null;

            try {
                objMapper.writeValue(witStr, dataList);
                logger.info("toJSONString<" + witStr + ">");
            } catch (JsonGenerationException e) {
                logger.error(e.getLocalizedMessage());
            } catch (JsonMappingException e) {
                logger.error(e.getLocalizedMessage());
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(witStr.toString().getBytes("utf-8"));
            dataList.clear();

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     *
     */
    public String getLimit10p() {
        String line;
        StringBuffer str = new StringBuffer("日期,涨停率,一字涨停率,涨停次日上涨率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSh_lup10_rate() + ","
                            + item.getSh_lup10nc_rate() + ","
                            + item.getSh_lup10ag_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 涨跌停率(上海)
     */
    public String getLup10Rate_sh() {
        String line;
        StringBuffer str = new StringBuffer("日期,上证指数,涨停率,跌停率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSh_index_000001() + ","
                            + item.getSh_lup10_rate() + ","
                            + item.getSh_ldw10_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 一字板率(上海)
     */
    public String getLup10ncRate_sh() {
        String line;
        StringBuffer str = new StringBuffer("日期,上证指数,涨停一字板率,跌停一字板率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSh_index_000001() + ","
                            + item.getSh_lup10nc_rate() + ","
                            + item.getSh_ldw10nc_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 前日涨停次日上涨率(上海)
     */
    public String getLup10agRate_sh() {
        String line;
        StringBuffer str = new StringBuffer("日期,上证指数,前日涨停次日上涨率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSh_index_000001() + ","
                            + item.getSh_lup10ag_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 连续涨停次数值(上海)
     */
    public String getLup10c5Value_sh() {
        String line;
        StringBuffer str = new StringBuffer("日期,上证指数,连续涨停次数值,连续跌停次数值");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSh_index_000001() + ","
                            + item.getSh_lup10c5_value() + ","
                            + item.getSh_ldw10c5_value();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 非涨跌停变化率超过10的率(上海)
     */
    public String getLud10Rate_sh() {
        String line;
        StringBuffer str = new StringBuffer("日期,上证指数,非涨跌停变化率超过10的率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSh_index_000001() + ","
                            + item.getSh_lud10_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 涨跌停率(深圳)
     */
    public String getLup10Rate_sz() {
        String line;
        StringBuffer str = new StringBuffer("日期,深证指数,涨停率,跌停率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSz_index_399001() + ","
                            + item.getSz_lup10_rate() + ","
                            + item.getSz_ldw10_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 一字板率(深圳)
     */
    public String getLup10ncRate_sz() {
        String line;
        StringBuffer str = new StringBuffer("日期,深证指数,涨停一字板率,跌停一字板率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSz_index_399001() + ","
                            + item.getSz_lup10nc_rate() + ","
                            + item.getSz_ldw10nc_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 前日涨停次日上涨率(深圳)
     */
    public String getLup10agRate_sz() {
        String line;
        StringBuffer str = new StringBuffer("日期,深证指数,前日涨停次日上涨率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSz_index_399001() + ","
                            + item.getSz_lup10ag_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 连续涨停次数值(深圳)
     */
    public String getLup10c5Value_sz() {
        String line;
        StringBuffer str = new StringBuffer("日期,深证指数,连续涨停次数值,连续跌停次数值");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSz_index_399001() + ","
                            + item.getSz_lup10c5_value() + ","
                            + item.getSz_ldw10c5_value();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 非涨跌停变化率超过10的率(深圳)
     */
    public String getLud10Rate_sz() {
        String line;
        StringBuffer str = new StringBuffer("日期,深证指数,非涨跌停变化率超过10的率");
        int pageNo;
        HashMap<String, Object> mapQryKey = new HashMap<>();
        List<DAnalyseStockLimit10> dataList;
        DAnalyseStockServiceI dAnalyseStockService;

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {

            pageNo = 1;
            while (true) {
                mapQryKey.clear();

                mapQryKey.put("pageSize", new Integer(200));
                mapQryKey.put("pageStartPos", new Integer((pageNo - 1) * 200));

                dataList = dAnalyseStockService.selectAllData(mapQryKey);

                //数据为空,没有新页
                if (dataList == null || dataList.size() == 0) {
                    break;
                }

                for (DAnalyseStockLimit10 item : dataList) {
                    line = "\n" + item.getYyyymmdd() + ","
                            + item.getSz_index_399001() + ","
                            + item.getSz_lud10_rate();
                    str.append(line);
                }

                pageNo++;
                dataList.clear();
            }

            //System.out.println(str);
            inputStream = new ByteArrayInputStream(str.toString().getBytes("utf-8"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * 获取每周上市新股数量
     *
     * @return
     */
    public String getNewStockCountByYearWeekType() {
        String name;
        ObjectMapper objMapper = new ObjectMapper();
        StringWriter witStr = new StringWriter();
        List<DYearWeekCount> dataListDefault;
        List<DYearWeekCountByType> dataListNewStock;
        DAnalyseStockServiceI dAnalyseStockService;
        class ReturnData {
            public int count_6[];
            public int count_0[];
            public int count_3[];
            public String names[];
        }
        ReturnData reData = new ReturnData();

        dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");

        try {
            dataListDefault = dAnalyseStockService.selectNewStockCountByYearWeek();
            dataListNewStock = dAnalyseStockService.selectNewStockCountByYearWeekType();

            reData.count_6 = new int[dataListDefault.size()];
            reData.count_0 = new int[dataListDefault.size()];
            reData.count_3 = new int[dataListDefault.size()];
            reData.names = new String[dataListDefault.size()];

            for (int n = 0; n < dataListDefault.size(); n++) {
                reData.names[n] = dataListDefault.get(n).getYear() + "年" + dataListDefault.get(n).getWeek() + "周";
                reData.count_6[n] = 0;
                reData.count_0[n] = 0;
                reData.count_3[n] = 0;
            }

            for (DYearWeekCountByType item : dataListNewStock) {
                name = item.getYear() + "年" + item.getWeek() + "周";
                for (int n = 0; n < reData.names.length; n++) {
                    if (name.equals(reData.names[n])) {
                        if (item.getType().equals("上证"))
                            reData.count_6[n] = item.getCount();
                        else if (item.getType().equals("深证"))
                            reData.count_0[n] = item.getCount();
                        else if (item.getType().equals("创业"))
                            reData.count_3[n] = item.getCount();
                        break;
                    }
                }
            }

            try {
                objMapper.writeValue(witStr, reData);
                logger.info("toJSONString<" + witStr + ">");
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
}
