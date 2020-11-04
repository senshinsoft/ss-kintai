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

function kdJknCalc() {
	var line;
	var tmp;
	var ssJkn = document.getElementsByName("ssJkn");
	var tsJkn = document.getElementsByName("tsJkn");
	var kkJkn = document.getElementsByName("kkJkn");
	var kdJkn = document.getElementsByName("kdJkn");
	var jkngi = document.getElementsByName("jkngi");
	var teiji = document.getElementsByName("teiji");
	var line;
	var workTime = "";
	var jkngiTime = 0;
	var jkngiKei = '00:00';
	var kdJknKei = '00:00';

	for (line = 0; line < ssJkn.length; line++) {
		//：記号と空白を除去し、入力欄の値に反映します。
		stvalorg = ssJkn[line].value.replace(/^\s+|\s+$|\:/g, '');
		envalorg = tsJkn[line].value.replace(/^\s+|\s+$|\:/g, '');
		kkvalorg = kkJkn[line].value.replace(/^\s+|\s+$|\:/g, '');

		//未入力、数値ではない、時刻逆転の３種類の場合、計算対象外にします。
		if (stvalorg == '' || envalorg == '' || kkvalorg == ''
			|| isNaN(stvalorg) || isNaN(envalorg) || isNaN(kkvalorg)
			|| stvalorg > envalorg) {
			myhr = 0;
		} else {
			//時と分を2桁ずつ切り出します。
			stmn = stvalorg.substr(-2, 2);
			enmn = envalorg.substr(-2, 2);
			kkmn = kkvalorg.substr(-2, 2);
			sthr = stvalorg.substr(-4, 2);
			enhr = envalorg.substr(-4, 2);
			kkhr = kkvalorg.substr(-4, 2);

			myhr2 = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr);
			myhr3 = (parseInt(enmn) - parseInt(stmn) - parseInt(kkmn)) / 60;

			if (myhr3 < 0) {
				myhr2 = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 1;
				myhr3 = 1 + myhr3;
				myhr3 = Math.round(myhr3 * 60);
			}

			else if (myhr3 < -100) {
				myhr2 = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 2;
				myhr3 = 1 + myhr3;
				myhr3 = Math.round(myhr3 * 60);

			} else {
				myhr3 = Math.round(myhr3 * 60);
			}
			myhr2 = String(myhr2);
			myhr3 = String(myhr3);
			workTime = myhr2 + myhr3;

			if (workTime.length == 3) {
				myhr2 = ('00' + myhr2).slice(-2);
				myhr3 = ('00' + myhr3).slice(-2);
				workTime = myhr2 + ':' + myhr3;
			}
			else if (workTime.length == 4) {
				workTime = myhr2 + ':' + myhr3;
			}
			else {
				myhr2 = ('00' + myhr2).slice(-2);
				myhr3 = ('00' + myhr3).slice(-2);
				workTime = myhr2 + ':' + myhr3;
			}
		}

		kdJkn[line].value = workTime;


		//時間外労働時間計算
		calcKdjkn = workTime.replace(/^\s+|\s+$|\:/g, '');
		calcTeiji = teiji[0].value.replace(/^\s+|\s+$|\:/g, '');

		if (calcTeiji == '' || isNaN(calcTeiji) ||
			calcKdjkn == '' || isNaN(calcKdjkn)) {
			totalJkngi = 0;
		} else {
			kdHour = calcKdjkn.substr(-4, 2);
			teijiHour = calcTeiji.substr(-4, 2);
			kdMin = calcKdjkn.substr(-2, 2);
			teijiMin = calcTeiji.substr(-2, 2);

			diffJkngiHour = parseInt(kdHour) - parseInt(teijiHour);
			diffJkngiMin = (parseInt(kdMin) - parseInt(teijiMin)) / 60;

			if (diffJkngiMin < 0) {
				diffJkngiHour = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 1;
				diffJkngiMin = 1 + diffJkngiMin;
				diffJkngiMin = Math.round(diffJkngiMin * 60);
			}

			else if (diffJkngiMin < -100) {
				diffJkngiHour = parseInt(enhr) - parseInt(sthr) - parseInt(kkhr) - 2;
				diffJkngiMin = 1 + diffJkngiMin;
				diffJkngiMin = Math.round(diffJkngiMin * 60);

			} else {
				diffJkngiMin = Math.round(diffJkngiMin * 60);
			}

			diffJkngiHour = String(diffJkngiHour);
			diffJkngiMin = String(diffJkngiMin);
			jkngiTime = diffJkngiHour + diffJkngiMin;
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
					workTime = diffJkngiHour + ':' + diffJkngiMin;
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
		tmp = window.document.forms[0].kdJkn[line].value;
		tmp2 = window.document.forms[0].jkngi[line].value;



		//記号の削除
		tmp = kdJkn[line].value.replace(/^\s+|\s+$|\:/g, '');
		tmp2 = jkngi[line].value.replace(/^\s+|\s+$|\:/g, '');
		jkngiKei = jkngiKei.replace(/^\s+|\s+$|\:/g, '');
		kdJknKei = kdJknKei.replace(/^\s+|\s+$|\:/g, '');

		if (tmp != "" && tmp2 != "") {
			//時と分を2桁ずつ切り出します。
			workTimemn = tmp.substr(-2, 2);
			jkngimn = tmp2.substr(-2, 2);
			workTimehr = tmp.substr(-4, 2);
			jkngihr = tmp2.substr(-4, 2);

			kdKeihr = kdJknKei.substr(-4, 2);
			jkngiKeihr = jkngiKei.substr(-4, 2);
			kdKeimn = kdJknKei.substr(-2, 2);
			jkngiKeimn = jkngiKei.substr(-2, 2);


			sumJkngiHour = parseInt(jkngihr);
			sumJkngiMin = (parseInt(jkngimn)) / 60;
			sumWorkTimeHour = parseInt(workTimehr);
			sumWorkTimeMin = (parseInt(workTimemn)) / 60;

			sumJkngiKeiHour = parseInt(jkngiKeihr);
			sumKdJknKeiHour = parseInt(kdKeihr);
			sumJkngiKeiMin = (parseInt(jkngiKeimn)) / 60;
			sumKdJknKeiMin = (parseInt(kdKeimn)) / 60;


			kdJknKeiHour = parseInt(sumKdJknKeiHour) + parseInt(sumWorkTimeHour);
			jkngiKeiHour = parseInt(sumJkngiHour) + parseInt(sumJkngiKeiHour);
			kdJknKeiMin = parseFloat(sumWorkTimeMin) + parseFloat(sumKdJknKeiMin);
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

			window.document.forms[0].kdJknKei.value = kdJknKei;
			window.document.forms[0].jkngiKei.value = jkngiKei;
		}

		//出社・退社・休憩が空欄の場合に、Nanが出ないように空文字をセットする
		jkngiTime = "";
		workTime = "";
		jkngiKei == "";
		kdJknKei == "";
	}
}
