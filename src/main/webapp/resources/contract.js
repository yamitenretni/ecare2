$(document).ready(function() {
    var $contractTariff = $("#tariff");
    var $tariffInfo = $("#tariff-info");
    var $tariffName = $("#tariff-name");
    var $tariffCost = $("#tariff-cost");
    var $tariffOptions = $("#tariff-options");

    $contractTariff.on("change", onTariffChange);

    function onTariffChange() {
        var tariffId = $contractTariff.val();
        var ajaxUrl = "/eCare/staff/tariff/" + tariffId + "/json" ;

        $.get(ajaxUrl, function(data) {
            $tariffName.text(data.name);
            $tariffCost.text("$" +  data.regularCost);

            var $options = data.availableOptions.map(function(option) {
                return $('<div/>').text(option.name);
            });

            if (!$options.length) {
                $options = $('<div/>').text('-');
            }

            $tariffOptions.html($options);

            $tariffInfo.removeClass("hidden");
        }).fail(function() {
            $tariffInfo.addClass("hidden");
        });
    }
});
