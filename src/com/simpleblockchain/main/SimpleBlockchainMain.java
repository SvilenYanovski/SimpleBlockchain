package com.simpleblockchain.main;

import com.simpleblockchain.constants.CoreConstants;
import com.simpleblockchain.core.components.BlockImpl;
import com.simpleblockchain.core.components.TransactionImpl;
import com.simpleblockchain.core.components.TransactionOutputImpl;
import com.simpleblockchain.core.components.WalletImpl;
import com.simpleblockchain.core.contracts.Block;
import com.simpleblockchain.core.contracts.Transaction;
import com.simpleblockchain.core.contracts.TransactionInput;
import com.simpleblockchain.core.contracts.TransactionOutput;
import com.simpleblockchain.core.contracts.Wallet;
import com.simpleblockchain.utils.StringUtil;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SimpleBlockchainMain {

	public static List<Block> blockchain = new ArrayList<Block>(); 
	public static Map<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //list of all unspent transactions.
	public static int difficulty = 5;
	public static float minimumTransaction = 0.1f;
	public static WalletImpl walletA, walletB;
	public static Transaction genesisTransaction;
	
	
	public static void main(String[] args) {
		//Setup Bouncey castle as a Security Provider
		Security.addProvider(new BouncyCastleProvider()); 
		
		walletA = new WalletImpl();
		walletB = new WalletImpl();
		Wallet coinbase = new WalletImpl();
		
		//create genesis transaction, which sends 100 NoobCoin to walletA: 
		genesisTransaction = new TransactionImpl(coinbase.getPublicKey(), walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.getPrivateKey());	 //manually sign the genesis transaction	
		genesisTransaction.setTransactionId("0"); //manually set the transaction id
		List<TransactionOutput> genesisOutputs = genesisTransaction.getOutputs();
		genesisOutputs.add(TransactionOutputImpl.createOutput(genesisTransaction.getReciepient(), 
				genesisTransaction.getValue(), genesisTransaction.getTransactionId()));
		genesisTransaction.setOutputs(genesisOutputs); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.
		
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new BlockImpl("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		//testing
		Block block1 = new BlockImpl(genesis.getHash());
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block2 = new BlockImpl(block1.getHash());
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		Block block3 = new BlockImpl(block2.getHash());
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		isChainValid();
		
//		//Test public and private keys
//		System.out.println("Private and public keys:");
//		System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
//		System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
//		//Create a test transaction from WalletA to walletB 
//		TransactionImpl transaction = new TransactionImpl(walletA.publicKey, walletB.publicKey, 5, null);
//		transaction.generateSignature(walletA.privateKey);
//		//Verify the signature works and verify it from the public key
//		System.out.println("Is signature verified:");
//		System.out.println(transaction.verifiySignature());
//		
//		System.out.println(CoreConstants.HELLO);
//		System.out.println(StringUtil.digestSha256(CoreConstants.HELLO));
//		
//		blockchain.add(new BlockImpl("First", "0"));
//		System.out.println("Trying to Mine block 1... ");
//		blockchain.get(0).mineBlock(difficulty);
//		
//		blockchain.add(new BlockImpl("Second",blockchain.get(blockchain.size()-1).getHash()));
//		System.out.println("Trying to Mine block 2... ");
//		blockchain.get(1).mineBlock(difficulty);
//		
//		blockchain.add(new BlockImpl("Third",blockchain.get(blockchain.size()-1).getHash()));
//		System.out.println("Trying to Mine block 3... ");
//		blockchain.get(2).mineBlock(difficulty);
//		
//		System.out.println("Blockchain is Valid: " + isChainValid());
//		
//		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
//		System.out.println(blockchainJson);
	}
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));
		
		
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			if(!currentBlock.getHash().equals(currentBlock.generateHash()) ){
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
			
			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.getTransactions().size(); t++) {
				Transaction currentTransaction = currentBlock.getTransactions().get(t);
				
				if(!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}
				
				for(TransactionInput input: currentTransaction.getInputs()) {	
					tempOutput = tempUTXOs.get(input.getTransactionOutputId());
					
					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
				
					if(input.getUtxo().getValue() != tempOutput.getValue()) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}
					
					tempUTXOs.remove(input.getTransactionOutputId());
				}
				
				for(TransactionOutput output: currentTransaction.getOutputs()) {
					tempUTXOs.put(output.getId(), output);
				}
				
				if( currentTransaction.getOutputs().get(0).getReciepient() != currentTransaction.getReciepient()) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.getOutputs().get(1).getReciepient() != currentTransaction.getSender()) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
				
			}
			
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}

}
