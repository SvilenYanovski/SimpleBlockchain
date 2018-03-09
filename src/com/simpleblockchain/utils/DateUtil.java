package com.simpleblockchain.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String timestamp() {
		return new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
	}
}
