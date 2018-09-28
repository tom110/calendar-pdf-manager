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
            select: function (start, end) {

                $("#myModal").modal("show");
                var key = new Date(start);
                key.setHours(key.getHours() + 1);
                var str = key.getFullYear() + "-" +
                    fix((key.getMonth() + 1), 2) +
                    "-" + fix(key.getDate(), 2) + "T" +
                    fix(key.getHours()-1, 2) + ":" +
                    fix(key.getMinutes(), 2);
                $("#date").val(key);
                // var title = prompt('Event Title:');
                // var eventData;
                // if (title) {
                //     eventData = {
                //         title: title,
                //         start: start,
                //         end: end
                //     };
                //     $('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
                // }
                $('#calendar').fullCalendar('unselect');
            },
            editable: false,
            eventLimit: true, // allow "more" link when too many events
            events: {
                url: 'getPdfs',
                error: function() {
                    $('#script-warning').show();
                }
            },
            eventClick: function(event) {
                if(confirm("删除此PDF文档吗？")){
                    $.post("deletePDF",{
                        title:event.title,
                        start:event.start._i
                    },function (dat) {
                        location.reload();
                        alert(dat);
                    })
                }
            },
            loading: function(bool) {
                $('#loading').toggle(bool);
            }
        });
    });
});
