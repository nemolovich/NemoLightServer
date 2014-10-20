/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function request(jsonObject) {
	return $.ajax({
		type : 'POST',
		url : '/ajax',
		data : {
			value : 'value'
		},
		success : function(response) {
			console.log('Success: '+response);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.error(xhr.status); // 0
			console.error(thrownError);
		}
	});
}