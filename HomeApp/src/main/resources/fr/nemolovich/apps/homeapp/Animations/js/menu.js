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
    $('#header').mouseenter(function () {
        showMenu($(this));
    });
	// $('#header').mouseleave(function () {
        // hideMenu($(this));
    // });
    // hideTimeout = setTimeout(function () {
        // hideMenu($("#header"));
    // }, 500);
    resizeContent();
	$('input').mouseenter(function(){
		inputHover($(this));
	});
	$('input').mouseleave(function(){
		inputLost($(this));
	});
});

function resizeContent() {
    var height = $(window).height();
    var outerHeight = $('#content-div').outerHeight(true) - $('#content-div').height();
    var headerHeight = $('#header').outerHeight(true);
    $('#content-div').css('height', (height - outerHeight - headerHeight) + 'px');
}

function inputHover(input) {
	input.css('border-color', '#799BD2');
	input.css('border-style', 'solid');
	input.css('-webkit-box-shadow', '-1px -1px 0.1em 0.01em #799BD2, 1px 1px 0.1em 0.01em #799BD2');
}

function inputLost(input) {
	setTimeout(function() {
		input.css('border-color', '#EEEEEE');
		inputType = input.attr("type") ;
		if(inputType === "text" || inputType === "password") {
			input.css('border', '2px inset');
		} else if(inputType) {
			input.css('border', '1px solid rgba(0, 0, 0, 0.25)');
		}
		input.css('-webkit-box-shadow', 'none');
	}, 250);
}

onresize = (function () {
    resizeContent();
});