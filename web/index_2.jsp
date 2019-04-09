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
    <title>胸毛兄的股票分析系统:价值</title>

    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/ionicons.min.css">
    <link rel="stylesheet" href="/css/patch.css">
    <link rel="stylesheet" href="/css/docs.min.css">

</head>

<body>

<div id="div_total" style="width: 1800px; height: 800px;"></div>

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
        var myChart_total = echarts.init(document.getElementById('div_total'));

        myChart_total.showLoading();

        $.ajax({
            type: "POST",
            async: false,
            url: encodeURI("qryIndexesDailyBasic_000001SH.action"),
            dataType: "json",
            success: function (svr_data) {
                myChart_total.hideLoading();

                var option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
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
                            name: '市值',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        },
                        {
                            name: '股本',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        },
                        {
                            name: '换手率',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        },
                        {
                            name: '市盈率',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        },
                        {
                            name: '市净率',
                            position: 'left',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        }
                    ],
                    /* "总市值(亿元)", "流通市值(亿元)", "总股本(亿股)", "流通股本", "自由流通股本", "换手率", "换手率(基于自由流通股本)", "市盈率", "市盈率TTM", "市净率" */
                    series: [
                        {
                            name: '总市值(亿元)',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[0]
                        },
                        {
                            name: '流通市值(亿元)',
                            type: 'line',
                            yAxisIndex: 0,
                            data: svr_data.series_data[1]
                        },
                        {
                            name: '总股本(亿股)',
                            type: 'line',
                            yAxisIndex: 1,
                            data: svr_data.series_data[2]
                        },
                        {
                            name: '流通股本',
                            type: 'line',
                            yAxisIndex: 1,
                            data: svr_data.series_data[3]
                        },
                        {
                            name: '自由流通股本',
                            type: 'line',
                            yAxisIndex: 1,
                            data: svr_data.series_data[4]
                        },
                        {
                            name: '换手率',
                            type: 'line',
                            yAxisIndex: 2,
                            data: svr_data.series_data[5]
                        },
                        {
                            name: '换手率(基于自由流通股本)',
                            type: 'line',
                            yAxisIndex: 2,
                            data: svr_data.series_data[6]
                        },
                        {
                            name: '市盈率',
                            type: 'line',
                            yAxisIndex: 3,
                            data: svr_data.series_data[7]
                        },
                        {
                            name: '市盈率TTM',
                            type: 'line',
                            yAxisIndex: 3,
                            data: svr_data.series_data[8]
                        },
                        {
                            name: '市净率',
                            type: 'line',
                            yAxisIndex: 4,
                            data: svr_data.series_data[9]
                        }

                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart_total.setOption(option);
            }
        });

    });
</script>

</body>
</html>