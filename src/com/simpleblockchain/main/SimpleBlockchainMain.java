package com.simpleblockchain.main;

import com.simpleblockchain.constants.CoreConstants;
import com.simpleblockchain.core.BlockImpl;
import com.simpleblockchain.core.contracts.Block;
import com.simpleblockchain.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import org.bouncycastle.*;

public class SimpleBlockchainMain {

	public static List<Block> blockchain = new ArrayList<Block>(); 
	public static int difficulty = 5;
	
	public static void main(String[] args) {
		System.out.println(CoreConstants.HELLO);
		System.out.println(StringUtil.digestSha256(CoreConstants.HELLO));
		
		blockchain.add(new BlockImpl("First", "0"));
		System.out.println("Trying to Mine block 1... ");
		blockchain.get(0).mineBlock(difficulty);
		
		blockchain.add(new BlockImpl("Second",blockchain.get(blockchain.size()-1).getHash()));
		System.out.println("Trying to Mine block 2... ");
		blockchain.get(1).mineBlock(difficulty);
		
		blockchain.add(new BlockImpl("Third",blockchain.get(blockchain.size()-1).getHash()));
		System.out.println("Trying to Mine block 3... ");
		blockchain.get(2).mineBlock(difficulty);
		
		System.out.println("Blockchain is Valid: " + isChainValid());
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
		System.out.println(blockchainJson);
	}
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashe is incorrect!");			
				return false;
			}
			if(!previousBlock.getHash().equals(currentBlock.getPrevHash()) ) {
				System.out.println("Previous Hashe not equal");
				return false;
			}
			if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		return true;
	}

}
