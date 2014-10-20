/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function request(jsonObject) {
        return $.ajax({
                url: 'http://localhost/ajax',
                data: {
                        value: 'value'
                },
                type: 'POST',
                dataType: 'json',
                success: function(response) {
                    console.log('Success!');
                }
        });
}