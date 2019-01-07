package com.mp2.analysestock.action;

import java.util.HashMap;
import java.util.List;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import com.mp2.analysestock.commons.MP2BaseActionSupport;
import com.mp2.analysestock.db.service.DAnalyseStockServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp2.analysestock.db.model.SelectItem;
import com.mp2.analysestock.db.model.SelectResult;

/**
 * Created by kenn on 16/5/9.
 * 用于select2查询的功能模块
 */
public class QrySelectResultAction extends MP2BaseActionSupport {
    private final static Logger logger = LoggerFactory.getLogger(QrySelectResultAction.class);

    private InputStream inputStream;    //返回的数据流
    private String qryKey;
    private String busiName;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setQryKey(String qryKey) {
        this.qryKey = qryKey;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    /**
     * 根据关键字，列出期货商品
     */
    public void getFutList() {
        String jsonStr;
        List<SelectItem> listData;
        HashMap<String, String> param = new HashMap<String, String>();
        SelectResult selectResult = new SelectResult();
        DAnalyseStockServiceI dAnalyseStockService;

        try {
            dAnalyseStockService = (DAnalyseStockServiceI) acMyBatis.getBean("dAnalyseStockService");
            param.put("key", qryKey);
            listData = dAnalyseStockService.selectFutInfoByKey(param);

            int y = 0;
            SelectItem[] data = new SelectItem[listData.size()];
            for (SelectItem item : listData) {
                data[y] = new SelectItem();
                data[y].setId(item.getId());
                data[y].setText(item.getText());
                y++;
            }

            selectResult.setTotal_count(listData.size());
            selectResult.setItems(data);
            jsonStr = selectResult.toJSONString();
            inputStream = new ByteArrayInputStream(jsonStr.getBytes("utf-8"));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * 返回ID, Text结构的结果集
     *
     * @return
     */
    public String getIDTextResult() {

        logger.info("Call: getIDTextResult()");
        logger.debug("Param:qryKey[" + qryKey + "] busiName[" + busiName + "]");

        switch (busiName) {
            case "futList":
                getFutList();
                break;
            default:
                logger.error("没有找到匹配的业务关键字(" + busiName + ")!");
        }

        return SUCCESS;
    }

}
