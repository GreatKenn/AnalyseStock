/**
 * Created by kenn on 2016/10/26.
 */

//制保留2位小数，如：2，会在2后面补上00.即2.00
function toDecimal2(doubleVal) {
    var f = parseFloat(doubleVal);
    if (isNaN(f)) {
        return false;
    }
    var f = Math.round(doubleVal * 100) / 100;
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + 2) {
        s += '0';
    }
    return s;
}


//金额千分位自动分位
function double2MoneyStr(doubleVal) {
    doubleVal = doubleVal.replace(/,/g, "");
    doubleVal = toDecimal2(doubleVal);
    if (doubleVal.length > 10) {
        doubleVal = doubleVal.substring(0, 10);
    }
    var re = /\d{1,3}(?=(\d{3})+$)/g;
    var n1 = doubleVal.replace(/^(\d+)((\.\d+)?)$/, function (s, s1, s2) {
        return "¥" + s1.replace(re, "$&,") + s2;
    });
    return n1;
}