package com.broadenway.ureasolution.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.broadenway.ureasolution.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ExcelStoreService {

	public List<User> readDatas(MultipartFile file) {
		List<User> users = new ArrayList<>();
		try{
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0); // 시트 선택
			Map<Integer, String> fieldInfo = setFieldInfo(sheet.getRow(0)); // 필드정보
			users = getUsers(fieldInfo,sheet);
		} catch (IOException exception) {
			System.out.println("file io exception");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("unexpected exception");
		}
		return users;
	}

	private Map<Integer, String> setFieldInfo(XSSFRow row) {
		Map<Integer, String> map = new HashMap<>();
		for (int colIdx = 0; colIdx < row.getPhysicalNumberOfCells(); colIdx++) {
			XSSFCell cell = row.getCell(colIdx);
			if (cell == null) {
				continue;
			}
			map.put(colIdx, cell.getStringCellValue());
		}
		return map;
	}


	private List<User> getUsers(Map<Integer, String> fieldInfo, XSSFSheet sheet) {
		List<User> users = new ArrayList<>();
		for (int rowIdx = 1; rowIdx < sheet.getPhysicalNumberOfRows(); rowIdx++) {
			XSSFRow row = sheet.getRow(rowIdx);
			if (row == null) {
				continue;
			}
			users.add(makeUserInfo(fieldInfo, row));
		}
		return users;
	}

	private User makeUserInfo(Map<Integer, String> fieldInfo, XSSFRow row) {
		int employeeId = 0;
		String name = "";
		String ip1 = "";
		String ip2 = "";
		String ip3 = "";

		for (int colIdx = 0; colIdx <= row.getPhysicalNumberOfCells(); colIdx++) {

			XSSFCell cell = row.getCell(colIdx);

			if (cell == null) {
				continue;
			}
			switch (fieldInfo.get(colIdx)) {
				case "employeeId":
					employeeId = (int)cell.getNumericCellValue();
					break;
				case "name":
					name = cell.getStringCellValue();
					break;
				case "ip1":
					ip1 = cell.getStringCellValue();
					break;
				case "ip2":
					ip2 = cell.getStringCellValue();
					break;
				case "ip3":
					ip3 = cell.getStringCellValue();
					break;
			}
		}
		return new User(name,employeeId,ip1,ip2,ip3);
	}
}
