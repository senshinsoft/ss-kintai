/**************************
 * コロン編集を行うFunction
 **************************/
function toColon(obj){
  if((obj.value).trim().length == 4 && !isNaN(obj.value)){
    var str = obj.value.trim();
    var h = str.substr(0,2);
    var m = str.substr(2,2);
    obj.value = h + ":" + m;
  }
}
 
/**************************
 * コロン編集を解除するFunction
 **************************/
function offColon(obj){
  var reg = new RegExp(":", "g");
  var chgVal = obj.value.replace(reg, "");
  if(!isNaN(chgVal)){
    obj.value = chgVal;  //値セット
    obj.select();        //全選択
  }
}

    
function calcKdJknKei() {
        var line;
        var tmp;
        var kdJknKei = 0;
        for(line=0;line<window.document.forms[0].kdJkn.length;line++){
            tmp = window.document.forms[0].kdJkn[line].value;

            if (tmp!="" && !isNaN(tmp)) {
                kdJknKei = kdJknKei + parseInt(tmp);
            }
        }
     window.document.forms[0].kdJknKei.value = kdJknKei;
        
    }
    
function calcJkngiKei() {
        var line;
        var tmp;
        var jkngiKei = 0;
        for(line=0;line<window.document.forms[0].jkngi.length;line++){
            tmp = window.document.forms[0].jkngi[line].value;

            if (tmp!="" && !isNaN(tmp)) {
                jkngiKei = jkngiKei + parseInt(tmp);
            }
        }
     window.document.forms[0].jkngiKei.value = jkngiKei;
        
    }