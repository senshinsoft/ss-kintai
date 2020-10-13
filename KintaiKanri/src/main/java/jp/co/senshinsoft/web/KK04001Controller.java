package jp.co.senshinsoft.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String enterKK04001() {
//		// Serviceクラスの実行に必要な値の取得（結合前は直打ちにて実装）
//		String userId = "78";
//		String year = "2020";
//		String month = "9";
//		// Serviceクラスを呼び出して、KK04001に必要な値を取得する。
//		userService.findEmployeeName(userId);
//		dailyService.findEmployeeWorkRecordDaily(userId, year, month);
//		monthlyService.findEmployeeWorkRecordMonthly(userId, year, month);
		return "KK04001";
	}
}