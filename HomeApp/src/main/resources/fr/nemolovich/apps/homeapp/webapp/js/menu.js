var menuActionMutex = false;
var hideTimeout;

function showMenu(menu) {
    clearTimeout(hideTimeout);
    menuActionMutex = true;
    menu.clearQueue();
    menu.stop();
    menu.animate({
        "opacity": "1"
    }, 100, function () {
        menuActionMutex = false;
    });
}

function hideMenu(menu) {
    if (!menuActionMutex) {
        menuActionMutex = true;
        menu.delay(2000).animate({
            "opacity": "0"
        }, 800, function () {
            menuActionMutex = false;
        });
    }
}

$(document).ready(function () {
    $("#header").hover(function () {
        showMenu($(this));
    }, function () {
        hideMenu($(this));
    });
    hideTimeout = setTimeout(function () {
        hideMenu($("#header"));
    }, 500);
    resizeContent();
});

function resizeContent() {
    var height = $(window).height();
    var outerHeight = $('#content-div').outerHeight(true) - $('#content-div').height();
    var headerHeight = $('#header').outerHeight(true);
    $('#content-div').css('height', (height - outerHeight - headerHeight) + 'px');
}

onresize = (function ()
{
    resizeContent();
});