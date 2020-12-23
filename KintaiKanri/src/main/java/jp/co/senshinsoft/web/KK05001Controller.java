package jp.co.senshinsoft.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.senshinsoft.auth.GetLoginUserDetails;

@Controller
public class KK05001Controller {
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	
	/**
	 * メニュー画面を表示する 初回ログインとパスワード更新後180日経過していた場合はパスワード変更画面へ遷移
	 * 
	 * @return KK05001、KK06001のパス
	 */
	@RequestMapping(value = "/menu")
	public String menuInput(Model model) {
		if (userInfo.getLoginUser().getPassUpdDate() != null) {
			SimpleDateFormat smdfmt = new SimpleDateFormat("yyyy-MM-dd");
			Calendar DBCalendar = Calendar.getInstance();
			Calendar nowCalendar = Calendar.getInstance();
			Date passUpdDate = new Date();
			try {
				passUpdDate = smdfmt.parse(userInfo.getLoginUser().getPassUpdDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			DBCalendar.setTime(passUpdDate);
			nowCalendar.add(Calendar.DAY_OF_MONTH, -180);
			if (DBCalendar.before(nowCalendar)) {
				model.addAttribute("screenName", "パスワード変更");
				model.addAttribute("userName",userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
				model.addAttribute("userInfo", userInfo.getLoginUser());
				return "KK06001";
			}
			System.out.println((nowCalendar.getTime()));

			model.addAttribute("screenName", "メニュー");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("userInfo", userInfo.getLoginUser());
			return "KK05001";
		}
		model.addAttribute("screenName", "パスワード変更");
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		model.addAttribute("userInfo", userInfo.getLoginUser());
		return "KK06001";
	}

	@RequestMapping(value = "/menuConf", params = "monthlyList")
	public String inputWorkReport() {
		return "redirect:/monthlyList";
	}

	@RequestMapping(value = "/menuConf", params = "pass")
	public String passChange() {
		return "redirect:/enterKK06001";
	}
	
	@RequestMapping(value = "/menuConf", params = "user")
	public String registUser() {
		return "redirect:/registUser";
	}
}
