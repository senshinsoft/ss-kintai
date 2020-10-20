package jp.co.senshinsoft.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ログイン画面コントローラ
 * @author takada
 */
@Controller
public class KK01001Controller {

	/**
	 * @return ログイン画面フォーム
	 */
	@ModelAttribute("KK01001Form")
	public KK01001Form loginForm() {
		return new KK01001Form();
	}

	/**
	 * @return ログイン画面パス
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "KK01001";
	}

	/**
	 * @param form
	 * @param result
	 * @return 遷移先画面パス（月別一覧画面）
	 */
	@RequestMapping(value = "/loginConf", method = RequestMethod.POST)
	public String loginConf(@Validated @ModelAttribute("KK01001Form") KK01001Form form, BindingResult result, Model model) {
		if(result.hasErrors()) {
//			List<ObjectError> errorList = result.getAllErrors();
//			model.addAttribute("errorList", errorList);

			return "KK01001";
		}
		
//		return "KK01002";
		return "KK02001";

	}
	
}