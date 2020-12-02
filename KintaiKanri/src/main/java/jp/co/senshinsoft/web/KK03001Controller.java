package jp.co.senshinsoft.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.service.UserService;

@Controller
public class KK03001Controller {
	@Autowired
	private UserService userService;
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	@ModelAttribute(value = "KK03001Form")
	public KK03001Form empForm() {
		return new KK03001Form();
	}

	/**
	 * 社員一覧へ遷移（管理者）
	 * 
	 * @param model       社員一覧の名前を入れるモデル
	 * @param KK03001form KK02001(月別一覧)画面で取得した年月
	 * @param KK02001form クリックされた年月の値
	 * @return KK03001のパス
	 */
	@RequestMapping("/employeeList")
	public String empInput(Model model, KK03001Form KK03001form, KK02001Form KK02001form) {
		List<User> empInfoList = userService.findEmployeeCatalog();
		KK03001form.setYear(KK02001form.getYear().substring(0, 4));
		KK03001form.setMonth(KK02001form.getYear().substring(5, 7));
		KK03001form.setEmpInfoList(empInfoList);
		model.addAttribute("screenName", "社員一覧");
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		return "KK03001";
	}

	/**
	 * KK02001(月別一覧)画面へ戻る
	 * 
	 * @return
	 */
	@RequestMapping(value = "/inputWorkReport", params = "back")
	public String backEmployeeList() {
		return "redirect:/monthlyList";
	}

}
