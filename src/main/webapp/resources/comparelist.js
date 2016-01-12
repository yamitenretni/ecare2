$(document).ready(function() {
    var $compareAdd = $(".compare-add");
    var $compareCancel = $(".compare-cancel");

    $compareAdd.add($compareCancel).on("click", function() {
        var $link = $(this);
        var ajaxUrl = $link.prop('href') + "/json";

        $.get(ajaxUrl, function(data) {
            if (data.success) {
                var action = $link.is($compareAdd) ? "added to" : "removed from";
                toastr.info(data.tariff + " has been " + action + " compare");
                $link.addClass("hidden");
                $link.siblings().removeClass("hidden");
            }
        });
        return false;
    });
});
