AJAX_FIELD_SUFFIX = "_ajax_input";

function request(bean, fields, callback) {
	if (!beanExists(bean)) {
		console.error("The bean '" + bean + "' does not exist!");
		return;
	}
	var value = getAjaxValues(fields);
	var uuid = generateUUID();
	var url = "${ApplicationContext}/ajax/" + uuid;
	$.ajax({
		type : 'POST',
		url : url,
		data : {
			bean : bean,
			uid : uuid,
			value : JSON.stringify(value)
		},
		success : function(response) {
			try {
				var r = response.replace(/\n/g, '').replace(/\r/g, '').replace(
						/\/\*{2}.*\*\//, '');
				var resp = JSON.parse(r);
				if (resp.action === 'update') {
					updateFields(resp.value);
				}
				if (callback !== undefined) {
					callback();
				}
			} catch (e) {
				console.error("Can not parse result: " + e);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.error(xhr.status); // 0
			console.error(ajaxOptions);
			console.error(thrownError);
		}
	});
	return false;
}

function ajaxFunction(funcName, param, callback) {
	var AJAX_RESPONSE;
	var tmp = callAjaxFunction(funcName, param, function(data) {
		AJAX_RESPONSE = data;
	});
	if (callback !== undefined) {
		tmp = callback(AJAX_RESPONSE);
	}
	return AJAX_RESPONSE;
}

function callAjaxFunction(funcName, param, callback) {
	var res = "error";
	var uuid = generateUUID();
	var url = "${ApplicationContext}/ajax/functions/" + uuid;
	$.ajax(
			{
				async: false,
				type : 'POST',
				url : url,
				data : {
					method : funcName,
					uid : uuid,
					param : JSON.stringify(param == null ? {} : param)
				},
				success : function(response) {
					try {
						var r = response.replace(/\n/g, '').replace(/\r/g, '')
								.replace(/\/\*{2}.*\*\//, '');
						res = JSON.parse(r);
						if (callback !== undefined) {
							callback(res);
						}
					} catch (e) {
						console.error("Can not parse result: " + e);
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					console.error(xhr.status); // 0
					console.error(ajaxOptions);
					console.error(thrownError);
				}
			});
	return false;

}

function getAjaxValues(fields) {
	var result = new Object();
	for (i = 0; i < fields.length; i++) {
		var value = document.getElementById(fields[i] + AJAX_FIELD_SUFFIX);
		if (value !== null) {
			result[fields[i]] = value.value;
		}
	}
	return result;
}

function generateUUID() {
	var d = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
			function(c) {
				var r = (d + Math.random() * 16) % 16 | 0;
				d = Math.floor(d / 16);
				return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
			});
	return uuid;
}

function updateFields(value) {
	for ( var key in value) {
		if (value.hasOwnProperty(key)) {
			var elm = document.getElementById(key);
			if (elm !== null && elm !== undefined) {
				elm.innerHTML = value[key];
			}
		}
	}
}

function log(txt) {
	console.log(txt);
}

function beanExists(bean) {
	/*
	 * TODO: Search in 2 maps
	 */
	return true;
}
