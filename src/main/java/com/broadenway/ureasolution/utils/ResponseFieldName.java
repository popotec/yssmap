package com.broadenway.ureasolution.utils;

public enum ResponseFieldName {

	STATION_CODE("코드"),
	ADDRESS("주소"),
	TEL_NO("전화번호"),
	STOCSK("재고량"),
	LATITUDE("위도"),
	LONGITUDE("경도"),
	PRICE("가격"),
	STD_DT("데이터기준일"),
	NAME("명칭"),
	OPENNING_HOURS("영업시간");

	private String fieldName;

	ResponseFieldName(String fieldName) {
		this.fieldName=fieldName;
	}

	public String getName() {
		return fieldName;
	}
}
