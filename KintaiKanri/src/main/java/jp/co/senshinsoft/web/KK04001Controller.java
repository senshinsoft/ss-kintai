package jp.co.senshinsoft.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;
import jp.co.senshinsoft.service.UserService;
import jp.co.senshinsoft.service.WorkReportDailyService;
import jp.co.senshinsoft.service.WorkReportMonthlyService;

@Controller
public class KK04001Controller {
	@Autowired
	private UserService userService;
	@Autowired
	private WorkReportDailyService dailyService;
	@Autowired
	private WorkReportMonthlyService monthlyService;

	@ModelAttribute(value = "KK04001Form")
	public KK04001Form workReportForm() {
		return new KK04001Form();
	}

	@RequestMapping(value = "atode-settei")
	public String enterKK04001(@ModelAttribute("insertForm") KK04001Form form) {
		// Serviceクラスの実行に必要な値の取得（結合前は直打ちにて実装）
		String userId = "78";
		String year = "2020";
		String month = "9";
		// Serviceクラスを呼び出して、KK04001に必要な値を取得する。
		// ユーザーの名前をformに格納
		form.setUserName(userService.findEmployeeName(userId));
		// ユーザーの日次勤務情報のリスト取得→表示用にformに格納
		List<WorkReportDaily> workDailyList = dailyService.findEmployeeWorkRecordDaily(userId, year, month);
		for (WorkReportDaily wrd : workDailyList) {
			form.setSsJkn(wrd.getSsJkn());
			form.setTsJkn(wrd.getTsJkn());
			form.setKkJkn(wrd.getKkJkn());
			form.setKdJkn(wrd.getKdJkn());
			form.setJkngi(wrd.getJkngi());
			form.setBiko(wrd.getBiko());
		}
		List<WorkReportMonthly> workMonthlyList = monthlyService.findEmployeeWorkRecordMonthly(userId, year, month); // ユーザーの月次勤務情報
		for(WorkReportMonthly wrm:workMonthlyList) {	
		form.setWorkingDate(wrm.getYear()+"年"+wrm.getMonth()+"月度");
		form.setTeiji(wrm.getTeiji());
		form.setKdJknKei(wrm.getKdJknKei());
		form.setJkngiKei(wrm.getJkngiKei());
		form.setPjMei(wrm.getPjMei());
		form.setTokkijiko(wrm.getTokkijiko());
		}
		return "KK04001";
	}
}