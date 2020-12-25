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
	 * mapperを呼び出して、ログインしている社員のクリックした年月の日次の勤務状況をリストで取得する。 テーブル名：work_report_daily
	 * 
	 * @param userId ログインしている社員の社員ID
	 * @param year   月別一覧でクリックされたリンクの年
	 * @param month  月別一覧でクリックされたリンクの月 * @return
	 *               出社時間(ss_jkn)、退社時間(ts_jkn)、休憩時間(kk_jkn)、稼働時間(k_djkn)、時間外労働時間(jikangi)、備考(biko)
	 */
	public List<WorkReportDaily> findEmployeeWorkRecordDaily(String userId, String year, String month) {
		return mapper.findEmployeeWorkRecordDaily(userId, year, month);
	}

	/**
	 * 勤務日次情報の登録を行う
	 * 
	 * @param workReportDaily
	 */
	@Transactional
	public void registWorkReportDaily(WorkReportDaily workReportDaily) {
		mapper.registWorkReportDaily(workReportDaily);
	}

	/**
	 * 勤務日次情報の更新を行う
	 * 
	 * @param workReportDaily
	 */
	public void updateWorkReportDaily(WorkReportDaily workReportDaily) {
		mapper.updateWorkReportDaily(workReportDaily);
	}
}
