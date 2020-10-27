package jp.co.senshinsoft.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommonController {

	/**
	 * ログアウトしてKK01001(ログイン画面)に戻る
	 * @return KK01001のパス
	 */
	@RequestMapping(value="/logout",  params = "logout", method = RequestMethod.GET)
	public String logout() {
		return "redirect:/login?logout";
	}
}
