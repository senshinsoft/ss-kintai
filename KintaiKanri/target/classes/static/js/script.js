/**************************
 * コロン編集を行うFunction
 **************************/
function toColon(obj) {
	if ((obj.value).trim().length == 4 && !isNaN(obj.value)) {
		var str = obj.value.trim();
		var h = str.substr(0, 2);
		var m = str.substr(2, 2);
		obj.value = h + ":" + m;
	}
	if ((obj.value).trim().length == 3 && !isNaN(obj.value)) {
		var str = ('0000' + obj.value.trim()).slice(-4);
		var h = str.substr(0, 2);
		var m = str.substr(2, 2);
		obj.value = h + ":" + m;
	}

}

/**************************
 * コロン編集を解除するFunction
 **************************/
function offColon(obj) {
	var reg = new RegExp(":", "g");
	var chgVal = obj.value.replace(reg, "");
	if (!isNaN(chgVal)) {
		obj.value = chgVal;  //値セット
		obj.select();        //全選択
	}
}

/*****************************************************************
 * 稼働、時間外労働時間をを計算して、合計稼働、合計時間外稼働時間を出力する
 *****************************************************************/
function workTimeCalc() {
	var line;
	var tmp;
	var ssJkn = document.getElementsByName("ssJkn");
	var tsJkn = document.getElementsByName("tsJkn");
	var kkJkn = document.getElementsByName("kkJkn");
	var kdJkn = document.getElementsByName("kdJkn");
	var jkngi = document.getElementsByName("jkngi");
	var teiji = document.getElementsByName("teiji");
	var line;
	var kdTime = "";
	var jkngiTime = 0;
	var jkngiKei = '00:00';
	var kdJknKei = '00:00';

	for (line = 0; line < ssJkn.length; line++) {
		//：記号と空白を除去し、入力欄の値に反映します。
		stvalorg = ssJkn[line].value.replace(/^\s+|\s+$|\:/g, '');
		envalorg = tsJkn[line].value.replace(/^\s+|\s+$|\:/g, '');
		kkvalorg = kkJkn[line].value.replace(/^\s+|\s+$|\:/g, '');

		//未入力、時刻逆転の場合、計算対象外にします。
		if (stvalorg == '' || envalorg == '' || kkvalorg == ''
			|| isNaN(stvalorg) || isNaN(envalorg) || isNaN(kkvalorg)
			|| stvalorg > envalorg) {
			workhr = '00:00';
		} else {
			//時と分を2桁ずつ切り出します。
			stmn = stvalorg.substr(-2, 2);
			enmn = envalorg.substr(-2, 2);
			kkmn = kkvalorg.substr(-2, 2);
			sthr = stvalorg.substr(-4, 2);
			enhr = envalorg.substr(-4, 2);
			kkhr = kkvalorg.substr(-4, 2);
	
			//退社時間 - 出社時間 - 休憩時間 (hr:時　mn:分)
			workhr = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr);
			workmn = (parseInt(enmn) - parseInt(stmn) - parseInt(kkmn)) / 60;

			if (workmn < 0) {
				workhr = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 1;
				workmn = 1 + workmn;
				workmn = Math.round(workmn * 60);
			}

			else if (workmn < -100) {
				workhr = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 2;
				workmn = 2 + workmn;
				workmn = Math.round(workmn * 60);

			} else {
				workmn = Math.round(workmn * 60);
			}
			
			//文字列に変換して文字列の長さによって0埋めを行う
			workhr = String(workhr);
			workmn = String(workmn);
			kdTime = workhr + workmn;

			if (kdTime.length == 3) {
				workhr = ('00' + workhr).slice(-2);
				workmn = ('00' + workmn).slice(-2);
				kdTime = workhr + ':' + workmn;
			}
			else if (kdTime.length == 4) {
				kdTime = workhr + ':' + workmn;
			}
			else {
				workhr = ('00' + workhr).slice(-2);
				workmn = ('00' + workmn).slice(-2);
				kdTime = workhr + ':' + workmn;
			}
		}

		kdJkn[line].value = kdTime;


		//時間外労働時間計算
		calcKdjkn = kdTime.replace(/^\s+|\s+$|\:/g, '');
		calcTeiji = teiji[0].value.replace(/^\s+|\s+$|\:/g, '');
		
		//値が存在しない場合、00:00を返す
		if (calcTeiji == '' || isNaN(calcTeiji) ||
			calcKdjkn == '' || isNaN(calcKdjkn)) {
			totalJkngi = '00:00';
		//存在していたら文字列を時分で区切る
		} else {　
			kdHour = calcKdjkn.substr(-4, 2);
			teijiHour = calcTeiji.substr(-4, 2);
			kdMin = calcKdjkn.substr(-2, 2);
			teijiMin = calcTeiji.substr(-2, 2);

			//稼働時間 - 定時間 (Hour:時　Min:分)
			diffJkngiHour = parseInt(kdHour) - parseInt(teijiHour);
			diffJkngiMin = (parseInt(kdMin) - parseInt(teijiMin)) / 60;

			if (diffJkngiMin < 0) {
				diffJkngiHour = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 1;
				diffJkngiMin = 1 + diffJkngiMin;
				diffJkngiMin = Math.round(diffJkngiMin * 60);
			}

			else if (diffJkngiMin < -100) {
				diffJkngiHour = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 2;
				diffJkngiMin = 2 + diffJkngiMin;
				diffJkngiMin = Math.round(diffJkngiMin * 60);

			} else {
				diffJkngiMin = Math.round(diffJkngiMin * 60);
			}
			
			//文字列連結を行い、文字数に応じて0埋めを行う
			diffJkngiHour = String(diffJkngiHour);
			diffJkngiMin = String(diffJkngiMin);
			jkngiTime = diffJkngiHour + diffJkngiMin;
			
			//時が0の場合(0時間以下ということ)は時間外労働時間は00:00にする
			if (parseInt(diffJkngiHour) < 0) {
				jkngiTime = '00:00';
			}
			else {
				if (jkngiTime.length == 3) {
					diffJkngiHour = ('00' + diffJkngiHour).slice(-2);
					diffJkngiMin = ('00' + diffJkngiMin).slice(-2);
					jkngiTime = diffJkngiHour + ':' + diffJkngiMin;
				}
				else if (jkngiTime.length == 4) {
					jkngiTime = diffJkngiHour + ':' + diffJkngiMin;
				}
				else {
					diffJkngiHour = ('00' + diffJkngiHour).slice(-2);
					diffJkngiMin = ('00' + diffJkngiMin).slice(-2);
					jkngiTime = diffJkngiHour + ':' + diffJkngiMin;
				}
			}
			jkngi[line].value = jkngiTime;
		}

		//稼働合計時間計算
		//入力された各行の時間
		tmp = kdJkn[line].value;
		tmp2 =jkngi[line].value;



		//記号の削除
		tmp = kdJkn[line].value.replace(/^\s+|\s+$|\:/g, '');
		tmp2 = jkngi[line].value.replace(/^\s+|\s+$|\:/g, '');
		jkngiKei = jkngiKei.replace(/^\s+|\s+$|\:/g, '');
		kdJknKei = kdJknKei.replace(/^\s+|\s+$|\:/g, '');

		if (tmp != "" && tmp2 != "") {
			//時と分を2桁ずつ切り出します。
			kdTimemn = tmp.substr(-2, 2);
			jkngimn = tmp2.substr(-2, 2);
			kdTimehr = tmp.substr(-4, 2);
			jkngihr = tmp2.substr(-4, 2);

			kdKeihr = kdJknKei.substr(-4, 2);
			jkngiKeihr = jkngiKei.substr(-4, 2);
			kdKeimn = kdJknKei.substr(-2, 2);
			jkngiKeimn = jkngiKei.substr(-2, 2);


			sumJkngiHour = parseInt(jkngihr);
			sumJkngiMin = (parseInt(jkngimn)) / 60;
			sumKdTimeHour = parseInt(kdTimehr);
			sumKdTimeMin = (parseInt(kdTimemn)) / 60;

			sumJkngiKeiHour = parseInt(jkngiKeihr);
			sumKdJknKeiHour = parseInt(kdKeihr);
			sumJkngiKeiMin = (parseInt(jkngiKeimn)) / 60;
			sumKdJknKeiMin = (parseInt(kdKeimn)) / 60;


			kdJknKeiHour = parseInt(sumKdJknKeiHour) + parseInt(sumKdTimeHour);
			jkngiKeiHour = parseInt(sumJkngiHour) + parseInt(sumJkngiKeiHour);
			kdJknKeiMin = parseFloat(sumKdTimeMin) + parseFloat(sumKdJknKeiMin);
			jkngiKeiMin = parseFloat(sumJkngiMin) + parseFloat(sumJkngiKeiMin);

			//稼働合計時間の60進数に直す計算
			if (kdJknKeiMin < 1) {
				kdJknKeiHour = parseInt(kdJknKeiHour);
				kdJknKeiMin = Math.round(kdJknKeiMin * 60);
				kdJknKei = String(kdJknKeiHour) + String(kdJknKeiMin);
			} else if (kdJknKeiMin < 2) {
				kdJknKeiHour = 1 + parseInt(kdJknKeiHour);
				kdJknKeiMin = Math.round((kdJknKeiMin - 1) * 60);
				kdJknKei = String(kdJknKeiHour) + String(kdJknKeiMin);
			} else {
				kdJknKeiHour = 2 + parseInt(kdJknKeiHour);
				kdJknKeiMin = Math.round((kdJknKeiMin - 2) * 60);
				kdJknKei = String(kdJknKeiHour) + String(kdJknKeiMin);
			}
			//時間外労働合計時間の60進数に直す計算
			if (jkngiKeiMin < 1) {
				jkngiKeiHour = parseInt(jkngiKeiHour);
				jkngiKeiMin = Math.round(jkngiKeiMin * 60);
				jkngiKei = String(jkngiKeiHour) + String(jkngiKeiMin);
			}
			else if (jkngiKeiMin < 2) {
				jkngiKeiHour = 1 + parseInt(jkngiKeiHour);
				jkngiKeiMin = Math.round((jkngiKeiMin - 1) * 60);
				jkngiKei = String(jkngiKeiHour) + String(jkngiKeiMin);

			} else {
				jkngiKeiHour = 2 + parseInt(jkngiKeiHour);
				jkngiKeiMin = Math.round((jkngiKeiMin - 2) * 60);
				jkngiKei = String(jkngiKeiHour) + String(jkngiKeiMin);
			}
			
			//稼働時間合計をコロンを入れた形に整形する
			if (kdJknKei.length == 2) {
				kdJknKeiHour = ('00' + kdJknKeiHour).slice(-2);
				kdJknKeiMin = ('00' + kdJknKeiMin).slice(-2);
				kdJknKei = kdJknKeiHour + ':' + kdJknKeiMin;

			}
			else if (kdJknKei.length == 3) {
				kdJknKeiHour = ('00' + kdJknKeiHour).slice(-2);
				kdJknKeiMin = ('00' + kdJknKeiMin).slice(-2);
				kdJknKei = kdJknKeiHour + ':' + kdJknKeiMin;
			}
			else if (kdJknKei.length == 4) {
				kdJknKei = kdJknKeiHour + ':' + kdJknKeiMin;
			}
			else {
				kdJknKeiHour = ('000' + kdJknKeiHour).slice(-3);
				kdJknKeiMin = ('00' + kdJknKeiMin).slice(-2);
				kdJknKei = diffJkngiHour + ':' + kdJknKeiMin;
			}

			

			//時間外労働合計をコロンを入れた形に整形する
			if (jkngiKei.length == 2) {
				jkngiKeiHour = ('00' + jkngiKeiHour).slice(-2);
				jkngiKeiMin = ('00' + jkngiKeiMin).slice(-2);
				jkngiKei = jkngiKeiHour + ':' + jkngiKeiMin;
			}
			else if (jkngiKei.length == 3) {
				jkngiKeiHour = ('00' + jkngiKeiHour).slice(-2);
				jkngiKeiMin = ('00' + jkngiKeiMin).slice(-2);
				jkngiKei = jkngiKeiHour + ':' + jkngiKeiMin;
			}
			else if (jkngiKei.length == 4) {
				jkngiKei = jkngiKeiHour + ':' + jkngiKeiMin;
			}
			else {
				jkngiKeiHour = ('000' + jkngiKeiHour).slice(-3);
				jkngiKeiMin = ('00' + jkngiKeiMin).slice(-2);
				jkngiKei = jkngiKeiHour + ':' + jkngiKeiMin;
			}

			document.form.kdJknKei.value  =kdJknKei;
			document.form.jkngiKei.value  =jkngiKei;
		}

		//出社・退社・休憩が空欄の場合に、Nanが出ないように空文字をセットする
		jkngiTime = "";
		kdTime = "";
		jkngiKei == "";
		kdJknKei == "";
	}
}
