package jp.co.senshinsoft.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.senshinsoft.auth.GetLoginUserDetails;
import jp.co.senshinsoft.domain.Location;
import jp.co.senshinsoft.domain.SiteInfo;
import jp.co.senshinsoft.domain.Supplier;
import jp.co.senshinsoft.domain.UnitInfo;
import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.service.LocationService;
import jp.co.senshinsoft.service.SiteInfoService;
import jp.co.senshinsoft.service.SiteService;
import jp.co.senshinsoft.service.SupplierService;
import jp.co.senshinsoft.service.UnitInfoService;
import jp.co.senshinsoft.service.UserService;

@Controller
public class KK06002Controller {

	@Autowired
	private UserService userService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private SiteInfoService siteInfoService;
	@Autowired
	private UnitInfoService unitInfoService;
	@Autowired
	private SiteService siteService;

	private GetLoginUserDetails userInfo = new GetLoginUserDetails();

	private Map<String, String> selectMap = new LinkedHashMap<String, String>();

	private Map<String, String> locationMap = new LinkedHashMap<>();

	private Map<String, String> supplierMap = new LinkedHashMap<>();

	private Map<String, String> userMap = new LinkedHashMap<>();

	// メールアドレスの重複チェックに使用
	private String oldMailAddress;

	/**
	 * @return ユーザー登録画面フォーム
	 */
	@ModelAttribute("KK06002Form")
	public KK06002Form registForm() {
		return new KK06002Form();
	}

	private Map<String, String> getRadioItems() {

		selectMap.put("1", "管理者");
		selectMap.put("0", "一般");

		return selectMap;
	}

	/**
	 * ユーザー登録画面(KK06002)の初期表示を行う
	 * 
	 * @return ユーザ登録画面パス
	 */
	@RequestMapping(value = "/registUser")
	public String registerInput(Model model, KK06002Form KK06002Form) {

		// 取引先のマップを作成(ロケーション・ユニット両方で使用)
		List<Supplier> supplierList = supplierService.supplierCatalog();

		for (int i = 0; i < supplierList.size(); i++) {
			supplierMap.put(supplierList.get(i).getSupplierCode(), supplierList.get(i).getSupplierName());
		}

		// ユニット情報の初期標示の処理
		List<User> userList = userService.findEmployeeCatalog();

		for (int i = 0; i < userList.size(); i++) {
			userMap.put(userList.get(i).getUserId(),
					userList.get(i).getUserId() + "：" + userList.get(i).getSei() + " " + userList.get(i).getMei());
		}
		// formとmodelに初期標示に必要な値をセットする
		KK06002Form.setLocationMap(locationMap);
		KK06002Form.setSupplierMap(supplierMap);
		KK06002Form.setUserMap(userMap);
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		model.addAttribute("screenName", "ユーザー登録");
		model.addAttribute("radioItems", getRadioItems());
		KK06002Form.setUpdateFlg("0");
		return "KK06002";
	}

	/**
	 * userテーブルへの登録を行う
	 * 
	 * @param KK06002Form
	 * @param result
	 * @param model
	 * @param attributes
	 * @return KK06002の初期表示コントローラーへのリダイレクトパス
	 */
	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "register")
	public String registUser(@Validated @ModelAttribute("KK06002Form") KK06002Form KK06002Form, BindingResult result,
			Model model, RedirectAttributes attributes) {
		User user = new User();
		SiteInfo siteInfo = new SiteInfo();
		UnitInfo unitInfo = new UnitInfo();
		BeanUtils.copyProperties(KK06002Form, user);
		BeanUtils.copyProperties(KK06002Form, siteInfo);
		BeanUtils.copyProperties(KK06002Form, unitInfo);

		// ユーザー情報の登録処理
		if (KK06002Form.getUserId().equals(",")) {
			KK06002Form.setUpdateFlg("0");
			KK06002Form.setUserId("");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("screenName", "ユーザー登録");
			model.addAttribute("radioItems", getRadioItems());
			result.rejectValue("regist", "errors.register");
			return "KK06002";
		}
		String[] useUserId = KK06002Form.getUserId().split(",");
		user.setUserId(useUserId[0]);
		user.setInsUser(userInfo.getLoginUser().getUserId());
		user.setUpdUser(userInfo.getLoginUser().getUserId());

		// どれか一つでも未入力ならエラー
		if (user.getUserId().equals("") || user.getMailAddress().equals("") || user.getPassword().equals("")
				|| user.getSei().equals("") || user.getMei().equals("") || KK06002Form.getSupplierCode().equals("")
				|| KK06002Form.getLocationCode().equals("") || KK06002Form.getTeiji().equals("")
				|| KK06002Form.getTeijiDecimalNumber().equals("") || KK06002Form.getSsJkn().equals("")
				|| KK06002Form.getTsJkn().equals("") || KK06002Form.getKkJkn().equals("")) {
			result.rejectValue("regist", "errors.register");
			String[] registUserId = KK06002Form.getUserId().split(",");
			KK06002Form.setUserId(registUserId[0]);
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("screenName", "ユーザー登録");
			KK06002Form.setUpdateFlg("0");
			KK06002Form.setLocationMap(locationMap);
			KK06002Form.setSupplierMap(supplierMap);
			KK06002Form.setUserMap(userMap);
			return "KK06002";
		}
		// パスワードが7以下ならエラー
		if (KK06002Form.getPassword().length() <= 7) {
			result.rejectValue("password", "errors.password");
			if (result.hasErrors()) {
				model.addAttribute("userName",
						userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
				model.addAttribute("screenName", "ユーザー登録");
				KK06002Form.setUpdateFlg("0");
				KK06002Form.setPassword("");
				useUserId = KK06002Form.getUserId().split(",");
				KK06002Form.setUserId(useUserId[0]);
				KK06002Form.setLocationMap(locationMap);
				KK06002Form.setSupplierMap(supplierMap);
				KK06002Form.setUserMap(userMap);

				return "KK06002";
			}
		}
		// 出社時間が退社時間より遅い場合にエラー
		String[] startWork = KK06002Form.getSsJkn().split(":");
		String[] finishWork = KK06002Form.getTsJkn().split(":");
		if (Integer.parseInt(finishWork[0]) > Integer.parseInt(startWork[0])) {
			if (Integer.parseInt(finishWork[1]) < Integer.parseInt(startWork[1])) {
				result.rejectValue("ssJkn", "errors.workTimeJkn");
				result.rejectValue("tsJkn", "errors.workTimeJkn");
			}
		} else {
			result.rejectValue("ssJkn", "errors.workTimeJkn");
			result.rejectValue("tsJkn", "errors.workTimeJkn");
		}

		if (KK06002Form.getLeaderUserId().equals("") && KK06002Form.getAssignmentLocationUserId().equals("")
				|| !KK06002Form.getLeaderUserId().equals("") && !KK06002Form.getAssignmentLocationUserId().equals("")) {

		} else {
			result.rejectValue("regist", "errors.unit");
		}
		// 既に既存のユーザーが登録されていないかDBに問い合わせを行う
		// 重複していなければ登録する
		Boolean isValid = userService.searchUser(user);
		if (!isValid) {
			result.rejectValue("userId", "errors.repeat");
			result.rejectValue("mailAddress", "errors.repeat");
		}
		if (result.hasErrors()) {
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("screenName", "ユーザー登録");
			KK06002Form.setUserId("");
			KK06002Form.setMailAddress("");
			KK06002Form.setUpdateFlg("0");
			KK06002Form.setLocationMap(locationMap);
			KK06002Form.setSupplierMap(supplierMap);
			KK06002Form.setUserMap(userMap);
			return "KK06002";
		}
		userService.registeringUser(user);

		// ロケーション情報の登録
		useUserId = siteInfo.getUserId().split(",");
		siteInfo.setUserId(useUserId[0]);
		siteInfoService.registSiteInfo(siteInfo);
		// ユニット情報の登録
		unitInfo.setSupplierCode(KK06002Form.getAssignmentLocationUserId());
		unitInfo.setMemberUserId(useUserId[0]);

		unitInfoService.registUnitInfo(unitInfo);

		attributes.addFlashAttribute("message", "登録完了しました");
		return "redirect:/menuConf?user=user";

	}

	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "back")
	public String getoutFromKK06002() {
		return "redirect:/menu";

	}

	/**
	 * 社員IDを検索して、該当の社員の情報をKK06002Formへセットして自画面遷移を行う
	 * 
	 * @param KK06002Form
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "search")
	public String searchEmployee(KK06002Form KK06002Form, BindingResult result, Model model) {
		String id = KK06002Form.getSearchEmpId();
		if (id.equals("")) {
			KK06002Form.setUpdateFlg("0");
			KK06002Form.setUserId("");
			KK06002Form.setMailAddress("");
			KK06002Form.setPassword("");
			KK06002Form.setSei("");
			KK06002Form.setMei("");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("screenName", "ユーザー登録");
			model.addAttribute("radioItems", getRadioItems());
			return "KK06002";
		}
		List<User> empList = userService.findUser(id);
		if (empList.size() == 0) {
			KK06002Form.setUpdateFlg("0");
			KK06002Form.setUserId("");
			KK06002Form.setMailAddress("");
			KK06002Form.setPassword("");
			KK06002Form.setSei("");
			KK06002Form.setMei("");
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("screenName", "ユーザー登録");
			model.addAttribute("radioItems", getRadioItems());
			result.rejectValue("regist", "error.noEmployee");
			return "KK06002";
		}

		for (User u : empList) {
			KK06002Form.setUserId(u.getUserId());
			KK06002Form.setMailAddress(u.getMailAddress());
			KK06002Form.setSei(u.getSei());
			KK06002Form.setMei(u.getMei());
			KK06002Form.setPassword(u.getPassword());
			KK06002Form.setAdminFlg(u.getAdminFlg());
			KK06002Form.setUpdateFlg("1");
			oldMailAddress = u.getMailAddress();
		}
		List<SiteInfo> siteList = siteInfoService.findSiteInfo(KK06002Form.getSearchEmpId());
		for (SiteInfo s : siteList) {
			KK06002Form.setSupplierCode(s.getSupplierCode());
			KK06002Form.setLocationCode(s.getLocationCode());
			KK06002Form.setUserId(s.getUserId());
			KK06002Form.setTeiji(s.getTeiji());
			KK06002Form.setTeijiDecimalNumber(s.getTeijiDecimalNumber());
			KK06002Form.setSsJkn(s.getSsJkn());
			KK06002Form.setTsJkn(s.getTsJkn());
			KK06002Form.setKkJkn(s.getKkJkn());
		}
		List<UnitInfo> unitList = unitInfoService.findUnitInfo(KK06002Form.getSearchEmpId());
		for (UnitInfo u : unitList) {
			KK06002Form.setLeaderUserId(u.getLeaderUserId());
			KK06002Form.setSupplierCode(u.getSupplierCode());
			KK06002Form.setAssignmentLocationUserId(u.getSupplierCode());
		}

		// 取引先のマップを作成(ロケーション・ユニット両方で使用)
		List<Supplier> supplierList = supplierService.supplierCatalog();

		for (int i = 0; i < supplierList.size(); i++) {
			supplierMap.put(supplierList.get(i).getSupplierCode(), supplierList.get(i).getSupplierName());
		}

		// ユニット情報の初期標示の処理
		List<User> userList = userService.findEmployeeCatalog();

		for (int i = 0; i < userList.size(); i++) {
			userMap.put(userList.get(i).getUserId(),
					userList.get(i).getUserId() + "：" + userList.get(i).getSei() + " " + userList.get(i).getMei());
		}

		KK06002Form.setLocationMap(locationMap);
		KK06002Form.setSupplierMap(supplierMap);
		KK06002Form.setUserMap(userMap);
		model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
		model.addAttribute("screenName", "ユーザー登録");
		model.addAttribute("radioItems", getRadioItems());
		return "KK06002";
	}

	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "update")
	public String updateUser(@Validated KK06002Form KK06002Form, BindingResult result, Model model,
			RedirectAttributes attributes) {
		User user = new User();
		SiteInfo siteInfo = new SiteInfo();
		UnitInfo unitInfo = new UnitInfo();
		BeanUtils.copyProperties(KK06002Form, user);
		BeanUtils.copyProperties(KK06002Form, siteInfo);
		BeanUtils.copyProperties(KK06002Form, unitInfo);
		user.setInsUser(userInfo.getLoginUser().getUserId());
		user.setUpdUser(userInfo.getLoginUser().getUserId());

		// エラーチェックスタート
		// アドレスに空欄がないか
		if (user.getMailAddress().equals("") || user.getSei().equals("") || user.getMei().equals("")) {
			result.rejectValue("sei", "errors.blank.userInfo");
			result.rejectValue("mei", "errors.blank.userInfo");
			result.rejectValue("mailAddress", "errors.blank.userInfo");
		}
		// メールアドレス被りチェック
		if (user.getMailAddress() != "") {

			List<String> addressList = userService.findMailAddress();
			for (String s : addressList) {
				if (s.equals(KK06002Form.getMailAddress())) {
					if (!oldMailAddress.equals(KK06002Form.getMailAddress())) {
						result.rejectValue("mailAddress", "errors.duplicateAddress");
					}
				}
			}
		}
		if (KK06002Form.getSupplierCode().equals("") || KK06002Form.getLocationCode().equals("")
				|| KK06002Form.getTeiji().equals("") || KK06002Form.getTeijiDecimalNumber().equals("")
				|| KK06002Form.getSsJkn().equals("") || KK06002Form.getTsJkn().equals("")
				|| KK06002Form.getKkJkn().equals("")) {
			result.rejectValue("regist", "errors.register");
		}
		if (KK06002Form.getLeaderUserId().equals("") && KK06002Form.getAssignmentLocationUserId().equals("")
				|| !KK06002Form.getLeaderUserId().equals("") && !KK06002Form.getAssignmentLocationUserId().equals("")) {

		} else {
			result.rejectValue("regist", "errors.unit");
		}
		if (result.hasErrors()) {
			List<User> empList = userService.findUser(KK06002Form.getUserId());
			for (User u : empList) {
				KK06002Form.setUserId(u.getUserId());
				KK06002Form.setMailAddress(u.getMailAddress());
				KK06002Form.setSei(u.getSei());
				KK06002Form.setMei(u.getMei());
				KK06002Form.setPassword(u.getPassword());
				KK06002Form.setAdminFlg(u.getAdminFlg());
				KK06002Form.setUpdateFlg("1");
			}
			model.addAttribute("userName", userInfo.getLoginUser().getSei() + " " + userInfo.getLoginUser().getMei());
			model.addAttribute("screenName", "ユーザー登録");
			model.addAttribute("radioItems", getRadioItems());
			KK06002Form.setLocationMap(locationMap);
			KK06002Form.setSupplierMap(supplierMap);
			KK06002Form.setUserMap(userMap);

			return "KK06002";
		}

		// ユーザー情報更新
		userService.updateUser(user);
		// サイト情報更新
		siteInfoService.updateSiteInfo(siteInfo);
		// ユニット情報更新
		unitInfo.setSupplierCode(KK06002Form.getAssignmentLocationUserId());
		unitInfo.setMemberUserId(KK06002Form.getUserId());
		unitInfoService.updateSiteInfo(unitInfo);
		return "redirect:/menuConf?user=user";
	}

	@RequestMapping(value = "/registerConf", method = RequestMethod.POST, params = "clear")
	public String clearKK06002(KK06002Form KK06002Form, Model model) {
		return "redirect:/menuConf?user=user";
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public String getSelectData(KK06002Form KK06002Form, ModelMap model) {
		locationMap.clear();
		List<String> selectDataList = siteService.findLocatist(KK06002Form.getSupplierCode());
		for (String s : selectDataList) {
			List<Location> locationList = locationService.findLocationInfo(s);
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getLocationCode(), locationList.get(i).getLocationName());

			}
		}
		model.addAttribute("locationMap", locationMap);
		return "KK06002::selectAjax";

	}

}
