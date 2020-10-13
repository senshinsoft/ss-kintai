package jp.co.senshinsoft.mapper;

import java.util.List;

import jp.co.senshinsoft.domain.WorkReportDaily;

public interface WorkReportDailyMapper {
	/**
	 * ログインしている社員のクリックした年月の日次の勤務状況をリストで返すインタフェース
	 * テーブル名：work_report_daily
	 * @param userId ログインしている社員の社員ID
	 * @param year 月別一覧でクリックされたリンクの年
	 * @param month 月別一覧でクリックされたリンクの月
	 * @return 日(day)、出社時間(ss_jkn)、退社時間(ts_jkn)、休憩時間(kk_jkn)、稼働時間(kd_jkn)、時間外労働時間(jkngi)、備考(biko)
	 */
	public List<WorkReportDaily> findEmployeeWorkRecordDaily(String userId,String year,String month);
	
}