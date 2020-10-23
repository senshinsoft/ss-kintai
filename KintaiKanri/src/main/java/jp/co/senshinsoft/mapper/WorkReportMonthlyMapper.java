package jp.co.senshinsoft.mapper;

import java.util.List;

import jp.co.senshinsoft.domain.WorkReportDaily;

public interface WorkReportMonthlyMapper {
	/**
	 * @return 該当社員の該当月における勤務報告書_月次情報
	 * 取得情報：定時間、稼働時間合計、時間外合計、プロジェクト名、特記事項
	 */
	public List<WorkReportDaily> findEmployeeWorkRecordMonthly(String userId,String year,String month);
}
