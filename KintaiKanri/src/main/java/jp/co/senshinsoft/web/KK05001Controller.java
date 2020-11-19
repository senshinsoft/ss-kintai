package jp.co.senshinsoft.web;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.senshinsoft.auth.GetLoginUserDetails;

@Controller
public class KK05001Controller {
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	/**
	 * @return パスワード変更画面フォーム
	 */
	@ModelAttribute("KK06001Form")
	public KK06001Form changePasswordForm() {
		return new KK06001Form();
	}
	
	/**
	 * メニュー画面を表示する
	 * 初回ログインとパスワード更新後180日経過していた場合はパスワード変更画面へ遷移
	 * @return KK05001、KK06001のパス
	 */
	@RequestMapping(value = "/menu")
	public String menuInput(Model model) {
		if(userInfo.getLoginUser().getPassUpdDate() != null) {
		model.addAttribute("screenName", "メニュー");
		model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
		model.addAttribute("userInfo", userInfo.getLoginUser());	
		return "KK05001";
	}
		model.addAttribute("screenName", "パスワード変更");
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		model.addAttribute("userInfo", userInfo.getLoginUser());
		return 	"KK06001";	
	}
	
	@RequestMapping(value = "/menuConf", params="monthlyList")
	public String inputWorkReport(){
		return "redirect:/monthlyList";
	}
	@RequestMapping(value = "/menuConf", params="pass")
	public String passChange(){	
		return "redirect:/enterKK06001";
	}
		
}
