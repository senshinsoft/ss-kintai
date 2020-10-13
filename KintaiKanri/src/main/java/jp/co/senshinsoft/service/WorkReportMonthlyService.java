package jp.co.senshinsoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.mapper.WorkReportMonthlyMapper;

@Service
public class WorkReportMonthlyService {

	@Autowired
	public WorkReportMonthlyMapper mapper;

	/**
	 * mapperを呼び出して、ログインしている社員のクリックした年月の月次の勤務状況をリストで返す
	 * テーブル名：work_report_monthly
	 * @param userId ログインしている社員の社員ID
	 * @param year 月別一覧でクリックされたリンクの年
	 * @param month 月別一覧でクリックされたリンクの月
	 * @return 定時間(tieji)、稼働時間合計(kd_jkn_kei)、時間外合計(jkngi_kei)、プロジェクト名(pj_mei)、特記事項(tokkijiko)
	 */
	public List<WorkReportDaily> findEmployeeWorkRecordMonthly(String userId,String year, String month){
		return mapper.findEmployeeWorkRecordMonthly(userId,year,month);
	}
}
