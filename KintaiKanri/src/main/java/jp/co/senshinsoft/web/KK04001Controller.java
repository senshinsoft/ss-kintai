package jp.co.senshinsoft.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;
import jp.co.senshinsoft.service.UserService;
import jp.co.senshinsoft.service.WorkReportDailyService;
import jp.co.senshinsoft.service.WorkReportMonthlyService;

@Controller
@SessionAttributes("KK04001Form")
public class KK04001Controller {
	@Autowired
	private UserService userService;
	@Autowired
	private WorkReportDailyService dailyService;
	@Autowired
	private WorkReportMonthlyService monthlyService;

	private List<WorkReportDaily> workDailyList = new ArrayList<>();

	private List<String> onlyDailyList = new ArrayList<>();

	@ModelAttribute(value = "KK04001Form")
	public KK04001Form workReportForm() {
		return new KK04001Form();
	}

	@RequestMapping(value = "KK03001")
	public String enterKK03001() {
		return "KK03001";
	}

	@RequestMapping(value = "enterKK04001")
	public String enterKK04001(KK04001Form form, Model model) {
		Calendar calendar = Calendar.getInstance();
		// Serviceクラスの実行に必要な値の取得（結合前は直打ちにて実装）
		String userId = "78";
		String year = "2020";
		String month = "10";
		// Serviceクラスを呼び出して、KK04001に必要な値を取得する。
		// ユーザーの名前をformに格納
		form.setUserName(userService.findEmployeeName(userId));
		// ユーザーの日次勤務情報のリスト取得してmodelオブジェクトへ格納する。
		workDailyList = dailyService.findEmployeeWorkRecordDaily(userId, year, month);
		// DBに勤務情報がない場合、日付のリストのみを作成しKK04001へ遷移する。
		if (workDailyList.size() == 0) { // 勤務表の登録記録が0
			// 該当月の最後の日付の取得
			int last = calendar.getActualMaximum(Calendar.DATE);
			// 該当月の最初の日付を取得
			int first = calendar.getActualMinimum(Calendar.DATE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd　E");
			// 該当月の初日から月末日までを取得して、リストに加える。
			do {
				calendar.set(Calendar.DATE, first);
				onlyDailyList.add((sdf.format(calendar.getTime()).substring(8, 12)));
				form.setYear(sdf.format(calendar.getTime()).substring(0, 4) + "年");
				form.setMonth(sdf.format(calendar.getTime()).substring(5, 7) + "月度");
				first++;
			} while (first <= last - 1);
			model.addAttribute("onlyDailyList", onlyDailyList);
			model.addAttribute("workDailyList", workDailyList);
		} else {
			Date date = new Date();
			String day = workDailyList.get(0).getYear() + "/" + workDailyList.get(0).getMonth() + "/"
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
			} while (first <= last - 1);
			model.addAttribute("workDailyList", workDailyList);
		}
		// 勤務月次情報テーブルからKK04001に必要な値を取得して、modelオブジェクトへ格納する
		List<WorkReportMonthly> workMonthlyList = monthlyService.findEmployeeWorkRecordMonthly(userId, year, month); // ユーザーの月次勤務情報
		for (WorkReportMonthly wrm : workMonthlyList) {
			form.setKdJknKei(wrm.getKdJknKei());
			form.setWorkingDate(wrm.getYear() + "年" + wrm.getMonth() + "月度");
			form.setTeiji(wrm.getTeiji());
			form.setJkngiKei(wrm.getJkngiKei());
			form.setPjMei(wrm.getPjMei());
			form.setTokkijiko(wrm.getTokkijiko());
			form.setYear(wrm.getYear());
			form.setMonth(wrm.getMonth());
			form.setAuthFlg(wrm.getAuthFlg());

		}
		return "KK04001";
	}

	@RequestMapping(value = "operateWorkReport", params = "registRecord")
	public String insertWorkDailyReport(@Validated @ModelAttribute("KK04001Form") KK04001Form form,
			BindingResult result, Model model) {
		// データ登録に利用するドメインクラスのインスタンス化
		WorkReportDaily workReportDaily = new WorkReportDaily();
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(form, workReportDaily);
		BeanUtils.copyProperties(form, workReportMonthly);
		// 勤務表の値を一つずつ登録するため、日にち、出社時間、退社時間、休憩時間、稼働時間、時間外労働時間、備考を一つずつに区切って登録を行う。
		String[] dayList = workReportDaily.getDay().split(",", -1);
		String[] ssJknList = workReportDaily.getSsJkn().split(",", -1);
		String[] tsJknList = workReportDaily.getTsJkn().split(",", -1);
		String[] kkJknList = workReportDaily.getKkJkn().split(",", -1);
		String[] kdJknList = workReportDaily.getKdJkn().split(",", -1);
		String[] jkngiList = workReportDaily.getJkngi().split(",", -1);
		String[] biko = workReportDaily.getBiko().split(",", -1);

		for (int i = 0; i < dayList.length; i++) {
			// ログイン情報ないため、ユーザーの情報を直打ち実装
			workReportDaily.setInsUser("78");
			workReportDaily.setUpdUser("78");
			workReportDaily.setUserId("78");
			// 年と月の文字を削除する。
			workReportDaily.setYear(workReportDaily.getYear().substring(0, 4));
			workReportDaily.setMonth(workReportDaily.getMonth().substring(0, 2));
			if (Integer.parseInt(workReportDaily.getMonth()) < 10) {
				workReportDaily.setMonth(workReportDaily.getMonth().substring(0, 1));
			}
			workReportDaily.setDay(dayList[i].substring(0, 2));
			workReportDaily.setSsJkn(ssJknList[i]);
			workReportDaily.setTsJkn(tsJknList[i]);
			workReportDaily.setKkJkn(kkJknList[i]);
			workReportDaily.setKdJkn(kdJknList[i]);
			workReportDaily.setJkngi(jkngiList[i]);
			workReportDaily.setBiko(biko[i]);
			// 全て値がnullの場合は、休日として判定
			if (workReportDaily.getSsJkn().equals("") && workReportDaily.getTsJkn().equals("")
					&& workReportDaily.getKkJkn().equals("") && workReportDaily.getKdJkn().equals("")
					&& workReportDaily.getJkngi().equals("")) {
			}
			// どれか一つに入ってる場合は入力エラーとして画面に出力
			else if (workReportDaily.getSsJkn().equals("") || workReportDaily.getTsJkn().equals("")
					|| workReportDaily.getKkJkn().equals("") || workReportDaily.getKdJkn().equals("")
					|| workReportDaily.getJkngi().equals("")) {
				result.rejectValue("ssJkn", "errors.workTime");
				result.rejectValue("tsJkn", "errors.workTime");
				result.rejectValue("kdJkn", "errors.workTime");
				result.rejectValue("kkJkn", "errors.workTime");
				result.rejectValue("jkngi", "errors.workTime");
			}
			String removeColonSsJkn = workReportDaily.getSsJkn();
			String removeColonTsJkn = workReportDaily.getTsJkn();
			if (!removeColonSsJkn.isBlank() &&!removeColonTsJkn.isBlank()) {
					if (Integer.parseInt(removeColonTsJkn.replace(":", "")) < Integer
							.parseInt(removeColonSsJkn.replace(":", ""))) {
						result.rejectValue("ssJkn", "format.errors.workTime");
						result.rejectValue("tsJkn", "format.errors.workTime");
					}
				}
			
			// 定時間が未入力の場合
			if (workReportMonthly.getTeiji().equals("")) {
				result.rejectValue("teiji", "errors.teiji");
			}
			// エラーがあったら警告を出して処理を停止
			if (result.hasErrors()) {
				for (int j = 0; j < dayList.length; j++) {
					workDailyList.get(j).setDay(dayList[j]);
					workDailyList.get(j).setSsJkn(ssJknList[j]);
					workDailyList.get(j).setTsJkn(tsJknList[j]);
					workDailyList.get(j).setKkJkn(kkJknList[j]);
					workDailyList.get(j).setKdJkn(kdJknList[j]);
					workDailyList.get(j).setJkngi(jkngiList[j]);
					workDailyList.get(j).setBiko(biko[j]);
				}
				model.addAttribute("onlyDailyList", onlyDailyList);
				model.addAttribute("workDailyList", workDailyList);
				return "KK04001";
			} else {
				if (workDailyList.size() == 0) {// ない場合は登録・更新処理に進む
					// DBへ勤務情報を登録する
					dailyService.registWorkReportDaily(workReportDaily);
					System.out.println("初めての日次情報登録完了");
				} else {
					// DBへ勤務情報の更新を行う
					dailyService.updateWorkReportDaily(workReportDaily);
					System.out.println("既存の日次情報更新完了");
				}
			}
		}
		// work_report_monthlyの登録
		workReportMonthly.setYear(workReportMonthly.getYear().substring(0, 4));
		workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 2));
		if (Integer.parseInt(workReportMonthly.getMonth()) < 10)

		{
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 1));
		}
		workReportMonthly.setAuthFlg("0");
		// 直打ち項目。あとで削除
		workReportMonthly.setInsUser("78");
		workReportMonthly.setUpdUser("78");
		workReportMonthly.setUserId("78");
		if (workDailyList.size() == 0) {
			monthlyService.registWorkReportMonthly(workReportMonthly);
			System.out.println("初めての月次情報登録完了");
		} else {
			monthlyService.updateWorkReportMonthly(workReportMonthly);
			System.out.println("既存の月次情報更新完了");
		}
		return "KK02001";

	}

	// 管理者が確定ボタンを押した時の処理
	@RequestMapping(value = "operateWorkReport", params = "admin-regist")
	public String determin(KK04001Form form, Model model) {
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(form, workReportMonthly);
		// 画面からキー項目のセットを行う
		if (Integer.parseInt(workReportMonthly.getMonth()) < 10) {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 1));
		} else {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 2));
		}
		workReportMonthly.setAuthFlg("1");
		// 今は直打ち
		workReportMonthly.setInsUser("78");
		monthlyService.determineWorkReport(workReportMonthly);
		System.out.println("管理者の確定処理完了");
		return "KK03001";
	}

	// 管理者が取消ボタンを押した時の処理
	@RequestMapping(value = "operateWorkReport", params = "admin-edit")
	public String edit(KK04001Form form, Model model) {
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(form, workReportMonthly);
		// 画面からキー項目のセットを行う
		workReportMonthly.setYear(workReportMonthly.getYear().substring(0, 4));
		if (Integer.parseInt(workReportMonthly.getMonth()) < 10) {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 1));
		} else {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 2));
		}
		workReportMonthly.setAuthFlg("0");
		// 今は直打ち
		workReportMonthly.setInsUser("78");
		monthlyService.editWorkReport(workReportMonthly);
		System.out.println("管理者の取消処理完了");
		return "KK02001";
	}
}