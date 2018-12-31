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
    <title>胸毛兄的股票分析系统:情绪分析</title>

    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/ionicons.min.css">
    <link rel="stylesheet" href="/css/patch.css">
    <link rel="stylesheet" href="/css/docs.min.css">

    <link rel="stylesheet" href="/css/plugins/jquery.toastr/toastr.min.css">
    <link rel="stylesheet" href="/css/dygraph.css">

</head>

<body>
<div align="center">
    <h3><a href="/index_sh.jsp">上海</a></h3>
    <h3><a href="/index_sz.jsp">深圳</a></h3>
</div>
<br>
<br>

<div id="qryAnalyseIndexData_sh" style="width: 1600px; height: 500px;"></div>
<div id="qryAnalyseIndexData_sz" style="width: 1600px; height: 500px;"></div>
<div id="qryAnalyseIndexData_399006" style="width: 1600px; height: 500px;"></div>

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

<script language="javascript" type="text/javascript" src="/js/plugins/toastr.min.js"></script>
<script language="javascript" type="text/javascript" src="/js/dygraph.min.js"></script>

<script type="text/javascript">
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "positionClass": "toast-top-right",
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "2000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    annotations = [];
    var graph_initialized = false;

    idx1 = new Dygraph(
        document.getElementById("qryAnalyseIndexData_sh"),
        "/qryAnalyseIndexData_sh.action",
        {
            rollPeriod: 1,
            showRoller: true,
            customBars: false,
            title: '上证指数',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(230,230,230)", "rgb(255,100,100)", "rgb(255,100,100)", "rgb(51,204,204)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '上证指数': {showInRangeSelector: true}
            },
            zoomCallback: function (minX, maxX, yRanges) {
                idx2.updateOptions({
                    dateWindow: [minX, maxX]
                });
                idx3.updateOptions({
                    dateWindow: [minX, maxX]
                });
            },
            drawCallback: function (g, is_initial) {
                if (is_initial) {
                    graph_initialized = true;
                    if (annotations.length > 0) {
                        g.setAnnotations(annotations);
                    }
                }

                idx2.updateOptions({
                    dateWindow: [g.xAxisRange()[0], g.xAxisRange()[1]]
                });
                idx3.updateOptions({
                    dateWindow: [g.xAxisRange()[0], g.xAxisRange()[1]]
                });
            }
        }
    );

    $.ajax({
        type: "POST",
        async: false,
        url: encodeURI("qryAnalyseEventData.action"),
        dataType: "json",
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                annotations.push({
                    series: '上证指数',
                    x: data[i].yyyymmdd,
                    icon: '/images/' + data[i].icon_name,
                    width: 24,
                    height: 24,
                    tickHeight: 5,
                    tickColor: 'indianred',
                    tickWidth: 1,
                    text: data[i].event_memo
                });
            }
        }
    });

    if (graph_initialized) {
        idx1.setAnnotations(annotations);
    }

    idx2 = new Dygraph(
        document.getElementById("qryAnalyseIndexData_sz"),
        "/qryAnalyseIndexData_sz.action",
        {
            rollPeriod: 1,
            showRoller: true,
            customBars: false,
            title: '深证指数',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(230,230,230)", "rgb(255,100,100)", "rgb(255,100,100)", "rgb(51,204,204)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '深证指数': {showInRangeSelector: true}
            }
        }
    );

    idx3 = new Dygraph(
        document.getElementById("qryAnalyseIndexData_399006"),
        "/qryAnalyseIndexData_399006.action",
        {
            rollPeriod: 1,
            showRoller: true,
            customBars: false,
            title: '创业板指数',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(230,230,230)", "rgb(255,100,100)", "rgb(255,100,100)", "rgb(51,204,204)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '创业板指数': {showInRangeSelector: true}
            }
        }
    );
</script>

<script type="text/javascript">

</script>

</body>
</html>