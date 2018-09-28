define(["jquery", "moment", "locale-all", "fullcalendar", "bootstrap"], function ($) {

    //将日期格式化为两位，不足补零
    function fix(num, length) {
        return ('' + num).length < length ? ((new Array(length + 1)).join('0') + num).slice(-length) : '' + num;
    }

    $(document).ready(function () {
        var initialLocaleCode = 'zh-cn';

        $('#calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'year,month,agendaWeek,agendaDay'
            },
            // defaultDate: '2018-03-12',
            locale: initialLocaleCode,
            buttonIcons: false, // show the prev/next text
            weekNumbers: true,
            navLinks: true, // can click day/week names to navigate views
            selectable: true,
            selectHelper: true,
            editable: false,
            eventLimit: true, // allow "more" link when too many events
            eventClick: function(event) {
                $("iframe").attr("src",'/pdfjs/web/viewer.html?file=' +
                    encodeURIComponent('/returnPDF?title='+event.title+"&start="+event.start._i));
            },
            events: {
                url: 'getPdfs',
                error: function() {
                    $('#script-warning').show();
                }
            }
        });
    });
});
