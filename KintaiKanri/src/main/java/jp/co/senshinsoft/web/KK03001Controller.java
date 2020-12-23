package jp.co.senshinsoft.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.service.CreateZipServiceImpl;
import jp.co.senshinsoft.service.UserService;

@Controller
public class KK03001Controller {
	@Autowired
	private UserService userService;
	@Autowired
	private CreateZipServiceImpl createZipService;

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


	/**
	 * Excel出力ボタン押下時実行
	 * 社員一覧画面でチェックボックスがオンになっているユーザの勤務表をZipに固めてダウンロードする
	 * @param KK03001form
	 * @return HttpEntity
	 */
	@RequestMapping(value = "/inputWorkReport", params = "admin-export")
	public HttpEntity downloadZip(KK03001Form KK03001form, KK02001Form KK02001form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		String loginUserName = userInfo.getLoginUser().getSei() + "_" + userInfo.getLoginUser().getMei();

		// 未完成
		// 自画面遷移してエラーメッセージを出したい
		if (StringUtils.isEmpty(KK03001form.getUserId())) {
//			result.rejectValue("userId", "errors.unselected");
			redirectAttributes.addAttribute("userId", "errors.unselected");
//			return "KK03001/employeeList";

			try {
				URI location = new URI("redirect:http://localhost:8080/employeeList");
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(URI.create("redirect:/employeeList"));

				return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

			} catch (URISyntaxException e) {
				e.printStackTrace();

				return new ResponseEntity<>(HttpStatus.NOT_FOUND);

			}

		}

		return createZipService.createZipFile(loginUserName, KK03001form.getUserId(), KK03001form.getYear(), KK03001form.getMonth());
	}

}
