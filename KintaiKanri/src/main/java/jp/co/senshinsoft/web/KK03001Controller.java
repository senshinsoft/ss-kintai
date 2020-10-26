package jp.co.senshinsoft.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.service.UserService;


@Controller
public class KK03001Controller {
	@Autowired
	private UserService userService;
	
	// 社員一覧へ遷移（管理者）
	@RequestMapping("/employeeList")
	public String empInput(Model model,KK03001Form KK03001form,KK02001Form KK02001form) {
		List<String> employeeNameList = new ArrayList<>();
		List<User> user = userService.findEmployeeCatalog();
		KK03001form.setYear(KK02001form.getYear().substring(0,4));
		KK03001form.setMonth(KK02001form.getYear().substring(5,7));
		for(int i=0;  i <user.size(); i++) {
			employeeNameList.add(user.get(i).getSei()+" "+user.get(i).getMei());
		}
		model.addAttribute("employeeNameList", employeeNameList);
		return "KK03001";
	}

	//月別一覧へ戻る
	@RequestMapping(value="/inputWorkReport",  params = "back")
	public String backEmployeeList(KK03001Form KK03001form,KK02001Form KK02001form) {
		return "redirect:/monthlyList";
	}
	
	
}
