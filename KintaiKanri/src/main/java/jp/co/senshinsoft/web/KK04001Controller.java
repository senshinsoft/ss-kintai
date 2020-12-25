package jp.co.senshinsoft.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;
import jp.co.senshinsoft.service.ExcelBuildService;
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
	@Autowired
	private ExcelBuildService excelBuildService;
	
	private List<WorkReportDaily> workDailyList = new ArrayList<>();
	private List<String> onlyDailyList = new ArrayList<>();
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();
	private Calendar calendar = Calendar.getInstance();
	private String userId = "";
	private String year = "";
	private String month = "";
	private static final Logger logger = LoggerFactory.getLogger(KK04001Controller.class);

	@ModelAttribute(value = "KK04001form")
	public KK04001Form workReportForm() {
		return new KK04001Form();
	}

	@RequestMapping(value = "inputWorkReport")
	public String enterKK04001(KK04001Form KK04001Form, Model model, KK02001Form KK02001form, KK03001Form KK03001Form) {
		logger.info(
				"-----------------------------------------------------KK04001(勤務表報告書画面)初期表示開始-------------------------------------------------------------------------------");
		if (userInfo.getLoginUser().getAdminFlg().equals("1")) {
			String[] empIdName =KK03001Form.getEmployeeName().split(Pattern.quote("."));
			userId = empIdName[0];
			year = KK03001Form.getYear();
			month = KK03001Form.getMonth();
			// Serviceクラスを呼び出して、KK04001に必要な値を取得する。
			KK04001Form.setUserName(userService.findEmployeeName(userId));
		} else if (userInfo.getLoginUser().getAdminFlg().equals("0")) {
			userId = userInfo.getLoginUser().getUserId();
			year = KK02001form.getYear().substring(0, 4);
			month = KK02001form.getYear().substring(5, 7);
			KK04001Form.setUserName(userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei());

		}
		KK04001Form.setUseUserId(userId);
		if (userId != userInfo.getLoginUser().getUserId()) {
			KK04001Form.setUseUserId(userId);
		}
		logger.info("参照する勤務表のユーザーID：" + userId);

		
		logger.info("参照する勤務表のユーザー名：" + KK04001Form.getUserName());
		// ユーザーの日次勤務情報のリスト取得してmodelオブジェクトへ格納する。
		workDailyList = dailyService.findEmployeeWorkRecordDaily(userId, year, month);
		// DBに勤務情報がない場合、日付のリストのみを作成しKK04001へ遷移する。
		if (workDailyList.size() == 0) { // 勤務表の登録記録が0
			onlyDailyList = new ArrayList<>();
			calendar.set(Integer.parseInt(KK02001form.getYear()), Integer.parseInt(KK02001form.getMonth())-1,1);
			// 該当月の最後の日付の取得
			int last = calendar.getActualMaximum(Calendar.DATE);
			// 該当月の最初の日付を取得
			int first = calendar.getActualMinimum(Calendar.DATE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd　E");
			// 該当月の初日から月末日までを取得して、リストに加える。
			do {
				calendar.set(Calendar.DATE, first);
				onlyDailyList.add((sdf.format(calendar.getTime()).substring(8, 12)));
				first++;
			} while (first <= last);
			KK04001Form.setYear(KK02001form.getYear() + "年");
			KK04001Form.setMonth(KK02001form.getMonth() + "月度");
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
			logger.info("参照する勤務表の日時：" + KK04001Form.getWorkingDate());
		}
		model.addAttribute("userRole", userInfo.getLoginUser().getAdminFlg());
		KK04001Form.setAdminFlg(userInfo.getLoginUser().getAdminFlg());
		model.addAttribute("screenName", "勤務報告書");
		model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
		logger.info(
				"-----------------------------------------------------KK04001(勤務表報告書画面)初期表示完了-------------------------------------------------------------------------------");
		return "KK04001";
	}

	@RequestMapping(value = "operateWorkReport", params = "registRecord")
	public String insertWorkDailyReport(@Validated @ModelAttribute("KK04001Form") KK04001Form KK04001Form,
			BindingResult result, Model model, RedirectAttributes redirectAttribute) {
		logger.info(
				"---------------------------------------------------------------------勤務表登録登録開始----------------------------------------------------------------------------------------------");
		// データ登録に利用するドメインクラスのインスタンス化
		WorkReportDaily workReportDaily = new WorkReportDaily();
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(KK04001Form, workReportDaily);
		BeanUtils.copyProperties(KK04001Form, workReportMonthly);
		// 勤務表の値を一つずつ登録するため、日にち、出社時間、退社時間、休憩時間、稼働時間、時間外労働時間、備考を一つずつに区切って登録を行う。
		String[] dayList = workReportDaily.getDay().split(",", -1);
		String[] ssJknList = workReportDaily.getSsJkn().split(",", -1);
		String[] tsJknList = workReportDaily.getTsJkn().split(",", -1);
		String[] kkJknList = workReportDaily.getKkJkn().split(",", -1);
		String[] kdJknList = workReportDaily.getKdJkn().split(",", -1);
		String[] jkngiList = workReportDaily.getJkngi().split(",", -1);
		String[] biko = workReportDaily.getBiko().split(",", -1);
		
		//既に勤務表のデータが登録されていないかDBに問合せを行う
		workDailyList = dailyService.findEmployeeWorkRecordDaily(userId, year, month);

		for (int i = 0; i < dayList.length; i++) {
			// ログイン情報の取得
			workReportDaily.setInsUser(userInfo.getLoginUser().getUserId());
			workReportDaily.setUpdUser(userInfo.getLoginUser().getUserId());
			workReportDaily.setUserId(userInfo.getLoginUser().getUserId());
			// 参照している勤務表が自分のものかそうでないかを確認する
			if (KK04001Form.getUseUserId() != workReportDaily.getUserId()) {
				workReportDaily.setUserId(KK04001Form.getUseUserId());
			}
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
				result.rejectValue("kkJkn", "errors.workTime");
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
			// 定時間の文字数が3文字以上であるかどうか
			if (workReportMonthly.getTeiji().length() < 3) {
				result.rejectValue("teiji", "errors.length.teiji");
			}
			if (workReportDaily.getSsJkn().length() != 0 || workReportDaily.getTsJkn().length() != 0
					|| workReportDaily.getKkJkn().length() != 0) {
				if (workReportDaily.getSsJkn().length() < 3 || workReportDaily.getTsJkn().length() < 3
						|| workReportDaily.getKkJkn().length() < 3) {
					result.rejectValue("ssJkn", "errors.length.workTime");
					result.rejectValue("tsJkn", "errors.length.workTime");
					result.rejectValue("kkJkn", "errors.length.workTime");
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
						first++;
					} while (first <= last);
					KK04001Form.setYear(sdf.format(calendar.getTime()).substring(0, 4) + "年");
					KK04001Form.setMonth(sdf.format(calendar.getTime()).substring(5, 7) + "月度");
					model.addAttribute("onlyDailyList", onlyDailyList);
					model.addAttribute("workDailyList", workDailyList);

					// 名前を再取得する
					KK04001Form.setUserName(userService.findEmployeeName(userId));
					// フィールドの値を空にする
					KK04001Form.setSsJkn("");
					KK04001Form.setTsJkn("");
					KK04001Form.setKkJkn("");
					KK04001Form.setKdJkn("");
					KK04001Form.setJkngi("");
					KK04001Form.setBiko("");
					model.addAttribute("userRole", userInfo.getLoginUser().getAdminFlg());
					model.addAttribute("screenName", "勤務報告書");
					model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
					logger.info(
							"---------------------------------------------------------------------入力項目についてエラーあり-----------------------------------------------------------------------------------------");
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
					model.addAttribute("userRole", userInfo.getLoginUser().getAdminFlg());
					model.addAttribute("screenName", "勤務報告書");
					model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
					logger.info(
							"---------------------------------------------------------------------入力項目についてエラーあり-----------------------------------------------------------------------------------------");
					return "KK04001";
				}
			}
			if (workDailyList.size() == 0) {// ない場合は登録・更新処理に進む
				// DBへ勤務情報を登録する
				dailyService.registWorkReportDaily(workReportDaily);
			} else {
				// DBへ勤務情報の更新を行う
				dailyService.updateWorkReportDaily(workReportDaily);
			}
		}
		logger.info(
				"---------------------------------------------------------------------"+year+"年"+month+"月度の"+"日次勤務表登録・更新完了-------------------------------------------------------------------------------------------");
		logger.info(
				"---------------------------------------------------------------------"+"作業者ID："+userInfo.getLoginUser().getUserId()+"-------------------------------------------------------------------------------------------");
		logger.info(
				"---------------------------------------------------------------------"+"作業対象者ID："+KK04001Form.getUseUserId()+"-------------------------------------------------------------------------------------------");
		// work_report_monthlyの登録
		workReportMonthly.setYear(workReportMonthly.getYear().substring(0, 4));
		workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 2));
		if (Integer.parseInt(workReportMonthly.getMonth()) < 10) {
			workReportMonthly.setMonth(workReportMonthly.getMonth().substring(0, 1));
		}
		//新規登録の場合は承認済みフラグを0にセットする
		workReportMonthly.setAuthFlg("0");
		// ログイン情報の取得
		workReportMonthly.setInsUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUpdUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUserId(userInfo.getLoginUser().getUserId());
		// 参照している勤務表が自分のものかそうでないかを確認する
		if (KK04001Form.getUseUserId() != workReportMonthly.getUserId()) {
			workReportMonthly.setUserId(KK04001Form.getUseUserId());
		}
	
		if (workDailyList.size() == 0) {
			monthlyService.registWorkReportMonthly(workReportMonthly);

		} else {
			monthlyService.updateWorkReportMonthly(workReportMonthly);

		}
		logger.info(
				"---------------------------------------------------------------------"+year+"年"+month+"月度の"+"月次勤務表登録・更新完了----------------------------------------------------------------------------------------------");
		// redirectのパラメータ使用
		return "forward:/reloadKK04001";

	}

	// 管理者が確定ボタンを押した時の処理
	@RequestMapping(value = "operateWorkReport", params = "admin-regist")
	public String determine(KK04001Form KK04001Form, KK03001Form KK03001Form, Model model, SessionStatus sessionStatus) {
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(KK04001Form, workReportMonthly);
		workReportMonthly.setYear(workReportMonthly.getYear().substring(0, 4));workReportMonthly.setYear(workReportMonthly.getYear());
		workReportMonthly.setMonth(workReportMonthly.getMonth());
		workReportMonthly.setAuthFlg("1");
		workReportMonthly.setUserId(userInfo.getLoginUser().getUserId());
		workReportMonthly.setInsUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUpdUser(userInfo.getLoginUser().getUserId());
		if (KK04001Form.getUseUserId() != workReportMonthly.getUserId()) {
			workReportMonthly.setUserId(KK04001Form.getUseUserId());
		}
		monthlyService.determineWorkReport(workReportMonthly);
		logger.info(
				"---------------------------------------------------------------------勤務表の確定処理完了----------------------------------------------------------------------------------------------");
		logger.info(
				"---------------------------------------------------------------------"+"作業者ID："+userInfo.getLoginUser().getUserId()+"-------------------------------------------------------------------------------------------");
		logger.info(
				"---------------------------------------------------------------------"+"作業対象者ID："+KK04001Form.getUseUserId()+"-------------------------------------------------------------------------------------------");
		return "forward:/reloadKK04001";
	}

	// 管理者が取消ボタンを押した時の処理
	@RequestMapping(value = "operateWorkReport", params = "admin-edit")
	public String edit(KK04001Form KK04001Form, KK03001Form KK03001Form, Model model, SessionStatus sessionStatus) {
		WorkReportMonthly workReportMonthly = new WorkReportMonthly();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(KK04001Form, workReportMonthly);
		// 画面からキー項目のセットを行う
		workReportMonthly.setYear(workReportMonthly.getYear());
		workReportMonthly.setMonth(workReportMonthly.getMonth());
		workReportMonthly.setAuthFlg("0");
		workReportMonthly.setUserId(userInfo.getLoginUser().getUserId());
		workReportMonthly.setInsUser(userInfo.getLoginUser().getUserId());
		workReportMonthly.setUpdUser(userInfo.getLoginUser().getUserId());
		if (KK04001Form.getUseUserId() != workReportMonthly.getUserId()) {
			workReportMonthly.setUserId(KK04001Form.getUseUserId());
		}
		monthlyService.editWorkReport(workReportMonthly);
		logger.info(
				"---------------------------------------------------------------------勤務表の取消処理完了----------------------------------------------------------------------------------------------");
		logger.info(
				"---------------------------------------------------------------------"+"作業者ID："+userInfo.getLoginUser().getUserId()+"-------------------------------------------------------------------------------------------");
		logger.info(
				"---------------------------------------------------------------------"+"作業対象者ID："+KK04001Form.getUseUserId()+"-------------------------------------------------------------------------------------------");

		return "forward:/reloadKK04001";
	}

	@RequestMapping(value = "operateWorkReport", params = "back")
	public String getoutFromKK04001(KK04001Form KK04001Form, Model model, SessionStatus sessionStatus,
			KK03001Form KK03001Form) {
		logger.info(
				"---------------------------------------------------------------------KK03001(社員一覧画面)またはKK02001(月別一覧画面)への遷移開始----------------------------------------------------------------------------------------------");
		if (KK04001Form.getAdminFlg().equals("1")) {
			List<User> empInfoList = userService.findEmployeeCatalog();
			KK03001Form.setYear(KK03001Form.getYear().substring(0,4));
			KK03001Form.setMonth(KK03001Form.getMonth().substring(0,2));
			for (int i = 0; i < empInfoList.size(); i++) {
				KK03001Form.setEmpInfoList(empInfoList);
			}
			model.addAttribute("screenName", "社員一覧");
			model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
			sessionStatus.setComplete();
			logger.info(
					"---------------------------------------------------------------------KK03001(社員一覧画面)への遷移処理完了----------------------------------------------------------------------------------------------");
			return "KK03001";
		} else {
			sessionStatus.setComplete();
			model.addAttribute("screenName", "月別一覧");
			model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
			logger.info(
					"---------------------------------------------------------------------KK02001(月別一覧画面)への遷移処理完了----------------------------------------------------------------------------------------------");
			return "redirect:/monthlyList";
		}
	}

	
	/**
	 * excel出力ボタン押下時実行
	 * Excelファイル作成
	 * 
	 * @param KK04001Form
	 * @return HttpEntity
	 */
	@RequestMapping(value = "operateWorkReport", params = "admin-export")
	public HttpEntity downloadExcel(@Validated @ModelAttribute("KK04001Form") KK04001Form KK04001Form, BindingResult result, Model model, RedirectAttributes redirectAttribute) {
		insertWorkDailyReport(KK04001Form, result, model, redirectAttribute);

		String name[] = KK04001Form.getUserName().split(" ");

		// 作成する勤務報告書Excelファイル名設定
		String fileName = "SSI勤務報告書_" + KK04001Form.getYear() + KK04001Form.getMonth() + "_" + name[0] + name[1] + "_v102.xlsx";

		return workbookToResponseEntity(fileName, excelBuildService.getExcel(KK04001Form, workDailyList));

	}

	
	/**
	 * HttpResponse 設定 (Excelファイルをダウンロードする設定)
	 * 
	 * @param fileName
	 * @param wb
	 * @return HttpResponse
	 */
	private ResponseEntity<byte[]> workbookToResponseEntity(String fileName, Workbook wb) {
		String encodedFileName = null;
		
		try {
			encodedFileName = URLEncoder.encode(fileName, "UTF-8");
		} catch(UnsupportedEncodingException e) {
			new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			wb.write(baos);

			if(null != wb && null != baos) {
				wb.close();
				baos.close();
			}
		} catch(IOException e) {
			new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		byte[] documentContent = baos.toByteArray();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.set("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
		headers.setContentLength(documentContent.length);
		
		try{
			wb.close();
			baos.close();
		} catch (IOException e) {
			new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
	
	}
}
