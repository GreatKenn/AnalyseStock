/**
 * Created by kenn on 16/6/30.
 */
function getNowDateStr() {
    var dd = new Date();
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;//获取当前月份的日期
    var d = dd.getDate();

    if (m < 10) {
        m = "0" + m;
    }
    if (d < 10) {
        d = "0" + d;
    }

    return y + "-" + m + "-" + d;
}

function getDateStr(addDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate() + addDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;//获取当前月份的日期
    var d = dd.getDate();
    return y + "-" + m + "-" + d;
}

function getDateYearStr(addYearCount) {
    var dd = new Date();
    var y = dd.getFullYear() + addYearCount;
    return y;
}

function getNowDateYearMonthStr() {
    var dd = new Date();
    var y = dd.getFullYear();
    var m = dd.getMonth();
    if (m.length = 1) {
        m = '0' + m;
    }
    return y + '-' + m;
}