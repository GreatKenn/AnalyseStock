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
    <title>胸毛兄的股票分析系统:期货</title>

    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/ionicons.min.css">
    <link rel="stylesheet" href="/css/patch.css">
    <link rel="stylesheet" href="/css/docs.min.css">
    <link rel="stylesheet" href="/css/plugins/jquery.toastr/toastr.min.css">

    <link rel="stylesheet" href="/css/select2.min.css">
    <link rel="stylesheet" href="/css/select2-bootstrap.min.css">

</head>

<body>

<div>
    <select id="ts_item" name="ts_item">
    </select>
</div>

<div id="div_fut" style="width: 1600px; height: 500px;"></div>

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
<script language="javascript" type="text/javascript" src="/js/toastr.min.js"></script>
<script language="javascript" type="text/javascript" src="/js/echarts/echarts.min.js"></script>

<script language="javascript" type="text/javascript" src="/js/select2/select2.min.js"></script>
<script language="javascript" type="text/javascript" src="/js/select2/i18n/zh-CN.js"></script>
<script language="javascript" type="text/javascript" src="/js/select2/select2_function.js"></script>

<script type="text/javascript">

    toastr.options = {
        "closeButton": true,
        "debug": false,
        "positionClass": "toast-top-right",
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "10000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    function chartLoadData(myChart, actionName, titleName, zoomStart) {
        myChart.showLoading();

        $.ajax({
            type: "POST",
            async: false,
            url: encodeURI(actionName),
            dataType: "json",
            success: function (svr_data) {
                myChart.hideLoading();

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
                        left: '10%',
                        right: '10%',
                        bottom: '15%'
                    },
                    xAxis: {
                        type: 'category',
                        data: svr_data.xAxis_data,
                        scale: true,
                        boundaryGap: false,
                        axisLine: {onZero: false},
                        splitLine: {show: false},
                        splitNumber: 20,
                        min: 'dataMin',
                        max: 'dataMax'
                    },
                    yAxis: [
                        {
                            scale: true,
                            splitArea: {
                                show: false
                            },
                            splitLine: {
                                show: false
                            }
                        },
                        {
                            name: '指标',
                            position: 'right',
                            type: 'value',
                            axisLabel: {
                                show: false
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'inside',
                            start: zoomStart,
                            end: 100
                        },
                        {
                            show: true,
                            type: 'slider',
                            y: '90%',
                            start: zoomStart,
                            end: 100
                        }
                    ],
                    series: [
                        {
                            name: titleName,
                            type: 'candlestick',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    color: 'red',
                                    width: 1,
                                    type: 'dotted'
                                }
                            },
                            data: svr_data.series_data_k,
                            markPoint: {
                                animation: false,
                                data: svr_data.markpoint_data
                            },
                            markLine: {
                                symbol: ['none', 'none'],
                                animation: false,
                                lineStyle: {
                                    normal: {
                                        color: 'blue',
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                data: svr_data.markline_data
                            },
                            markArea: {
                                silent: true,
                                animation: false,
                                itemStyle: {
                                    normal: {
                                        color: 'transparent',
                                        borderWidth: 0,
                                        borderType: 'solid'
                                    }
                                },
                                data: svr_data.markarea_data
                            }
                        },
                        {
                            name: '成交量',
                            type: 'bar',
                            yAxisIndex: 1,
                            stack: '成交量',
                            animation: false,
                            itemStyle: {
                                color: '#DDDDDD'
                            },
                            data: svr_data.series_data_vol
                        },
                        {
                            name: 'BIAS:5',
                            type: 'bar',
                            yAxisIndex: 1,
                            animation: false,
                            stack: 'BIAS',
                            data: svr_data.series_data_bias_5
                        },
                        {
                            name: 'BIAS:20',
                            type: 'bar',
                            yAxisIndex: 1,
                            animation: false,
                            stack: 'BIAS',
                            data: svr_data.series_data_bias_20
                        },
                        {
                            name: 'MACD',
                            type: 'bar',
                            yAxisIndex: 1,
                            animation: false,
                            stack: 'MACD',
                            data: svr_data.series_data_macd
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
                // 响应事件
                myChart.on('click', function (params) {
                    if (params.componentType === 'markLine') {
                        toastr.info("动能值：" + params.value);
                    }
                });
            }
        });
    }

    $(document).ready(function () {
        initSelect2("ts_item", "qryIDTextSelectResult.action", "futList", "200px");

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('div_fut'));

        chartLoadData(myChart, "qryFutTechByCode.action?paramValue=SR.ZCE_白银主力", "白糖主力", 60);

    });

</script>

</body>
</html>