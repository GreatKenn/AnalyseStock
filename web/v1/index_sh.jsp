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

    <link rel="stylesheet" href="/css/dygraph.css">

</head>

<body>
<h3>上海</h3>

<div id="qryAnalyseLup10Rate_sh" style="width: 1600px; height: 500px;"></div>
<div id="qryAnalyseLup10agRate_sh" style="width: 1600px; height: 500px;"></div>
<div id="qryAnalyseLup10ncRate_sh" style="width: 1600px; height: 500px;"></div>
<div id="qryAnalyseLup10c5Value_sh" style="width: 1600px; height: 500px;"></div>
<div id="qryAnalyseLud10Rate_sh" style="width: 1600px; height: 500px;"></div>

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

<script language="javascript" type="text/javascript" src="/js/dygraph.min.js"></script>

<script type="text/javascript">
    sh1 = new Dygraph(
        document.getElementById("qryAnalyseLup10Rate_sh"),
        "/qryAnalyseLup10Rate_sh.action",
        {
            rollPeriod: 1,
            showRoller: true,
            customBars: false,
            title: '涨跌停率(上海)',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(255,100,100)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '上证指数': {showInRangeSelector: true}
            },
            zoomCallback: function(minX, maxX, yRanges) {
                sh2.updateOptions({ dateWindow: [minX, maxX] });
                sh3.updateOptions({ dateWindow: [minX, maxX] });
                sh4.updateOptions({ dateWindow: [minX, maxX] });
                sh5.updateOptions({ dateWindow: [minX, maxX] });
            },
            drawCallback: function(g) {
                sh2.updateOptions({ dateWindow: [g.xAxisRange()[0], g.xAxisRange()[1]] });
                sh3.updateOptions({ dateWindow: [g.xAxisRange()[0], g.xAxisRange()[1]] });
                sh4.updateOptions({ dateWindow: [g.xAxisRange()[0], g.xAxisRange()[1]] });
                sh5.updateOptions({ dateWindow: [g.xAxisRange()[0], g.xAxisRange()[1]] });
            }
        }
    );

    sh2 = new Dygraph(
        document.getElementById("qryAnalyseLup10ncRate_sh"),
        "/qryAnalyseLup10ncRate_sh.action",
        {
            rollPeriod: 1,
            showRoller: true,
            customBars: false,
            title: '涨跌停一字板率(上海)',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(255,100,100)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '上证指数': {showInRangeSelector: true}
            }
        }
    );

    sh3 = new Dygraph(
        document.getElementById("qryAnalyseLup10agRate_sh"),
        "/qryAnalyseLup10agRate_sh.action",
        {
            rollPeriod: 0,
            showRoller: true,
            customBars: false,
            title: '涨停次日上涨率(上海)',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '上证指数': {showInRangeSelector: true}
            }
        }
    );

    sh4 = new Dygraph(
        document.getElementById("qryAnalyseLup10c5Value_sh"),
        "/qryAnalyseLup10c5Value_sh.action",
        {
            rollPeriod: 0,
            showRoller: true,
            customBars: false,
            title: '连续涨跌停次数值(上海)',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(255,100,100)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '上证指数': {showInRangeSelector: true}
            }
        }
    );

    sh5 = new Dygraph(
        document.getElementById("qryAnalyseLud10Rate_sh"),
        "/qryAnalyseLud10Rate_sh.action",
        {
            rollPeriod: 0,
            showRoller: true,
            customBars: false,
            title: '非涨跌停变化率超过10的率(上海)',
            ylabel: '',
            colors: ["rgb(0,0,0)", "rgb(51,204,204)"],
            legend: 'always',
            showRangeSelector: true,
            series: {
                '上证指数': {showInRangeSelector: true}
            }
        }
    );
</script>

</body>
</html>