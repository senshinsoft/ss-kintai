package jp.co.senshinsoft.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class KinmuHokokushoController {

	@ModelAttribute(value = "inputWorkReportForm")
	public KinmuHokokushoForm workReportForm() {
		 return new KinmuHokokushoForm();
	}
}
