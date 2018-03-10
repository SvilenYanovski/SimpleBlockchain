package com.simpleblockchain.core.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.simpleblockchain.core.contracts.Block;
import com.simpleblockchain.core.contracts.Transaction;
import com.simpleblockchain.utils.StringUtil;

public class BlockImpl implements Block{
	private String hash;
	private String prevHash;
	private String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
	private long timeStamp;
	private int nonce = 0;

	public BlockImpl(String previousHash ) {
		this.prevHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = generateHash();
	}

	public String getHash() {
		return hash;
	}

	public String getPrevHash() {
		return prevHash;
	}
	
	public ArrayList<Transaction> getTransactions() {
		return this.transactions;
	}

	public String generateHash() {
		return StringUtil.digestSha256( 
				prevHash +
				Long.toString(timeStamp) +
				Integer.toString(nonce) + 
				merkleRoot 
				);
	}
	
	public void mineBlock(int difficulty) {
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = new String(new char[difficulty]).replace('\0', '0');
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = generateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
	
	//Add transactions to this block
	public boolean addTransaction(Transaction transaction) {
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) return false;		
		if((prevHash != "0")) {
			if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
}
