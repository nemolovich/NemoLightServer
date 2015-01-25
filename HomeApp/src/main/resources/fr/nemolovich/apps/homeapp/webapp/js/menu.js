var menuActionMutex = false;
var hideTimeout;
var DEFAULT_HIDE_DELAY=200;
var ELEMENTS_DELAY = new Map();
ELEMENTS_DELAY.put('a', 275);
ELEMENTS_DELAY.put('input[type!=submit]', 200);
ELEMENTS_DELAY.put('input[type=submit]', 250);
ELEMENTS_DELAY.put('.ui-icon', 100);
ELEMENTS_DELAY.put('.icon-close', 350);

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
	resizeContent();
    $('#header').mouseenter(function () {
        showMenu($(this));
    });
	
	var keys = ELEMENTS_DELAY.keySet();
	for (var i = 0; i<keys.length; i++) {
		$(keys[i]).mouseenter(function(){
			inputHover($(this));
		});
		$(keys[i]).focus(function(){
			inputHover($(this));
		});
		$(keys[i]).mouseleave(function(){
			inputLost($(this));
		});
		$(keys[i]).focusout(function(){
			inputLost($(this));
		});
	}
});

function resizeContent() {
	var marginBottom = 100;
    var contentHeight = $('#content-div').outerHeight(true);
    var logoHeight = $('#logo-div').outerHeight(true);
    $('#main-div').css('height', (contentHeight - logoHeight - marginBottom) + 'px');
}

function inputHover(input) {
	input.addClass('hover');
}

function inputLost(input) {
	var delay = DEFAULT_HIDE_DELAY;
	var keys = ELEMENTS_DELAY.keySet();
	for (var i = 0; i<keys.length; i++) {
		if(input.is(keys[i])) {
			delay = ELEMENTS_DELAY.get(keys[i]);
			break;
		}
	}
	setTimeout(function() {
		input.removeClass('hover');
	}, delay);
}

onresize = (function () {
    resizeContent();
});

if(!String.prototype.startsWith) {
    String.prototype.startsWith = function (str) {
        return !this.indexOf(str);
    }
}

if(!String.prototype.contains) {
    String.prototype.contains = function (str) {
        return this.indexOf(str) > -1;
    }
}
