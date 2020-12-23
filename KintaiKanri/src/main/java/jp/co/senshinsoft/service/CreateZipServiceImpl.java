package jp.co.senshinsoft.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;

/**
 * Zip作成
 * @author takada
 *
 */
@Service
public class CreateZipServiceImpl implements CreateZipService {
	@Autowired
	private ExcelBuildService excelBuildService;
	@Autowired
	private UserService userService;
	@Autowired
	private WorkReportDailyService dailyService;
	@Autowired
	private WorkReportMonthlyService monthlyService;


	public HttpEntity createZipFile(String loginUserName, String userIds, String year, String month) {
		String[] userId = userIds.split(",");
		User user = null;
		List<WorkReportDaily> wrdList = null;
		List<WorkReportMonthly> wrmList = null;
		String folderPath = createFolder(loginUserName);
		String filePath = null;
		String fileName = null;
		String zipFileName = "SSI勤務報告書_" + year + month + ".zip";
		Workbook wb;
		FileOutputStream fos = null;
		FileOutputStream zipFos = null;
		ZipOutputStream zipZos = null;

		if(folderPath.isEmpty()) {
			return ResponseEntity.badRequest().body("bad make folder");
		}

		try {
			for(String userIdTmp : userId) {
				if(!userIdTmp.isEmpty()) {
					user = userService.findAccountByUserId(userIdTmp);
					wrdList = dailyService.findEmployeeWorkRecordDaily(userIdTmp, year, month);
					wrmList = monthlyService.findEmployeeWorkRecordMonthly(userIdTmp, year, month);
	
					// 勤務報告書ファイル名と作成するパスの設定
					fileName = "SSI勤務報告書_" + year + month + "_" + user.getSei() + user.getMei() + "_v102.xlsx";
					filePath = folderPath + File.separator + fileName;
					byte[] buffer = new byte[1024];
	
					wb = excelBuildService.getExcel(user, wrdList, wrmList.get(0));
					fos = new FileOutputStream(filePath);
					wb.write(fos);

					if(zipFos == null) {
						zipFos = new FileOutputStream(folderPath + File.separator + zipFileName);
						zipZos = new ZipOutputStream(zipFos, Charset.forName("MS932"));
					}

					zipZos.putNextEntry(new ZipEntry(fileName));
					FileInputStream fis = new FileInputStream(filePath);
					
					int i;
					while((i = fis.read(buffer)) > 0) {
						zipZos.write(buffer, 0, i);
					}

					try {
						if(fis != null) {
							fis.close();
						}
						if(fos != null) {
							fos.close();
						}
						
					} catch (IOException e) {
						return ResponseEntity.badRequest().body("bad file close");

					}
				}
			}
	
		} catch (ResponseStatusException e) {
			return ResponseEntity.badRequest().body("bad response status");

		} catch (FileNotFoundException e) {
			return ResponseEntity.badRequest().body("file not found");

		} catch (Exception e) {
			return ResponseEntity.badRequest().body("bad");

		} finally {
			try {
				if(zipZos != null) {
					zipZos.closeEntry();
					zipZos.close();
				}
				if(zipFos != null) {
					zipFos.close();
				}

			} catch (IOException e) {
				return ResponseEntity.badRequest().body("bad file close");
				
			}
		}
	
		return ResponseEntity(folderPath, zipFileName);

	}

	
	public String createFolder(String loginUserName) {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
		String dateStr = dtf.format(date);
		String folderPath = System.getProperty("user.home") + File.separator +dateStr + "_" + loginUserName;

		File newdir = new File(folderPath);

		if(!newdir.exists()) {
			if(!newdir.mkdir()) {
				folderPath = null;
			}
		}
		
		return folderPath;
	}
	

	public ResponseEntity<byte[]> ResponseEntity(String folderPath, String zipFileName) {
		String encodedFileName = "";
		byte[] fileToByte = null;
		HttpHeaders headers = new HttpHeaders();

		try {
			encodedFileName = URLEncoder.encode(zipFileName, "UTF-8");
			InputStream is = new FileInputStream(folderPath + File.separator + zipFileName);
			fileToByte = IOUtils.toByteArray(is);

			headers.setContentType(MediaType.parseMediaType("application/zip"));
			headers.set("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
			headers.setContentLength(fileToByte.length);
			
			is.close();

			FileUtils.forceDelete(new File(folderPath));

		} catch(UnsupportedEncodingException e) {
			new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

		} catch(IOException e) {
			new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
		return new ResponseEntity<byte[]>(fileToByte, headers, HttpStatus.OK);

	}

}
