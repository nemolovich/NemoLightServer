/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function request(bean, value) {
    if(!beanExists(bean)) {
        console.error("The bean '"+bean+"' does not exist!");
        return;
    }
    var url=getAjaxURL(bean);
    var fun=getAjaxFunction(bean);
    return $.ajax({
        type: 'POST',
        url: url,
        data: {
            bean:   bean,
            value:  value
        },
        success: function (response) {
            console.log('Success: ' + response);
            try {
                var r = response.replace(/\n/g, '').replace(/\r/g, '')
                        .replace(/\/\*{2}.*\*\//,'');
                var resp = JSON.parse(r);
                console.log(resp);
                fun(resp.value);
            } catch (e) {
                console.error("Can not parse result!");
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.error(xhr.status); // 0
            console.error(ajaxOptions);
            console.error(thrownError);
        }
    });
}

function getAjaxURL(bean) {
    /*
     * TODO: Search in map
     */
    return "/ajax";
}

function getAjaxFunction(bean) {
    /*
     * TODO: Search in map
     */
    return updateSize;
}

function beanExists(bean) {
    /*
     * TODO: Search in 2 maps
     */
    return true;
}
