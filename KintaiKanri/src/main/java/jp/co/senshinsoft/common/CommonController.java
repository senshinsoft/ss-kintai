package jp.co.senshinsoft.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;
import jp.co.senshinsoft.service.UserService;
import jp.co.senshinsoft.service.WorkReportDailyService;
import jp.co.senshinsoft.service.WorkReportMonthlyService;
import jp.co.senshinsoft.web.KK04001Form;

@Controller
public class CommonController {
	@Autowired
	private UserService userService;
	@Autowired
	private WorkReportDailyService dailyService;
	@Autowired
	private WorkReportMonthlyService monthlyService;

	private GetLoginUserDetails userInfo = new GetLoginUserDetails();
	private Calendar calendar = Calendar.getInstance();

	/**
	 * ログアウトしてKK01001(ログイン画面)に戻る
	 * 
	 * @return KK01001のパス
	 */
	@RequestMapping(value = "/logout", params = "logout", method = RequestMethod.GET)
	public String logout() {
		return "redirect:/login?logout";
	}

	/**
	 * 
	 * @param KK04001Form 勤務報告書画面から引き継いだ値（選択されている年月と選択されている社員の名前）
	 * @return KK04001のパス
	 */
	@RequestMapping(value = "reloadKK04001")
	public String reloadKK04001(KK04001Form KK04001Form, Model model) {
		 List<WorkReportDaily> workDailyList = new ArrayList<>();
		String[] name = KK04001Form.getUserName().split(" ");
		String userId = userService.findEmployeeUserId(name[0], name[1]);
		String year = KK04001Form.getYear().substring(0,4);
		String month = KK04001Form.getMonth().substring(0,2);
		// Serviceクラスを呼び出して、KK04001に必要な値を取得する。
		KK04001Form.setUserName(userService.findEmployeeName(userId));
		// ユーザーの日次勤務情報のリスト取得してmodelオブジェクトへ格納する。
		workDailyList = dailyService.findEmployeeWorkRecordDaily(userId, year, month);
		Date date = new Date();
		String day = year+ "/" + month + "/"
				+ workDailyList.get(0).getDay();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			date = sdf.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);

		// 該当月の最後の日付の取得
		int last = calendar.getActualMaximum(Calendar.DATE);
		// 該当月の最初の日付を取得
		int first = calendar.getActualMinimum(Calendar.DATE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd　E");
		// 該当月の初日から月末日までを取得して、リストに加える。
		do {
			calendar.set(Calendar.DATE, first);
			workDailyList.get(first - 1).setDay((sdf.format(calendar.getTime()).substring(8, 12)));
			first++;
		} while (first <= last );
		model.addAttribute("workDailyList", workDailyList);

		// 勤務月次情報テーブルからKK04001に必要な値を取得して、modelオブジェクトへ格納する
		List<WorkReportMonthly> workMonthlyList = monthlyService.findEmployeeWorkRecordMonthly(userId, year, month); // ユーザーの月次勤務情報
		for (WorkReportMonthly wrm : workMonthlyList) {
			KK04001Form.setKdJknKei(wrm.getKdJknKei());
			KK04001Form.setWorkingDate(wrm.getYear() + "年" + wrm.getMonth() + "月度");
			KK04001Form.setTeiji(wrm.getTeiji());
			KK04001Form.setJkngiKei(wrm.getJkngiKei());
			KK04001Form.setPjMei(wrm.getPjMei());
			KK04001Form.setTokkijiko(wrm.getTokkijiko());
			KK04001Form.setYear(wrm.getYear());
			KK04001Form.setMonth(wrm.getMonth());
			KK04001Form.setAuthFlg(wrm.getAuthFlg());
		}
		model.addAttribute("userRole", userInfo.getLoginUser().getAdminFlg());
		model.addAttribute("screenName", "勤務報告書");
		model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
		KK04001Form.setAdminFlg(userInfo.getLoginUser().getAdminFlg());
		return "KK04001";
	}
}
