package jp.co.senshinsoft.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.senshinsoft.auth.GetLoginUserDetails;

@Controller
public class KK02001Controller {
	private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	@ModelAttribute("KK02001Form")
	public KK02001Form setForm() {
		return new KK02001Form();
	}

	/**
	 * 現在月から過去12ヶ月のリストをYYYY年MM月の形で表示する
	 * 
	 * @return KK02001のパス
	 */
	@RequestMapping(value = "/monthlyList", method = RequestMethod.GET)
	public String workCalendar(@ModelAttribute("KK02001Form") KK02001Form form, Model model) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		List<String> oneYearCalendarList = new ArrayList<String>();
		for (int i = 0; i <= 11; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			Date date = cal.getTime();
			oneYearCalendarList.add(sdf.format(date));
			model.addAttribute("oneYearCalendarList", oneYearCalendarList);
		}
		model.addAttribute("userInfo", userInfo.getLoginUser());
		model.addAttribute("screenName", "月別一覧");
		model.addAttribute("userName",userInfo.getLoginUser().getSei()+" "+userInfo.getLoginUser().getMei() );
		return "KK02001";
	}

	@RequestMapping(value = "/menu", params = "back")
	public String backMenu() {
		return "redirect:/menu";
	}
}