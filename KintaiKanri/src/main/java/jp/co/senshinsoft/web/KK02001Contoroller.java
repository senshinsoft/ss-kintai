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

public class KK02001Contoroller {

	@ModelAttribute
	public KK02001Form setForm() {
		return new KK02001Form();
	}
	
	@RequestMapping("/KK01001")
	public String menu(Model model){
		KK02001Form form = new KK02001Form();
		model.addAttribute("KK02001Form",form);
		return "KK01001";
	}
	
	//12か月分の年月を取得
	@RequestMapping("/KK02001")
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
		model.addAttribute("id",form.getId());
		return "/KK02001";
	}

	//月別一覧から社員一覧または勤務表に遷移
	@RequestMapping("next")
	public String next(@ModelAttribute("KK02001Form") KK02001Form form,Model model) {
		
		if(form.getId() == 1 ) {
			//社員一覧（管理者）
			return "/KK03001";
		}else {
			//勤務表（	一般社員）
			return "/KK04001";
		}
	}
}