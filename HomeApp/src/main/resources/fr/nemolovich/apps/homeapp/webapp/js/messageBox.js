var MESSAGE_BOX_ID=0;
var DEFAULT_HIDDING_TIME=2000;

function hideMessage(id) {
	hideMessage(id, DEFAULT_HIDDING_TIME);
}

function hideMessage(id, time) {
	var box=$('#'+id);
	box.mouseenter(function() {
		showMessage(id, -1);
	});
	box.mouseleave(function() {
		hideMessage(id, DEFAULT_HIDDING_TIME);
	});
    box.clearQueue();
    box.stop();
	box.delay(time).animate({
		"opacity": "0"
	}, 3000, function () {
		box.css('display', 'none');
		box.remove();
	});
}

function showMessage(id, time) {
	var box=$('#'+id);
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

function addMessage(msg) {
	var boxContainer=$('#msgBoxContainer');
	var id='msgBox'+(MESSAGE_BOX_ID++);
	var div=$('<div class="msgBox" id="'+id+'">'+msg+'</div>');
	boxContainer.append(div);
	showMessage(id, 10000);
}

$(document).ready(function() {
	$('.msgBox')
});