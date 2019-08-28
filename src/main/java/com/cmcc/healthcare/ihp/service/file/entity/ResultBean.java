package com.cmcc.healthcare.ihp.service.file.entity;

import java.io.Serializable;
import java.util.HashMap;

public class ResultBean implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	//回款/付款日期结果集合    当前回档期 原数据
	private HashMap<Integer,String> titleMap;
	
	//回款/付款日期结果集合    当前回档期 原数据
	private HashMap<Integer,String> dateMap;
	
	//付款集合    当前回档期  原数据
	private HashMap<Integer,Double> paymentMap;

	//回款集合     当前回档期  原数据
	private HashMap<Integer,Double> gatherMap;
	
	//付款统计集合     当前回档期    
	private HashMap<Integer,Double> paymentTotalMap;

	//回款统计集合     当前回档期
	private HashMap<Integer,Double> gatherTotalMap;
	
	//垫资金额集合     当前回档期
	private HashMap<Integer,Double> dzMap;

	//垫资时长集合     当前回档期
	private HashMap<Integer,Long> dzscMap;
	
	//垫资成本集合     当前回档期
	private HashMap<Integer,Double> dzcbMap;
	
	//日利率当前回档期
	private HashMap<Integer,String> dayRateMap;
	
	
	public HashMap<Integer, String> getDateMap() {
		return dateMap;
	}

	public void setDateMap(HashMap<Integer, String> dateMap) {
		this.dateMap = dateMap;
	}

	public HashMap<Integer, Double> getPaymentMap() {
		return paymentMap;
	}

	public void setPaymentMap(HashMap<Integer, Double> paymentMap) {
		this.paymentMap = paymentMap;
	}

	public HashMap<Integer, Double> getGatherMap() {
		return gatherMap;
	}

	public void setGatherMap(HashMap<Integer, Double> gatherMap) {
		this.gatherMap = gatherMap;
	}

	public HashMap<Integer, Double> getPaymentTotalMap() {
		return paymentTotalMap;
	}

	public void setPaymentTotalMap(HashMap<Integer, Double> paymentTotalMap) {
		this.paymentTotalMap = paymentTotalMap;
	}

	public HashMap<Integer, Double> getGatherTotalMap() {
		return gatherTotalMap;
	}

	public void setGatherTotalMap(HashMap<Integer, Double> gatherTotalMap) {
		this.gatherTotalMap = gatherTotalMap;
	}

	public HashMap<Integer, Double> getDzMap() {
		return dzMap;
	}

	public void setDzMap(HashMap<Integer, Double> dzMap) {
		this.dzMap = dzMap;
	}

	public HashMap<Integer, Long> getDzscMap() {
		return dzscMap;
	}

	public void setDzscMap(HashMap<Integer, Long> dzscMap) {
		this.dzscMap = dzscMap;
	}

	public HashMap<Integer, Double> getDzcbMap() {
		return dzcbMap;
	}

	public void setDzcbMap(HashMap<Integer, Double> dzcbMap) {
		this.dzcbMap = dzcbMap;
	}

	public HashMap<Integer, String> getTitleMap() {
		return titleMap;
	}

	public void setTitleMap(HashMap<Integer, String> titleMap) {
		this.titleMap = titleMap;
	}

	public HashMap<Integer, String> getDayRateMap() {
		return dayRateMap;
	}

	public void setDayRateMap(HashMap<Integer, String> dayRateMap) {
		this.dayRateMap = dayRateMap;
	}

}
