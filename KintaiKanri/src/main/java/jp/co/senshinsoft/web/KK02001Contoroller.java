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
import jp.co.senshinsoft.domain.User;

@Controller
public class KK02001Contoroller {

	@ModelAttribute("KK02001Form")
	public KK02001Form setForm() {
		return new KK02001Form();
	}

		
	//12か月分の年月を取得
	@RequestMapping(value="/monthlyList", method = RequestMethod.GET)
	public String workCalendar(@ModelAttribute("KK02001Form") KK02001Form form, Model model) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月");
		List<String> list = new ArrayList<String>();
		for(int i = 0; i <= 11 ; i++) {
			
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH,-i);

			Date date = cal.getTime();

			list.add(sdf.format(date));
			

				model.addAttribute("list",list);
				
			
		}

		return "KK02001";
	}

//	//月別一覧から社員一覧または勤務表に遷移
//	@RequestMapping("/next")
//	public String next(@ModelAttribute("GetLoginUserDetails") GetLoginUserDetails getdb,Model model) {
//		User user = getdb.getLoginUser();
//		
//		if(user.getAdminFlg().equals("1")) {
//			//社員一覧（管理者）
//			return "KK03001";
//		}else if(user.getAdminFlg().equals("0")) {
//			//勤務表（	一般社員）
//			return "KK04001";
//		}else {
//		return null;
//		}
//	}

	//社員一覧へ遷移（管理者）
	@RequestMapping("/employeeList")
		public String EmplyeeList() {
			return "KK03001";
		}
	
	
	//勤務報告書へ遷移（一般社員）
	@RequestMapping("/workReport")
		public String WorkReport(){
			return "KK04001"; 
	}


}