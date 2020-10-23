package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;
import jp.co.senshinsoft.persistence.WorkReportMonthlyMapper;

@Service
public class WorkReportMonthlyService {

	@Autowired
	public WorkReportMonthlyMapper mapper;

	/**
	 * mapperを呼び出して、ログインしている社員のクリックした年月の月次の勤務状況をリストで取得する。 テーブル名：work_report_monthly
	 * 
	 * @param userId ログインしている社員の社員ID
	 * @param year   月別一覧でクリックされたリンクの年
	 * @param month  月別一覧でクリックされたリンクの月
	 * @return 定時間(tieji)、稼働時間合計(kd_jkn_kei)、時間外合計(jkngi_kei)、プロジェクト名(pj_mei)、特記事項(tokkijiko)
	 */
	public List<WorkReportMonthly> findEmployeeWorkRecordMonthly(String userId, String year, String month) {
		return mapper.findEmployeeWorkRecordMonthly(userId, year, month);
	}

	/**
	 * 入力した勤務情報をwork_report_monthlyテーブルへ登録する。
	 * 
	 * @param workReportMonthly KK040001で入力された値
	 */
	public void registWorkReportMonthly(WorkReportMonthly workReportMonthly) {
		mapper.registWorkReportMonthly(workReportMonthly);
	}

	/**
	 * 入力した勤務情報をwork_report_monthlyテーブルへ更新する。
	 * 
	 * @param workReportMonthly KK040001で入力された値
	 */
	public void updateWorkReportMonthly(WorkReportMonthly workReportMonthly) {
		mapper.updateWorkReportMonthly(workReportMonthly);
	}
	
	/**
	 * 管理者が勤務報告の確定を行うメソッド。work_report_monthlyテーブルのauth_flg="1"に変更する
	 * 
	 * @param workReportMonthly
	 */
	public void determineWorkReport(WorkReportMonthly workReportMonthly) {
		int i = mapper.changeAuthFlg(workReportMonthly);
		System.out.println("更新件数："+i);
	}

	/**
	 * 管理者が勤務報告の確定を行うメソッド。work_report_monthlyテーブルのauth_flg="0"に変更する
	 * 
	 * @param workReportMonthly
	 */
	@Transactional
	public void editWorkReport(WorkReportMonthly workReportMonthly) {
		mapper.changeAuthFlg(workReportMonthly);
	}
}
