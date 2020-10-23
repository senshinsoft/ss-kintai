package jp.co.senshinsoft.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.auth.LoginUserDetails;
import jp.co.senshinsoft.domain.User;

@Controller
public class KK02001Controller {

private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	@ModelAttribute("KK02001Form")
	public KK02001Form setForm() {
		return new KK02001Form();
	}

	// 12か月分の年月を取得
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
			model.addAttribute("userInfo", userInfo.getLoginUser());
		}
		return "KK02001";
	}

	// 社員一覧へ遷移（管理者）
	@RequestMapping("/employeeList")
	public String EmplyeeList() {
		return "KK03001";
	}
}