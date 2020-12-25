package jp.co.senshinsoft.domain;

/**
 * 定数用クラス
 * @author 髙田
 *
 */
public class Constants {

	// Excel作成用 行列位置
	public enum CellPosition {
		YEAR(0, 1),
		MONTH(1, 1),
		USER_NAME(2, 3),
		TEIJI(2, 7),
		PJ_NAME(38, 8),
		TOKKIJIKO(40, 3);
	
		private int rowIndex;
		private int colIndex;
	
		private CellPosition(int rowIndex, int colIndex) {
			this.rowIndex = rowIndex;
			this.colIndex = colIndex;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}

		public int getColIndex() {
			return colIndex;
		}

		public void setColIndex(int colIndex) {
			this.colIndex = colIndex;
		}
	
	};

	// Excel作成用 列位置
	public enum ColPosition {
		SS_JKN(3),
		TS_JKN(4),
		KK_JKN(5),
		BIKO(8),
		OFFSET(6);

		private int colIndex;

		private ColPosition(int colIndex) {
			this.colIndex = colIndex;
		}
		
		public int getColIndex() {
			return colIndex;
		}

		public void setColIndex(int colIndex) {
			this.colIndex = colIndex;
		}

	};
	
}