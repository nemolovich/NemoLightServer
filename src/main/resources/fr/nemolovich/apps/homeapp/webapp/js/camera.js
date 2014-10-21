/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function updateSize(json) {
    var width=json.width;
    var height=json.height;
    $('#width_input').val(width);
    $('#height_input').val(height);
    var vlc=$('#vlc_pi_camera');
    vlc.attr('width', width);
    vlc.attr('height', height);
}

function requestSize() {
    var width = $('#width_input').val();
    var height = $('#height_input').val();
    request("CameraBean", width + 'x' + height);
}