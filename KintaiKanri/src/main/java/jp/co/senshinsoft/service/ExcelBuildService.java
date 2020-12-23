package jp.co.senshinsoft.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.server.ResponseStatusException;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;
import jp.co.senshinsoft.web.KK04001Form;

public interface ExcelBuildService {
	
	/**
	 * 勤務報告書画面用 Excelファイル作成
	 * @param KK04001Form
	 * @param workDailyList
	 * @return Workbook
	 * @throws ResponseStatusException
	 */
	Workbook getExcel(KK04001Form KK04001Form, List<WorkReportDaily> workDailyList) throws ResponseStatusException;

	/**
	 * 社員一覧画面用 Excelファイル作成
	 * @param KK03001Form
	 * @param empInfoList
	 * @return Workbook
	 * @throws ResponseStatusException
	 */
	Workbook getExcel(User user, List<WorkReportDaily> wrdList, WorkReportMonthly wrmList) throws ResponseStatusException;
}
