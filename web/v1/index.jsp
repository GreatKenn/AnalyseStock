<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <% // 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！%>
    <meta name="author" content="MP2:huangkai">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Contral" content="no-cache, must-revalidate">
    <meta http-equiv="expires" content="0">
    <title>胸毛兄的股票分析系统:新股上市量</title>

    <link rel="stylesheet" href="/../src/main/webapp/css/bootstrap.min.css">
    <link rel="stylesheet" href="/../src/main/webapp/css/font-awesome.min.css">
    <link rel="stylesheet" href="/../src/main/webapp/css/ionicons.min.css">
    <link rel="stylesheet" href="/../src/main/webapp/css/patch.css">
    <link rel="stylesheet" href="/../src/main/webapp/css/docs.min.css">

</head>

<body>
<div id="newstock_count_div" style="width: 1600px; height: 500px;"></div>
<br>
<br>


<!--[if lt IE 9]>
<script language="javascript" type="text/javascript" src="/js/jquery-1.12.0.min.js"></script>
<script language="javascript" type="text/javascript" src="/js/html5shiv.js"></script>
<![endif]-->
<!--[if gte IE 9]><!-->
<script language="javascript" type="text/javascript" src="/js/jquery-2.2.0.min.js"></script>
<!--<![endif]-->
<script language="javascript" type="text/javascript" src="/js/bootstrap.min.js"></script>
<script language="javascript" type="text/javascript" src="/js/plugins/jquery.slimscroll.min.js"></script>
<script language="javascript" type="text/javascript" src="/js/fastclick.min.js"></script>

<script language="javascript" type="text/javascript" src="/js/echarts/echarts.min.js"></script>

<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('newstock_count_div'));

    myChart.showLoading();

    $.ajax({
        type: "POST",
        async: false,
        url: encodeURI("qryAnalyseNewStockCount.action"),
        dataType: "json",
        success: function (stock_data) {
            myChart.hideLoading();

            option = {
                title: {
                    text: '按周统计新股上市量'
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow',
                        label: {
                            show: true
                        }
                    }
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: false, readOnly: true},
                        magicType: {show: true, type: ['stack', 'tiled']},
                        restore : {show: false},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                legend: {
                    data:['上证', '深证', '创业'],
                    itemGap: 5
                },
                grid: {
                    top: '12%',
                    left: '1%',
                    right: '5%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type : 'category',
                        data : stock_data.names
                    }
                ],
                yAxis: [
                    {
                        type : 'value',
                        name : '数量'
                    }
                ],
                dataZoom: [
                    {
                        show: true,
                        start: 94,
                        end: 100
                    },
                    {
                        type: 'inside',
                        start: 94,
                        end: 100
                    },
                    {
                        show: true,
                        yAxisIndex: 0,
                        filterMode: 'empty',
                        width: 30,
                        height: '80%',
                        showDataShadow: false,
                        left: '98%'
                    }
                ],
                series : [
                    {
                        name: '上证',
                        type: 'bar',
                        data: stock_data.count_6
                    },
                    {
                        name: '深证',
                        type: 'bar',
                        data: stock_data.count_0
                    },
                    {
                        name: '创业',
                        type: 'bar',
                        data: stock_data.count_3
                    }
                ]
            };

            myChart.setOption(option);
        }
    });

</script>

</body>
</html>