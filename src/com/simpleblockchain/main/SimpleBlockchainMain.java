package com.simpleblockchain.main;

import com.simpleblockchain.constants.CoreConstants;
import com.simpleblockchain.utils.DateUtil;
import com.google.gson.*;

public class SimpleBlockchainMain {

	public static void main(String[] args) {
		System.out.println(CoreConstants.HELLO);
		System.out.println(DateUtil.timestamp());
	}

}
