package com.broadenway.utils.job;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DateJobParameter {
	private LocalDate createDate;

	public DateJobParameter(String createDateStr) {
		this.createDate = LocalDate.parse(createDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
