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
    <title>胸毛兄的股票分析系统:技术</title>

    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/ionicons.min.css">
    <link rel="stylesheet" href="/css/patch.css">
    <link rel="stylesheet" href="/css/docs.min.css">
    <link rel="stylesheet" href="/css/plugins/jquery.toastr/toastr.min.css">

</head>

<body>

<div id="div_indexe_000852SH" style="width: 1600px; height: 500px;"></div>
<div id="div_indexe_000001SH" style="width: 1600px; height: 500px;"></div>
<div id="div_indexe_399001SZ" style="width: 1600px; height: 500px;"></div>

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

    $(document).ready(function () {
        // 基于准备好的dom，初始化echarts实例
        var myChart_indexe_000852SH = echarts.init(document.getElementById('div_indexe_000852SH'));

        myChart_indexe_000852SH.showLoading();

        $.ajax({
            type: "POST",
            async: false,
            url: encodeURI("qryIndexesTech_000852SH.action"),
            dataType: "json",
            success: function (svr_data) {
                myChart_indexe_000852SH.hideLoading();

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
                            start: 60,
                            end: 100
                        },
                        {
                            show: true,
                            type: 'slider',
                            y: '90%',
                            start: 60,
                            end: 100
                        }
                    ],
                    series: [
                        {
                            name: '中证1000',
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
                            /*markPoint: {
                                data: [
                                    {type: 'max', name: '最大值'},
                                    {type: 'min', name: '最小值'}
                                ]
                            },*/
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
                            name: 'EMA:5',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_5
                        },
                        {
                            name: 'EMA:10',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_10
                        },
                        {
                            name: 'EMA:20',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_20
                        },
                        {
                            name: 'EMA:30',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_30
                        },
                        {
                            name: 'EMA:250',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_250
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
                myChart_indexe_000852SH.setOption(option);
                // 响应事件
                myChart_indexe_000852SH.on('click', function (params) {
                    if (params.componentType === 'markLine') {
                        toastr.info("动能值：" + params.value);
                    }
                });
            }
        });

        //==============================================================================================================

        var myChart_indexe_000001SH = echarts.init(document.getElementById('div_indexe_000001SH'));

        myChart_indexe_000001SH.showLoading();

        $.ajax({
            type: "POST",
            async: false,
            url: encodeURI("qryIndexesTech_000001SH.action"),
            dataType: "json",
            success: function (svr_data) {
                myChart_indexe_000001SH.hideLoading();

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
                            start: 80,
                            end: 100
                        },
                        {
                            show: true,
                            type: 'slider',
                            y: '90%',
                            start: 80,
                            end: 100
                        }
                    ],
                    series: [
                        {
                            name: '上证综指',
                            type: 'candlestick',
                            yAxisIndex: 0,
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
                            /*markPoint: {
                                data: [
                                    {type: 'max', name: '最大值'},
                                    {type: 'min', name: '最小值'}
                                ]
                            },*/
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
                            name: 'EMA:5',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_5
                        },
                        {
                            name: 'EMA:10',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_10
                        },
                        {
                            name: 'EMA:20',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_20
                        },
                        {
                            name: 'EMA:30',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_30
                        },
                        {
                            name: 'EMA:250',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_250
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
                myChart_indexe_000001SH.setOption(option);
                // 响应事件
                myChart_indexe_000001SH.on('click', function (params) {
                    if (params.componentType === 'markLine') {
                        toastr.info("动能值：" + params.value);
                    }
                });
            }
        });

        //==============================================================================================================

        var myChart_indexe_399001SZ = echarts.init(document.getElementById('div_indexe_399001SZ'));

        myChart_indexe_399001SZ.showLoading();

        $.ajax({
            type: "POST",
            async: false,
            url: encodeURI("qryIndexesTech_399001SZ.action"),
            dataType: "json",
            success: function (svr_data) {
                myChart_indexe_399001SZ.hideLoading();

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
                            start: 80,
                            end: 100
                        },
                        {
                            show: true,
                            type: 'slider',
                            y: '90%',
                            start: 80,
                            end: 100
                        }
                    ],
                    series: [
                        {
                            name: '深证成指',
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
                            /*markPoint: {
                                data: [
                                    {type: 'max', name: '最大值'},
                                    {type: 'min', name: '最小值'}
                                ]
                            },*/
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
                            name: 'EMA:5',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_5
                        },
                        {
                            name: 'EMA:10',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_10
                        },
                        {
                            name: 'EMA:20',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_20
                        },
                        {
                            name: 'EMA:30',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_30
                        },
                        {
                            name: 'EMA:250',
                            type: 'line',
                            yAxisIndex: 0,
                            animation: false,
                            lineStyle: {
                                normal: {
                                    width: 1
                                }
                            },
                            data: svr_data.series_data_ema_250
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
                myChart_indexe_399001SZ.setOption(option);
                // 响应事件
                myChart_indexe_399001SZ.on('click', function (params) {
                    if (params.componentType === 'markLine') {
                        toastr.info("动能值：" + params.value);
                    }
                });
            }
        });

        echarts.connect([myChart_indexe_000001SH, myChart_indexe_399001SZ]);

    });

</script>

</body>
</html>