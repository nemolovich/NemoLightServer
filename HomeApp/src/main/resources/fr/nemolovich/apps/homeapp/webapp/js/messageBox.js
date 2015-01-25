var MESSAGE_BOX_ID=0;
var DEFAULT_HIDDING_TIME=2000;

function hideMessage(id) {
	hideMessage(id, DEFAULT_HIDDING_TIME);
}

function hideMessage(id, time) {
	var box = $('#' + id);
	box.mouseenter(function() {
		showMessage(id, -1);
	});
	box.mouseleave(function() {
		hideMessage(id, DEFAULT_HIDDING_TIME);
	});
    box.clearQueue();
    box.stop();
	var duration = 3000;
	box.delay(time).animate({
		"opacity": "0"
	}, duration, function () {
		box.css('display', 'none');
		box.remove();
	});
}

function killMessage(id) {
	var box = $('#' + id);
	box.unbind('mouseenter mouseleave');
    box.clearQueue();
    box.stop();
	box.css('min-height', '0px');
	var close = box.find('.title');
	close.css('display', 'none');
	box.slideUp(500);
}

function showMessage(id, time) {
	var box = $('#' + id);
    box.clearQueue();
    box.stop();
    box.animate({
        "opacity": "1"
    }, 100, function () {
		box.css('display', 'block');
    });
    box.delay(50).queue(function() {
		box.css('opacity', '1');
		box.css('display', 'block');
    });
	if(time >= 1) {
		hideMessage(id, time);
	}
}

function addMessage(title, msg, style) {
	var boxContainer = $('#msgBoxContainer');
	var id = 'msgBox' + (MESSAGE_BOX_ID++);
	var classType = "";
	if(style !== undefined) {
		classType = " " + style
	}
	var box = $('<div class="msgBox' + classType + '" id="' + id + '"></div>');
	var div=$('<div class="icon-close""></div>');
	div.click(function() {
		killMessage(id);
	});
	box.append(div);
	div=$('<div class="title">' + title + '</div>');
	box.append(div);
	div=$('<div>' + msg + '</div>');
	box.append(div);
	boxContainer.append(box);
	showMessage(id, 20000);
}

$(document).ready(function() {
	$('.msgBox')
});