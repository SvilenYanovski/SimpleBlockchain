package com.simpleblockchain.core.contracts;

public interface Block {
	public String getHash();
	public String getPrevHash();
	public String calculateHash();
	public void mineBlock(int difficulty);
}
