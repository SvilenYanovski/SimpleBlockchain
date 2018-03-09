package com.simpleblockchain.core;

import java.util.Date;

import com.simpleblockchain.core.contracts.Block;
import com.simpleblockchain.utils.StringUtil;

public class BlockImpl implements Block{
	private String hash;
	private String prevHash;
	private String data;
	private long timeStamp;
	private int nonce = 0;

	public BlockImpl(String data,String previousHash ) {
		this.data = data;
		this.prevHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}

	public String getHash() {
		return hash;
	}

	public String getPrevHash() {
		return prevHash;
	}
	
	public String calculateHash() {
		return StringUtil.digestSha256( 
				prevHash +
				Long.toString(timeStamp) +
				Integer.toString(nonce) + 
				data 
				);
	}
	
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0');
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
}
