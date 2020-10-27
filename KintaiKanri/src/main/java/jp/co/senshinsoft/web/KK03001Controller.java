package jp.co.senshinsoft.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.service.UserService;


@Controller
public class KK03001Controller {
	@Autowired
	private UserService userService;
	
	/**
	 * 社員一覧へ遷移（管理者）
	 * @param model 社員一覧の名前を入れるモデル
	 * @param KK03001form KK02001(月別一覧)画面で取得した年月
	 * @param KK02001form クリックされた年月の値
	 * @return KK03001のパス
	 */
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

	/**
	 * KK02001(月別一覧)画面へ戻る
	 * @return
	 */
	@RequestMapping(value="/inputWorkReport",  params = "back")
	public String backEmployeeList() {
		return "redirect:/monthlyList";
	}
	
	/**
	 * ログアウトしてKK01001(ログイン画面)に戻る
	 * @return KK01001のパス
	 */
	@RequestMapping(value="/logout",  params = "logout", method = RequestMethod.GET)
	public String logout() {
		return "redirect:/login?logout";
	}
}
