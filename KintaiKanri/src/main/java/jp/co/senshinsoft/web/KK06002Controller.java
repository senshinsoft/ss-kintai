package jp.co.senshinsoft.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.senshinsoft.domain.RegisteringUser;
import jp.co.senshinsoft.service.RegisteringUserService;



@Controller
public class KK06002Controller {
	
	@Autowired
    private RegisteringUserService registerSrvice;

	
	/**
	 * @return ユーザー登録画面フォーム
	 */
	@ModelAttribute("KK06002Form")
	public KK06002Form regusterForm() {
		return new KK06002Form();
	}

	private Map<String,String> getRadioItems(){	
		Map<String,String> selectMap = new LinkedHashMap<String,String>();
		  selectMap.put("1", "管理者");
		  selectMap.put("0", "一般");
		  return selectMap;
	}
	
	
	/**
	 * @return ユーザ登録画面パス
	 */
	@RequestMapping(value = "/menuConf",params="user")
	public String registerInput(Model model,KK06002Form form) {
		
		model.addAttribute("radioItems",getRadioItems());
			
		return "KK06002";
	}
	
	
	
	@RequestMapping(value = "/registerConf",method = RequestMethod.POST,params="register")
	public String registUser(@Validated @ModelAttribute("KK06002Form")KK06002Form form,BindingResult result,Model model
			, RedirectAttributes attributes){
		RegisteringUser registeringUser = new RegisteringUser();		
		BeanUtils.copyProperties(form, registeringUser);
			
			// どれか一つでも未入力ならエラー
			if(registeringUser.getUserId().equals("")
				|| registeringUser.getMailAddress().equals("")
				|| registeringUser.getPassword().equals("")
				|| registeringUser.getSei().equals("")
				|| registeringUser.getMei().equals("")) {
				result.rejectValue("regist", "errors.register");
			}
            // パスワードが7以下ならエラー
			 if(form.getPassword().length() <= 7) {
			    	result.rejectValue("password", "errors.password");
			    }
			 
			
		  //既に既存のユーザーが登録されていないかDBに問い合わせを行う
		  //重複していなければ登録する
			Boolean isValid = registerSrvice.searchUser(registeringUser);
			if(!isValid) { 
				result.rejectValue("checkRepeat","errors.repeat");
		    }
		    if(result.hasErrors()){
				 return "KK06002";
			 }
	
		  registerSrvice.registeringUser(registeringUser);
		
		
		attributes.addFlashAttribute("message","登録完了しました");
		return "redirect:/menuConf?user=user";
		
		
	}
	
	
	@RequestMapping(value = "/registerConf",method = RequestMethod.POST,params="back")
	public String getoutFormKK06002(){
	   return "redirect:/menu";		
	
	}
	
	

}
