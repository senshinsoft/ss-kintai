package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.persistence.WorkReportDailyMapper;

@Service
public class WorkReportDailyService {

	@Autowired
	public WorkReportDailyMapper mapper;

	/**
	 * mapperを呼び出して、ログインしている社員のクリックした年月の日次の勤務状況をリストで取得する。
	 * テーブル名：work_report_daily
	 * @param userId ログインしている社員の社員ID
	 * @param year 月別一覧でクリックされたリンクの年
	 * @param month 月別一覧でクリックされたリンクの月
	 */
	public void findEmployeeWorkRecordDaily(String userId,String year, String month){
		List<WorkReportDaily> dailyList = mapper.findEmployeeWorkRecordDaily(userId,year,month);
	}
}
