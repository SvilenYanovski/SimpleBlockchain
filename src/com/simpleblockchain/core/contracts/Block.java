package com.simpleblockchain.core.contracts;

import java.util.ArrayList;
import java.util.Properties;

public interface Block {
	public String getHash();
	public String getPrevHash();
	public String calculateHash();
	public void mineBlock(int difficulty);
	public ArrayList<Transaction> getTransactions();
	public boolean addTransaction(Transaction transaction);
}
