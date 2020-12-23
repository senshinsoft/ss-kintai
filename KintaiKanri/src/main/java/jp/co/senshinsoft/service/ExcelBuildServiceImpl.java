package jp.co.senshinsoft.service;

import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jp.co.senshinsoft.domain.Constants.CellPosition;
import jp.co.senshinsoft.domain.Constants.ColPosition;
import jp.co.senshinsoft.domain.User;
import jp.co.senshinsoft.domain.WorkReportDaily;
import jp.co.senshinsoft.domain.WorkReportMonthly;
import jp.co.senshinsoft.web.KK04001Form;

/**
 * Excelファイル作成
 * @author takada
 *
 */
@Service
public class ExcelBuildServiceImpl implements ExcelBuildService {
	@Value("classpath:SSI勤務報告書_yyyymm_名前_v102.xlsx")
	private Resource templateResource;
	
	@Override
	public Workbook getExcel(KK04001Form KK04001Form, List<WorkReportDaily> workDailyList) throws ResponseStatusException {
		Workbook wb;

		try {
			// テンプレートからWorkBookオブジェクト作成
			wb = WorkbookFactory.create(templateResource.getInputStream());
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);	
		}

		// WorkBookオブジェクトからシートを取得
		Sheet sheet = wb.getSheetAt(0);

		CellStyle cellStyleText = wb.createCellStyle();
		CellStyle cellStyleTime = wb.createCellStyle();
		CellStyle cellStyleYM =wb.createCellStyle();
		CellStyle cellStyleName =wb.createCellStyle();
		CellStyle cellStylePJName =wb.createCellStyle();
		Font fontYM = wb.createFont();
		Font fontPJName = wb.createFont();
		DataFormat format = wb.createDataFormat();

		// 年、月、名前、定時間、プロジェクト名、特記事項 セルの設定 
		getCell(sheet, CellPosition.YEAR.getRowIndex(), CellPosition.YEAR.getColIndex()).setCellValue(KK04001Form.getYear());
		getCell(sheet, CellPosition.MONTH.getRowIndex(), CellPosition.MONTH.getColIndex()).setCellValue(KK04001Form.getMonth());
		getCell(sheet, CellPosition.USER_NAME.getRowIndex(), CellPosition.USER_NAME.getColIndex()).setCellValue(KK04001Form.getUserName());
		if(!KK04001Form.getTeiji().isEmpty()) {
			getCell(sheet, CellPosition.TEIJI.getRowIndex(), CellPosition.TEIJI.getColIndex()).setCellValue(DateUtil.convertTime(KK04001Form.getTeiji()));
		} else {
			getCell(sheet, CellPosition.TEIJI.getRowIndex(), CellPosition.TEIJI.getColIndex()).setCellValue(KK04001Form.getTeiji());
		}
		getCell(sheet, CellPosition.PJ_NAME.getRowIndex(), CellPosition.PJ_NAME.getColIndex()).setCellValue("プロジェクト名：" + KK04001Form.getPjMei());
		getCell(sheet, CellPosition.TOKKIJIKO.getRowIndex(), CellPosition.TOKKIJIKO.getColIndex()).setCellValue(KK04001Form.getTokkijiko());

		getCell(sheet, CellPosition.YEAR.getRowIndex(), CellPosition.YEAR.getColIndex()).setCellStyle(cellYM(cellStyleYM, fontYM));
		getCell(sheet, CellPosition.MONTH.getRowIndex(), CellPosition.MONTH.getColIndex()).setCellStyle(cellYM(cellStyleYM, fontYM));
		getCell(sheet, CellPosition.USER_NAME.getRowIndex(), CellPosition.USER_NAME.getColIndex()).setCellStyle(cellName(cellStyleName));
		getCell(sheet, CellPosition.TEIJI.getRowIndex(), CellPosition.TEIJI.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
		getCell(sheet, CellPosition.PJ_NAME.getRowIndex(), CellPosition.PJ_NAME.getColIndex()).setCellStyle(cellPJName(cellStylePJName, fontPJName));
		getCell(sheet, CellPosition.TOKKIJIKO.getRowIndex(), CellPosition.TOKKIJIKO.getColIndex()).setCellStyle(cellText(cellStyleText));

		// 月初日、月末日、出社時間、退社時間、休憩時間、備考を取得してExcel出力用に成形
		String firstDay[] = workDailyList.get(0).getDay().split("　");
		String lastDay[] = workDailyList.get(workDailyList.size() -1).getDay().split("　");
		String ssJkn[] = KK04001Form.getSsJkn().split(",", -1);
		String tsJkn[] = KK04001Form.getTsJkn().split(",", -1);
		String kkJkn[] = KK04001Form.getKkJkn().split(",", -1);
		String biko[] = KK04001Form.getBiko().split(",", -1);
		final int FIRST_DAY = Integer.parseInt(firstDay[0]);
		final int LAST_DAY = Integer.parseInt(lastDay[0]);

		// 列毎に出社時間、退社時間、休憩時間、備考 セルの設定
		for(int i = FIRST_DAY + ColPosition.OFFSET.getColIndex(), j = 0 ; i <= LAST_DAY + ColPosition.OFFSET.getColIndex() ; i ++, j++) {
			if(!ssJkn[j].isEmpty()) {
				getCell(sheet, i, ColPosition.SS_JKN.getColIndex()).setCellValue(DateUtil.convertTime(ssJkn[j]));
			} else {
				getCell(sheet, i, ColPosition.SS_JKN.getColIndex()).setCellValue(ssJkn[j]);
			}

			if(!tsJkn[j].isEmpty()) {
				getCell(sheet, i, ColPosition.TS_JKN.getColIndex()).setCellValue(DateUtil.convertTime(tsJkn[j]));
			} else {
				getCell(sheet, i, ColPosition.TS_JKN.getColIndex()).setCellValue(tsJkn[j]);
			}

			if(!kkJkn[j].isEmpty()) {
				getCell(sheet, i, ColPosition.KK_JKN.getColIndex()).setCellValue(DateUtil.convertTime(kkJkn[j]));
			} else {
				getCell(sheet, i, ColPosition.KK_JKN.getColIndex()).setCellValue(kkJkn[j]);
			}

			getCell(sheet, i, ColPosition.BIKO.getColIndex()).setCellValue(biko[j]);

			getCell(sheet, i, ColPosition.SS_JKN.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
			getCell(sheet, i, ColPosition.TS_JKN.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
			getCell(sheet, i, ColPosition.KK_JKN.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
			getCell(sheet, i, ColPosition.BIKO.getColIndex()).setCellStyle(cellText(cellStyleText));
		}

		// シートの自動計算 有効
		sheet.setForceFormulaRecalculation(true);

		return wb;
	}

	@Override
	public Workbook getExcel(User user, List<WorkReportDaily> wrdList, WorkReportMonthly wrmList) throws ResponseStatusException {
		Workbook wb;

		try {
			// テンプレートからWorkBookオブジェクト作成
			wb = WorkbookFactory.create(templateResource.getInputStream());

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

		}

		// WorkBookオブジェクトからシートを取得
		Sheet sheet = wb.getSheetAt(0);

		CellStyle cellStyleText = wb.createCellStyle();
		CellStyle cellStyleTime = wb.createCellStyle();
		CellStyle cellStyleYM =wb.createCellStyle();
		CellStyle cellStyleName =wb.createCellStyle();
		CellStyle cellStylePJName =wb.createCellStyle();
		Font fontYM = wb.createFont();
		Font fontPJName = wb.createFont();
		DataFormat format = wb.createDataFormat();

		// 年、月、名前、定時間、プロジェクト名、特記事項 セルの設定 
		getCell(sheet, CellPosition.YEAR.getRowIndex(), CellPosition.YEAR.getColIndex()).setCellValue(wrmList.getYear());
		getCell(sheet, CellPosition.MONTH.getRowIndex(), CellPosition.MONTH.getColIndex()).setCellValue(wrmList.getMonth());
		getCell(sheet, CellPosition.USER_NAME.getRowIndex(), CellPosition.USER_NAME.getColIndex()).setCellValue(user.getSei() + " " + user.getMei());
		if(!wrmList.getTeiji().isEmpty()) {
			getCell(sheet, CellPosition.TEIJI.getRowIndex(), CellPosition.TEIJI.getColIndex()).setCellValue(DateUtil.convertTime(wrmList.getTeiji()));
		} else {
			getCell(sheet, CellPosition.TEIJI.getRowIndex(), CellPosition.TEIJI.getColIndex()).setCellValue(wrmList.getTeiji());
		}
		getCell(sheet, CellPosition.PJ_NAME.getRowIndex(), CellPosition.PJ_NAME.getColIndex()).setCellValue("プロジェクト名：" + wrmList.getPjMei());
		getCell(sheet, CellPosition.TOKKIJIKO.getRowIndex(), CellPosition.TOKKIJIKO.getColIndex()).setCellValue(wrmList.getTokkijiko());

		getCell(sheet, CellPosition.YEAR.getRowIndex(), CellPosition.YEAR.getColIndex()).setCellStyle(cellYM(cellStyleYM, fontYM));
		getCell(sheet, CellPosition.MONTH.getRowIndex(), CellPosition.MONTH.getColIndex()).setCellStyle(cellYM(cellStyleYM, fontYM));
		getCell(sheet, CellPosition.USER_NAME.getRowIndex(), CellPosition.USER_NAME.getColIndex()).setCellStyle(cellName(cellStyleName));
		getCell(sheet, CellPosition.TEIJI.getRowIndex(), CellPosition.TEIJI.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
		getCell(sheet, CellPosition.PJ_NAME.getRowIndex(), CellPosition.PJ_NAME.getColIndex()).setCellStyle(cellPJName(cellStylePJName, fontPJName));
		getCell(sheet, CellPosition.TOKKIJIKO.getRowIndex(), CellPosition.TOKKIJIKO.getColIndex()).setCellStyle(cellText(cellStyleText));

		// 月初日、月末日を取得する
		String firstDay = wrdList.get(0).getDay();
		String lastDay = wrdList.get(wrdList.size() - 1).getDay();
		final int FIRST_DAY = Integer.parseInt(firstDay);
		final int LAST_DAY = Integer.parseInt(lastDay);

		// 列毎に出社時間、退社時間、休憩時間、備考 セルの設定
		for(int i = FIRST_DAY + ColPosition.OFFSET.getColIndex(), j = 0 ; i <= LAST_DAY + ColPosition.OFFSET.getColIndex() ; i ++, j++) {
			if(!wrdList.get(j).getSsJkn().isEmpty()) {
				getCell(sheet, i, ColPosition.SS_JKN.getColIndex()).setCellValue(DateUtil.convertTime(wrdList.get(j).getSsJkn()));
			} else {
				getCell(sheet, i, ColPosition.SS_JKN.getColIndex()).setCellValue("");
			}

			if(!wrdList.get(j).getTsJkn().isEmpty()) {
				getCell(sheet, i, ColPosition.TS_JKN.getColIndex()).setCellValue(DateUtil.convertTime(wrdList.get(j).getTsJkn()));
			} else {
				getCell(sheet, i, ColPosition.TS_JKN.getColIndex()).setCellValue("");
			}

			if(!wrdList.get(j).getKkJkn().isEmpty()) {
				getCell(sheet, i, ColPosition.KK_JKN.getColIndex()).setCellValue(DateUtil.convertTime(wrdList.get(j).getKkJkn()));
			} else {
				getCell(sheet, i, ColPosition.KK_JKN.getColIndex()).setCellValue("");
			}

			getCell(sheet, i, ColPosition.BIKO.getColIndex()).setCellValue(wrdList.get(j).getBiko());

			getCell(sheet, i, ColPosition.SS_JKN.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
			getCell(sheet, i, ColPosition.TS_JKN.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
			getCell(sheet, i, ColPosition.KK_JKN.getColIndex()).setCellStyle(cellTime(cellStyleTime, format));
			getCell(sheet, i, ColPosition.BIKO.getColIndex()).setCellStyle(cellText(cellStyleText));
		}

		// シートの自動計算 有効
		sheet.setForceFormulaRecalculation(true);

		return wb;
	}

	/**
	 * Excel 行列生成
	 * @param sheet
	 * @param rowIndex
	 * @param colIndex
	 * @return Cell
	 */
	private Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
		Row row = sheet.getRow(rowIndex);
		if(row == null) {
			row = sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(colIndex);
		if(cell == null) {
			cell = row.createCell(colIndex);
		}
		
		return cell;
	}

	/**
	 * 勤務報告書 年月セル用フォーマット
	 * @param cellStyle
	 * @param font
	 * @return CellStyle
	 */
	private CellStyle cellYM(CellStyle cellStyle, Font font) {
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setLocked(false);
		font.setBold(true);
		font.setFontName("ＭＳ Ｐゴシック");
		font.setFontHeightInPoints((short) 12);
		cellStyle.setFont(font);
		
		return cellStyle;
	}

	/**
	 * 勤務報告書 名前セル用フォーマット
	 * @param cellStyle
	 * @return CellStyle
	 */
	private CellStyle cellName(CellStyle cellStyle) {
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setLocked(false);

		return cellStyle;
	}

	/**
	 * 勤務報告書 時間セル用フォーマット
	 * @param cellStyle
	 * @param format
	 * @return CellStyle
	 */
	private CellStyle cellTime(CellStyle cellStyle, DataFormat format) {
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setDataFormat(format.getFormat("HH:MM"));
		cellStyle.setLocked(false);
		
		return cellStyle;
	}

	/**
	 * 勤務報告書 備考セル用フォーマット
	 * @param cellStyle
	 * @return CellStyle
	 */
	private CellStyle cellText(CellStyle cellStyle) {
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setWrapText(true);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}

	/**
	 * 勤務報告書 プロジェクト名セル用フォーマット
	 * @param cellStyle
	 * @param font
	 * @return CellStyle
	 */
	private CellStyle cellPJName(CellStyle cellStyle, Font font) {
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setLocked(false);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font.setFontName("ＭＳ Ｐゴシック");
		font.setFontHeightInPoints((short) 8);
		cellStyle.setFont(font);
		
		return cellStyle;
	}
}
