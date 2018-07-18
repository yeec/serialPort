package bros.manage.entity;

import java.util.Date;

public class LogSysStatemBen {
	 // 日志序号
	 private int logNum;
	 // 日志时间
	 private Date logTime;
	 // 对应系统
	 private String logSys;
	 // 对应功能
	 private String logFun;
	 // 日志类别
	 private String logType;
	 // 日志语句
	 private String logSql;
	 // 日志结果
	 private String logResult;
	 // 日志描述
	 private String logMemo;
	 // 数据主键
	 private String mainkey;
	 // 日志等级
	 private String logGrade;
	 // 日志分类
	 private String logClass;
	 // 操作人员
	 private String userid;
	 // 处理序号
	 private int dealNum;
	 // 状态类型
	 private String dealType;
	 // 状态说明
	 private String dealMemo;
	 // 是否处理
	 private String dispose;
	public int getLogNum() {
		return logNum;
	}
	public void setLogNum(int logNum) {
		this.logNum = logNum;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public String getLogSys() {
		return logSys;
	}
	public void setLogSys(String logSys) {
		this.logSys = logSys;
	}
	public String getLogFun() {
		return logFun;
	}
	public void setLogFun(String logFun) {
		this.logFun = logFun;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getLogSql() {
		return logSql;
	}
	public void setLogSql(String logSql) {
		this.logSql = logSql;
	}
	public String getLogResult() {
		return logResult;
	}
	public void setLogResult(String logResult) {
		this.logResult = logResult;
	}
	public String getLogMemo() {
		return logMemo;
	}
	public void setLogMemo(String logMemo) {
		this.logMemo = logMemo;
	}
	public String getMainkey() {
		return mainkey;
	}
	public void setMainkey(String mainkey) {
		this.mainkey = mainkey;
	}
	public String getLogGrade() {
		return logGrade;
	}
	public void setLogGrade(String logGrade) {
		this.logGrade = logGrade;
	}
	public String getLogClass() {
		return logClass;
	}
	public void setLogClass(String logClass) {
		this.logClass = logClass;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getDealNum() {
		return dealNum;
	}
	public void setDealNum(int dealNum) {
		this.dealNum = dealNum;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getDealMemo() {
		return dealMemo;
	}
	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}
	public String getDispose() {
		return dispose;
	}
	public void setDispose(String dispose) {
		this.dispose = dispose;
	}
	
	
	@Override
	public String toString() {
		return "LogSysStatemBen [logNum=" + logNum + ", logTime=" + logTime + ", logSys=" + logSys + ", logFun="
				+ logFun + ", logType=" + logType + ", logSql=" + logSql + ", logResult=" + logResult + ", logMemo="
				+ logMemo + ", mainkey=" + mainkey + ", logGrade=" + logGrade + ", logClass=" + logClass + ", userid="
				+ userid + ", dealNum=" + dealNum + ", dealType=" + dealType + ", dealMemo=" + dealMemo + ", dispose="
				+ dispose + "]";
	}
	
	
}
