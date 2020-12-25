package jp.co.senshinsoft.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;

public interface CreateZipService {

	/**
	 * 社員一覧画面でチェックボックスを選択した社員の勤務表(Excelファイル)をzipに固めてダウンロードする
	 * 
	 * @param loginUserName		ログインユーザの名前 (姓_名)
	 * @param userIds					ダウンロード対象者のユーザID
	 * @param year						ダウンロード対象の年
	 * @param month					ダウンロード対象の月
	 * @return HttpEntity				HttpResponse情報
	 */
	HttpEntity createZipFile(String loginUserName, String userIds, String year, String month);

	
	/**
	 *  zipファイル作成用一時フォルダの作成
	 * @return 作成したフォルダのパス
	 */
	String createFolder(String loginUserName);

	
	/**
	 * HttpResponse 設定 (zipファイルをダウンロードする設定)
	 * @param folderPath
	 * @param zipFileName
	 * @return HttpResponse
	 */
	ResponseEntity<byte[]> ResponseEntity(String folderPath, String zipFileName);

}
