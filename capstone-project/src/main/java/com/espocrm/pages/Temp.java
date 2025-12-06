package com.espocrm.pages;

import com.espocrm.utils.ExcelUtils;

public class Temp {

	public static void main(String[] args) {
		ExcelUtils util = new ExcelUtils("src/test/resources/testData.xlsx");
		System.out.println(util.getData("Opportunities"));
	}

}
