package jp.co.senshinsoft.mapper;

import java.util.List;

import jp.co.senshinsoft.domain.WorkReportDaily;

public interface WorkReportDailyMapper {
	/**
	 * @return 社員の該当月における勤務記録一覧
	 * 取得情報：日、出社時間、退社時間、休憩時間、稼働時間、時間外労働時間、備考
	 */
	public List<WorkReportDaily> findEmployeeWorkRecordDaily(String userId,String year,String month);
	
}