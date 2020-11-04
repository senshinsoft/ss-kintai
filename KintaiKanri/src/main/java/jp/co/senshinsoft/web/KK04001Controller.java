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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.User;
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
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();
	private Calendar calendar = Calendar.getInstance();
	private String userId = "";
	private String year = "";
	private String month = "";

	@ModelAttribute(value = "KK04001form")
	public KK04001Form workReportForm() {
		return new KK04001Form();
	}

	@RequestMapping(value = "inputWorkReport")
	public String enterKK04001(KK04001Form KK04001Form, Model model, KK02001Form KK02001form, KK03001Form KK03001Form) {

		if (userInfo.getLoginUser().getAdminFlg().equals("1")) {
			String[] name = KK03001Form.getEmployeeName().split(" ");
			userId = userService.findEmployeeUserId(name[0], name[1]);
			year = KK03001Form.getYear();
			month = KK03001Form.getMonth();
		} else if (userInfo.getLoginUser().getAdminFlg().equals("0")) {
			userId = userInfo.getLoginUser().getUserId();
			year = KK02001form.getYear().substring(0, 4);
			month = KK02001form.getYear().substring(5, 7);
		}
		// Serviceクラスを呼び出して、KK04001に必要な値を取得する。
		KK04001Form.setUserName(userService.findEmployeeName(userId));
		// ユーザーの日次勤務情報のリスト取得してmodelオブジェクトへ格納する。
		workDailyList = dailyService.findEmployeeWorkRecordDaily(userId, year, month);
		// DBに勤務情報がない場合、日付のリストのみを作成しKK04001へ遷移する。
		if (workDailyList.size() == 0) { // 勤務表の登録記録が0
			onlyDailyList = new ArrayList<>();
			// 該当月の最後の日付の取得
			int last = calendar.getActualMaximum(Calendar.DATE);
			// 該当月の最初の日付を取得
			int first = calendar.getActualMinimum(Calendar.DATE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd　E");
			// 該当月の初日から月末日までを取得して、リストに加える。
			do {
				calendar.set(Calendar.DATE, first);
				onlyDailyList.add((sdf.format(calendar.getTime()).substring(8, 12)));
				KK04001Form.setYear(sdf.format(calendar.getTime()).substring(0, 4) + "年");
				KK04001Form.setMonth(sdf.format(calendar.getTime()).substring(5, 7) + "月度");
				first++;
			} while (first <= last);
			// 新規登録なので承認済みフラグを0(未承認)に設定する。
			KK04001Form.setAuthFlg("0");
			KK04001Form.setAdminFlg(userInfo.getLoginUser().getAdminFlg());
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
			} while (first <= last);
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
		}
		model.addAttribute("userRole", userInfo.getLoginUser().getAdminFlg());
		KK04001Form.setAdminFlg(userInfo.getLoginUser().getAdminFlg());
		System.out.println("adminFlg" + userInfo.getLoginUser().getAdminFlg());
		System.out.println("authFlg" + KK04001Form.getAuthFlg());
		return "KK04001";
	}

	@RequestMapping(value = "operateWorkReport", params = "registRecord")
	public String insertWorkDailyReport(@Validated @ModelAttribute("KK04001Form") KK04001Form form,
			BindingResult result, Model model, RedirectAttributes redirectAttribute) {
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
			// ログイン情報の取得
			workReportDaily.setInsUser(userInfo.getLoginUser().getUserId());
			workReportDaily.setUpdUser(userInfo.getLoginUser().getUserId());
			workReportDaily.setUserId(userInfo.getLoginUser().getUserId());

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
			// 出社時間と退社時間がどちらかが入力されている場合に、出社時間よりも退社時間が遅いかをチェック
			String removeColonSsJkn = workReportDaily.getSsJkn();
			String removeColonTsJkn = workReportDaily.getTsJkn();
			if (!removeColonSsJkn.isBlank() && !removeColonTsJkn.isBlank()) {
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
			//定時間の文字数が3文字以上であるかどうか
			if(workReportMonthly.getTeiji().length() < 3 ) {
				result.rejectValue("teiji", "errors.length.teiji");
			}
			if(workReportDaily.getSsJkn().length() != 0   || workReportDaily.getTsJkn().length() != 0 || workReportDaily.getKkJkn().length() !=0) {
			if(workReportDaily.getSsJkn().length() < 3  || workReportDaily.getTsJkn().length() < 3 || workReportDaily.getKkJkn().length() < 3)  {
				result.rejectValue("ssJkn", "errors.length.workTime");
			}
			}
			if (result.hasErrors()) {
				// 新規登録か更新によってカレンダーをもう一度取得するか判定をする。workDailyList.size==0なら新規登録
				if (workDailyList.size() == 0) {
					onlyDailyList = new ArrayList<>();
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
					} while (first <= last);
					model.addAttribute("onlyDailyList", onlyDailyList);
					model.addAttribute("userRole", userInfo.getLoginUser().getAdminFlg());
					model.addAttribute("workDailyList", workDailyList);

					// 名前を再取得する
					form.setUserName(userService.findEmployeeName(userId));
					//フィールドの値を空にする
					form.setSsJkn("");
					form.setTsJkn("");
					form.setKkJkn("");
					form.setKdJkn("");
					form.setJkngi("");
					form.setBiko("");
					return "KK04001";
				} else {
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
				}
			}
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
		// work_report_monthlyの登録
		workReportMonthly.setYear(workReportMonthly.getYear().substring(0, 4));
		workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 2));
		if (Integer.parseInt(workReportMonthly.getMonth()) < 10) {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 1));
		}
		workReportMonthly.setAuthFlg("0");
		// ログイン情報の取得
		workReportMonthly.setInsUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUpdUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUserId(userInfo.getLoginUser().getUserId());
		if (workDailyList.size() == 0) {
			monthlyService.registWorkReportMonthly(workReportMonthly);
			System.out.println("初めての月次情報登録完了");
		} else {
			monthlyService.updateWorkReportMonthly(workReportMonthly);
			System.out.println("既存の月次情報更新完了");
		}
		// redirectのパラメータ使用
		return "forward:/reroadKK04001";

	}

	// 管理者が確定ボタンを押した時の処理
	@RequestMapping(value = "operateWorkReport", params = "admin-regist")
	public String determin(KK04001Form KK04001Form, KK03001Form KK03001Form, Model model, SessionStatus sessionStatus) {
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(KK04001Form, workReportMonthly);
		// monthの値が「〜月度」となっているため漢字を除去する
		String[] month = workReportMonthly.getMonth().split("月");
		// 画面からキー項目のセットを行う
		if (Integer.parseInt(month[0]) < 10) {
			workReportMonthly.setMonth(month[0].substring(0, 1));
		} else {
			workReportMonthly.setMonth(month[0].substring(0, 2));
		}
		workReportMonthly.setAuthFlg("1");
		workReportMonthly.setUserId(userInfo.getLoginUser().getUserId());
		workReportMonthly.setInsUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUpdUser(userInfo.getLoginUser().getUserId());
		monthlyService.determineWorkReport(workReportMonthly);
		System.out.println("管理者の確定処理完了");
		return "forward:/reroadKK04001";
	}

	// 管理者が取消ボタンを押した時の処理
	@RequestMapping(value = "operateWorkReport", params = "admin-edit")
	public String edit(KK04001Form KK04001Form, KK03001Form KK03001Form, Model model, SessionStatus sessionStatus) {
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(KK04001Form, workReportMonthly);
		// 画面からキー項目のセットを行う
		workReportMonthly.setYear(workReportMonthly.getYear().substring(0, 4));
		if (Integer.parseInt(workReportMonthly.getMonth()) < 10) {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 1));
		} else {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 2));
		}
		workReportMonthly.setAuthFlg("0");
		workReportMonthly.setUserId(userInfo.getLoginUser().getUserId());
		workReportMonthly.setInsUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUpdUser(userInfo.getLoginUser().getUserId());
		monthlyService.editWorkReport(workReportMonthly);
		System.out.println("管理者の取消処理完了");
		// KK04001の初期表示で使用するパラメータをフォームに入れてfowardする
		KK03001Form.setEmployeeName(KK04001Form.getUserName());
		KK03001Form.setYear(KK04001Form.getYear());
		KK03001Form.setMonth(KK04001Form.getMonth());
		return "forward:/reroadKK04001";
	}

	@RequestMapping(value = "operateWorkReport", params = "back")
	public String getoutFromKK04001(KK04001Form KK04001Form, Model model, SessionStatus sessionStatus,
			KK03001Form KK03001Form) {
		if (KK04001Form.getAdminFlg().equals("1")) {
			List<String> employeeNameList = new ArrayList<>();
			List<User> user = userService.findEmployeeCatalog();
			KK03001Form.setYear(KK03001Form.getYear().substring(0, 4));
			KK03001Form.setMonth(KK03001Form.getMonth().substring(0, 2));
			for (int i = 0; i < user.size(); i++) {
				employeeNameList.add(user.get(i).getSei() + " " + user.get(i).getMei());
			}
			model.addAttribute("employeeNameList", employeeNameList);
			sessionStatus.setComplete();
			return "KK03001";
		} else {
			sessionStatus.setComplete();
			return "redirect:/monthlyList";
		}
	}
}
