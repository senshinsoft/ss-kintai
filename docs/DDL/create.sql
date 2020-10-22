-- データベース作成
create database KintaiKanri;

-- テーブル作成
-- userテーブル
create table user(
	user_id varchar(4) NOT NULL comment '社員番号',
	mail_address varchar(50) NOT NULL comment 'メールアドレス',
	password varchar(100) NOT NULL comment 'パスワード',
	sei varchar(20) NOT NULL comment '氏名（姓）',
	mei varchar(20) NOT NULL comment '氏名（名）',
	admin_flg int NOT NULL default 0 comment '管理者フラグ',
	pass_upd_date TIMESTAMP NOT NULL comment 'パスワード更新日時',
	ins_user varchar(50) NOT NULL comment '作成者',
	ins_date TIMESTAMP NOT NULL comment '作成日時',
	upd_user varchar(50) NOT NULL comment '更新者',
	upd_date TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL comment '更新日時',
	primary key(user_id)
) comment 'ユーザ';

-- work_report_dailyテーブル
create table work_report_daily(
	user_id varchar(4) NOT NULL comment '社員番号',
	year varchar(4) NOT NULL comment '年',
	month varchar(2) NOT NULL comment '月',
	day varchar(2) NOT NULL comment '日',
	ss_jkn varchar(5) comment '出社時間',
	ts_jkn varchar(5) comment '退社時間',
	kk_jkn varchar(5) comment '休憩時間',
	kd_jkn varchar(5) comment '稼働時間',
	jkngi varchar(5) comment '時間外労働時間',
	biko varchar(5) comment '備考',
	ins_user varchar(50) NOT NULL comment '作成者',
	ins_date TIMESTAMP NOT NULL comment '作成日時',
	upd_user varchar(50) NOT NULL comment '更新者',
	upd_date TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL comment '更新日時',
	primary key(user_id,year,month,day)
)comment '勤務表報告書_日次情報';

-- work_report_monthlyテーブル
create table work_report_monthly(
	user_id varchar(4) NOT NULL comment '社員番号',
	year varchar(4) NOT NULL comment '年',
	month varchar(2) NOT NULL comment '月',
	teiji varchar(5) NOT NULL comment '定時間',
	kd_jkn_kei varchar(6) NOT NULL comment '稼働時間合計',
	jkngi_kei varchar(6) NOT NULL comment '時間外合計',
	pj_mei varchar(50) comment 'プロジェクト名',
	tokkijiko varchar(200) comment '特記事項',
	auth_flg int NOT NULL default 0 comment '承認済みフラグ',
	ins_user varchar(50) NOT NULL comment '作成者',
	ins_date TIMESTAMP NOT NULL comment '作成日時',
	upd_user varchar(50) NOT NULL comment '更新者',
	upd_date TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL comment '更新日時',
	primary key(user_id,year,month)
)comment '勤務表報告書_日次情報';
