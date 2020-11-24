package jp.co.senshinsoft.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.service.UserService;

@Controller
public class KK06002Controller {

	@Autowired
	private UserService userService;
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	/**
	 * @return ユーザー登録画面フォーム
	 */
	@ModelAttribute("KK06002Form")
	public KK06002Form registForm() {
		return new KK06002Form();
	}

	private Map<String, String> getRadioItems() {
		Map<String, String> selectMap = new LinkedHashMap<String, String>();
		selectMap.put("1", "管理者");
		selectMap.put("0", "一般");

		return selectMap;
	}

	/**
	 * ユーザー登録画面(KK06002)の初期表示を行う
	 * 
	 * @return ユーザ登録画面パス
	 */
	@RequestMapping(value = "/menuConf", params = "user")
	public String registerInput(Model model, KK06002Form KK06002Form) {
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		model.addAttribute("screenName", "ユーザー登録");
		model.addAttribute("radioItems", getRadioItems());
		KK06002Form.setUpdateFlg("0");
		return "KK06002";
	}

	/**
	 * userテーブルへの登録を行う
	 * 
	 * @param KK06002Form
	 * @param result
	 * @param model
	 * @param attributes
	 * @return KK06002の初期表示コントローラーへのリダイレクトパス
	 */
	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "register")
	public String registUser(@Validated @ModelAttribute("KK06002Form") KK06002Form KK06002Form, BindingResult result,
			Model model, RedirectAttributes attributes) {
		User user = new User();
		BeanUtils.copyProperties(KK06002Form, user);
		String[] useUserId = KK06002Form.getUserId().split(",");
		user.setUserId(useUserId[0]);
		user.setInsUser(userInfo.getLoginUser().getUserId());
		user.setUpdUser(userInfo.getLoginUser().getUserId());

		// どれか一つでも未入力ならエラー
		if (user.getUserId().equals("") || user.getMailAddress().equals("") || user.getPassword().equals("")
				|| user.getSei().equals("") || user.getMei().equals("")) {
			result.rejectValue("regist", "errors.register");
		}
		// パスワードが7以下ならエラー
		if (KK06002Form.getPassword().length() <= 7) {
			result.rejectValue("password", "errors.password");
		}

		// 既に既存のユーザーが登録されていないかDBに問い合わせを行う
		// 重複していなければ登録する
		Boolean isValid = userService.searchUser(user);
		if (!isValid) {
			result.rejectValue("checkRepeat", "errors.repeat");
		}
		if (result.hasErrors()) {
			return "KK06002";
		}
		userService.registeringUser(user);
		attributes.addFlashAttribute("message", "登録完了しました");
		return "redirect:/menuConf?user=user";

	}

	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "back")
	public String getoutFromKK06002() {
		return "redirect:/menu";

	}

	/**
	 * 社員IDを検索して、該当の社員の情報をKK06002Formへセットして自画面遷移を行う
	 * 
	 * @param KK06002Form
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "search")
	public String searchEmployee(KK06002Form KK06002Form, BindingResult result, Model model) {
		String id = KK06002Form.getSearchEmpId();
		List<User> empList = userService.findUser(id);

		for (User u : empList) {
			KK06002Form.setUserId(u.getUserId());
			KK06002Form.setMailAddress(u.getMailAddress());
			KK06002Form.setSei(u.getSei());
			KK06002Form.setMei(u.getMei());
			KK06002Form.setPassword(u.getPassword());
			KK06002Form.setAdminFlg(u.getAdminFlg());
			KK06002Form.setUpdateFlg("1");
		}
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		model.addAttribute("screenName", "ユーザー登録");
		model.addAttribute("radioItems", getRadioItems());
		return "KK06002";
	}

	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "update")
	public String updateUser(@Validated KK06002Form KK06002Form, BindingResult result, Model model,
			RedirectAttributes attributes) {
		User user = new User();
		BeanUtils.copyProperties(KK06002Form, user);
		user.setInsUser(userInfo.getLoginUser().getUserId());
		user.setUpdUser(userInfo.getLoginUser().getUserId());

		// エラーチェックスタート	
		//アドレスに空欄がないか
		if (user.getMailAddress().equals("") ||  user.getSei().equals("") || user.getMei().equals("")) {
			result.rejectValue("sei", "errors.blank.userInfo");
			result.rejectValue("mei", "errors.blank.userInfo");
			result.rejectValue("mailAddress", "errors.blank.userInfo");
			}
		//メールアドレス被りチェック
		if(user.getMailAddress()!="") {
		List<String> addressList = userService.findMailAddress();
		for(String s:addressList) {
		if (s.equals(KK06002Form.getMailAddress())) {
			result.rejectValue("mailAddress", "errors.duplicateAddress");
			}
		}
		}
		
		if (result.hasErrors()) {
			List<User> empList = userService.findUser(KK06002Form.getUserId());
			for (User u : empList) {
				KK06002Form.setUserId(u.getUserId());
				KK06002Form.setMailAddress(u.getMailAddress());
				KK06002Form.setSei(u.getSei());
				KK06002Form.setMei(u.getMei());
				KK06002Form.setPassword(u.getPassword());
				KK06002Form.setAdminFlg(u.getAdminFlg());
				KK06002Form.setUpdateFlg("1");
			}
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("screenName", "ユーザー登録");
			model.addAttribute("radioItems", getRadioItems());
			return "KK06002";
		}

		// ユーザー情報更新
		userService.updateUser(user);

		return "redirect:/menuConf?user=user";
	}
	
	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "clear")
	public String clearKK06002(KK06002Form KK06002Form, Model model) {
		return  "redirect:/menuConf?user=user";
	}
}
