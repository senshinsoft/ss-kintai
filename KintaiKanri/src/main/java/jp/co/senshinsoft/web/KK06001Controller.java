package jp.co.senshinsoft.web;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.service.UserService;

@Controller
public class KK06001Controller {
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	/**
	 * @return パスワード変更画面フォーム
	 */
	@ModelAttribute("KK06001Form")
	public KK06001Form changePasswordForm() {
		return new KK06001Form();
	}
	
	/**
	 *パスワード変更画面初期表示 
	 */
	@RequestMapping(value="enterKK06001")
	public String enterKK06001(KK06001Form form, Model model) {
		model.addAttribute("screenName", "パスワード変更");
		model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
		model.addAttribute("userInfo", userInfo.getLoginUser());	
		return "KK06001";
	}
	
	/**
	 * パスワード更新処理を行う
	 */
	@RequestMapping(value = "/updatePassword", params = "update")
	public String updateChangePassword(Model model, KK06001Form form, BindingResult result) {
		User user = new User();
	String encodeNewPassword =passwordEncoder.encode(form.getNewPassword());
	if(form.getPassword().equals("")|| form.getConfPassword().equals("") || form.getConfPassword().equals("")) {
		result.rejectValue("regist","errors.register");
		model.addAttribute("screenName", "パスワード変更");
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		return "KK06001";
	}
		// 現在のパスワードが一致しているか
		if (!passwordEncoder.matches(form.getPassword(), userInfo.getLoginUser().getPassword())) {
			result.rejectValue("password", "errors.vaild.nowPassword");
			model.addAttribute("screenName", "パスワード変更");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			return "KK06001";
		}
		// 新しいパスワードが8文字以上であるか
		if (form.getNewPassword().length() <= 7) {
			result.rejectValue("newPassword", "errors.password");
			model.addAttribute("screenName", "パスワード変更");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			return "KK06001";
		}
		// 現在のパスワードと新しいパスワードが一致していないか
		if (form.getPassword().equals(form.getNewPassword())) {
			result.rejectValue("newPassword", "errors.samePassword");
			model.addAttribute("screenName", "パスワード変更");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			return "KK06001";
		}
		// 新しいパスワードと確認用パスワードが一致しているか
		if (!form.getNewPassword().equals( form.getConfPassword())) {
			result.rejectValue("confPassword", "errors.confPassword");
			model.addAttribute("screenName", "パスワード変更");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			return "KK06001";
		}
		if (result.hasErrors()) {
			model.addAttribute("screenName", "パスワード変更");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			return "KK06001";
		}
		BeanUtils.copyProperties(form, user);
		user.setPassword(encodeNewPassword);
		user.setUserId(userInfo.getLoginUser().getUserId());
		user.setUpdUser(userInfo.getLoginUser().getUserId());
		userService.updateUserPass(user);
		System.out.println("パスワード変更完了");
		return "redirect:/enterKK06001";
	}
	
	/**
	 * メニュー画面へリダイレクトする
	 * @return
	 */
	@RequestMapping(value = "/updatePassword",  params = "back")
	public String getoutFromKK06001() {
		return "redirect:/menu";

	}
}