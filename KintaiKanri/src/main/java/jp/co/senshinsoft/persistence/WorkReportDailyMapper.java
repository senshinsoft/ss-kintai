package jp.co.senshinsoft.persistence;

import java.util.List;

import jp.co.senshinsoft.domain.WorkReportDaily;

public interface WorkReportDailyMapper {
	/**
	 * ログインしている社員のクリックした年月の日次の勤務状況をリストで返すインタフェース テーブル名：work_report_daily
	 * 
	 * @param userId ログインしている社員の社員ID
	 * @param year   月別一覧でクリックされたリンクの年
	 * @param month  月別一覧でクリックされたリンクの月
	 * @return 日(day)、出社時間(ss_jkn)、退社時間(ts_jkn)、休憩時間(kk_jkn)、稼働時間(kd_jkn)、時間外労働時間(jkngi)、備考(biko)
	 */
	public List<WorkReportDaily> findEmployeeWorkRecordDaily(String userId, String year, String month);

	/**
	 *  KK04001で入力された勤務表のデータのまとまり 入力した勤務報告書をDBに登録するインタフェース
	 *  登録テーブル名：work_report_daily
	 * @param workReportDaily                       
	 */
	public void registWorkReportDaily(WorkReportDaily workReportDaily);

	/**
	 * 入力した勤務報告書をDBに更新するインタフェース 登録テーブル名：work_report_daily
	 * 登録テーブル名：work_report_daily
	 * @param workReportDaily KK04001で入力された勤務表のデータのまとまり
	 */
	public void updateWorkReportDaily(WorkReportDaily workReportDaily);

}