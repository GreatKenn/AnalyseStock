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
    <title>胸毛兄的股票分析系统:资金</title>

    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/ionicons.min.css">
    <link rel="stylesheet" href="/css/patch.css">
    <link rel="stylesheet" href="/css/docs.min.css">

</head>

<body>

<div id="div_indexes" style="width: 1600px; height: 500px;"></div>
<div id="div_margin" style="width: 1600px; height: 500px;"></div>

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

    $(document).ready(function () {
        // 基于准备好的dom，初始化echarts实例
        var myChart_indexes = echarts.init(document.getElementById('div_indexes'));
        var myChart_margin = echarts.init(document.getElementById('div_margin'));

        myChart_indexes.showLoading();
        myChart_margin.showLoading();

        $.ajax({
            type: "POST",
            async: false,
            url: encodeURI("qryIndexes.action"),
            dataType: "json",
            success: function (svr_data) {
                myChart_indexes.hideLoading();

                var option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: svr_data.legend_data,
                        selected: svr_data.legend_selected
                    },
                    grid: {
                        left: '3%',
                        right: '10%',
                        bottom: '3%',
                        containLabel: true
                    },
                    dataZoom: [
                        {
                            type: 'slider',
                            start: 99,
                            end: 100
                        }
                    ],
                    xAxis: [
                        {
                            type: 'category',
                            data: svr_data.xAxis_data
                        }
                    ],
                    yAxis: [
                        {
                            name: '指数',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        }
                    ],
                    series: [
                        {
                            name: '上证综指',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[0]
                        },
                        {
                            name: '深证成指',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[1]
                        },
                        {
                            name: '中证1000',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[2]
                        },
                        {
                            name: '创业板指',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[3]
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart_indexes.setOption(option);
            }
        });

        $.ajax({
            type: "POST",
            async: false,
            url: encodeURI("qryEveryDayMargin.action"),
            dataType: "json",
            success: function (svr_data) {
                myChart_margin.hideLoading();

                var option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: svr_data.legend_data,
                        selected: svr_data.legend_selected
                    },
                    grid: {
                        left: '3%',
                        right: '10%',
                        bottom: '3%',
                        containLabel: true
                    },
                    dataZoom: [
                        {
                            type: 'slider',
                            start: 99,
                            end: 100
                        }
                    ],
                    xAxis: [
                        {
                            type: 'category',
                            data: svr_data.xAxis_data
                        }
                    ],
                    yAxis: [
                        {
                            name: '两融',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        },
                        {
                            name: '互通',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        }
                    ],
                    series: [
                        {
                            name: '上证融资',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[0]
                        },
                        {
                            name: '深证融资',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[1]
                        },
                        {
                            name: '上证融券',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[2]
                        },
                        {
                            name: '深证融券',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[3]
                        },
                        {
                            name: '融资余额',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[4]
                        },
                        {
                            name: '北上资金',
                            type: 'line',
                            yAxisIndex: 1,
                            data: svr_data.series_data[5]
                        },
                        {
                            name: '南下资金',
                            type: 'line',
                            yAxisIndex: 1,
                            data: svr_data.series_data[6]
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart_margin.setOption(option);
            }
        });

        echarts.connect([myChart_indexes, myChart_margin]);

    });
</script>

</body>
</html>