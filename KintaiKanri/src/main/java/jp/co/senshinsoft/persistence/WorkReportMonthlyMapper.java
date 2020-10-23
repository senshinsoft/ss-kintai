package jp.co.senshinsoft.persistence;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import jp.co.senshinsoft.domain.WorkReportMonthly;

public interface WorkReportMonthlyMapper {
	/**
	 * ログインしている社員のクリックした年月の月次の勤務状況をリストで返すインタフェース テーブル名：work_report_monthly
	 * 
	 * @param userId ログインしている社員の社員ID
	 * @param year   月別一覧でクリックされたリンクの年
	 * @param month  月別一覧でクリックされたリンクの月
	 * @return 定時間(tieji)、稼働時間合計(kd_jkn_kei)、時間外合計(jkngi_kei)、プロジェクト名(pj_mei)、特記事項(tokkijiko)
	 */
	public List<WorkReportMonthly> findEmployeeWorkRecordMonthly(String userId, String year, String month);

	/**
	 * 入力した勤務報告書をDBに登録するインタフェース 登録テーブル名：work_report_monthly
	 */
	public void registWorkReportMonthly(WorkReportMonthly workReportMonthly);

	/**
	 * 入力した勤務報告書をDBに更新するインタフェース 登録テーブル名：work_report_monthly
	 */
	public void updateWorkReportMonthly(WorkReportMonthly workReportMonthly);

	
	/**
	 * 管理者が社員の勤務報告を確定・取消にする際に使用する
	 * @param workReportMonthly
	 */
	
	public int changeAuthFlg(WorkReportMonthly workReportMonthly);

}