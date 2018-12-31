function formatRepo(repo) {
    //var markup = "<div>" + repo.id + " + " + repo.text + "</div>";
    var markup = "<div>" + repo.text + "</div>";
    return markup;
}

function formatRepoSelection(repo) {
    return repo.text;
}

function initSelect2(objName, actionName, busiParam) {
    return initSelect2(objName, actionName, busiParam, "100%")
}

function initSelect2(objName, actionName, busiParam, iniWidth) {
    var reObj = $('#' + objName).select2({
        ajax: {
            url: actionName,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    qryKey: params.term, // search term
                    busiName: busiParam,
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;

                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * 30) < data.total_count
                    }
                };
            },
            cache: true
        },
        escapeMarkup: function (markup) {
            return markup;
        },
        minimumInputLength: 2,
        width: iniWidth,
        language: "zh-CN",
        placeholder: "请输入",
        templateResult: formatRepo,
        templateSelection: formatRepoSelection
    });

    return reObj;
}

function getSelect2IDS(objName) {
    var selectID = "";
    var data = $('#' + objName).val();
    $.each(data, function (key, val) {
        selectID = selectID + val + "||";
    });
    return selectID;
}
