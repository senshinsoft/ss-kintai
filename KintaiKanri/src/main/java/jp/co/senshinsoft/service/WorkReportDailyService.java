package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.mapper.WorkReportDailyMapper;

@Service
public class WorkReportDailyService {

	@Autowired
	public WorkReportDailyMapper mapper;

	/**
	 * mapperを呼び出して、ログインしている社員のクリックした年月の日次の勤務状況をリストで返す
	 * テーブル名：work_report_daily
	 * @param userId ログインしている社員の社員ID
	 * @param year 月別一覧でクリックされたリンクの年
	 * @param month 月別一覧でクリックされたリンクの月
	 * @return 日(day)、出社時間(ss_jkn)、退社時間(ts_jkn)、休憩時間(kk_jkn)、稼働時間(kd_jkn)、時間外労働時間(jkngi)、備考(biko)
	 */
	public List<WorkReportDaily> findEmployeeWorkRecordDaily(String userId,String year, String month){
		return mapper.findEmployeeWorkRecordDaily(userId,year,month);
	}
}
