$(document).ready(function(e) {
	$(function () {
        $('#datetimepicker1').datepicker({
            format: "dd/mm/yyyy",
            language: $.cookie('localeCookie'),
            autoclose: true,
            todayHighlight: true
        });
    });
});