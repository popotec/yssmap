package com.broadenway.utils;

public enum ResponseFieldName {

	STATION_CODE("code"),
	ADDRESS("addr"),
	TEL_NO("tel"),
	STOCSK("inventory"),
	LATITUDE("lat"),
	LONGITUDE("lng"),
	PRICE("price"),
	STD_DT("regDt"),
	NAME("name"),
	OPENNING_HOURS("openTime");

	private String fieldName;

	ResponseFieldName(String fieldName) {
		this.fieldName=fieldName;
	}

	public String getName() {
		return fieldName;
	}
}
