package com.simpleblockchain.core.components;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.simpleblockchain.core.contracts.Transaction;
import com.simpleblockchain.core.contracts.TransactionInput;
import com.simpleblockchain.core.contracts.TransactionOutput;
import com.simpleblockchain.main.SimpleBlockchainMain;
import com.simpleblockchain.utils.StringUtil;

public class TransactionImpl implements Transaction{
	private String transactionId; // this is also the hash of the transaction.
	public PublicKey sender; // senders address/public key.
	public PublicKey reciepient; // Recipients address/public key.
	public float value;
	public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	
	public List<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int sequence = 0; // a rough count of how many transactions have been generated. 
	
	// Constructor: 
	public TransactionImpl(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	public String getTransactionId() {
		return this.transactionId;
	}
	
	public void setTransactionId(String id) {
		this.transactionId = id;
	}
	
	public List<TransactionOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<TransactionOutput> outputs) {
		this.outputs = outputs;
	}

	public PublicKey getReciepient() {
		return this.reciepient;
	}

	public float getValue() {
		return this.value;
	}

	public List<TransactionInput> getInputs() {
		return this.inputs;
	}

	public PublicKey getSender() {
		return this.sender;
	}

	// This Calculates the transaction hash (which will be used as its Id)
	private String generateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.digestSha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	}
	
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		signature = StringUtil.applyECDSASig(privateKey,data);		
	}
	
	//Verifies the data we signed hasn't been tampered with
	public boolean verifiySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		return StringUtil.verifyECDSASig(sender, data, signature);
	}
	
	//Returns true if new transaction could be created.	
	public boolean processTransaction() {
			
			if(verifiySignature() == false) {
				System.out.println("#Transaction Signature failed to verify");
				return false;
			}
					
			//gather transaction inputs (Make sure they are unspent):
			for(TransactionInput i : inputs) {
				i.setUtxo(SimpleBlockchainMain.UTXOs.get(i.getTransactionOutputId()));
			}

			//check if transaction is valid:
			if(getInputsValue() < SimpleBlockchainMain.minimumTransaction) {
				System.out.println("#Transaction Inputs to small: " + getInputsValue());
				return false;
			}
			
			//generate transaction outputs:
			float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
			transactionId = generateHash();
			outputs.add(TransactionOutputImpl.createOutput( this.reciepient, value,transactionId)); //send value to recipient
			outputs.add(TransactionOutputImpl.createOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
				
			//add outputs to Unspent list
			for(TransactionOutput tranOutput : outputs) {
				SimpleBlockchainMain.UTXOs.put(tranOutput.getId() , tranOutput);
			}
			
			//remove transaction inputs from UTXO lists as spent:
			for(TransactionInput tranInput : inputs) {
				if(tranInput.getUtxo() == null) continue; //if Transaction can't be found skip it 
				SimpleBlockchainMain.UTXOs.remove(tranInput.getUtxo().getId());
			}
			
			return true;
		}
	
	//returns sum of inputs(UTXOs) values
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput tranInput : inputs) {
			if(tranInput.getUtxo() == null) {
				continue; 
			}
			total += tranInput.getUtxo().getValue();
		}
		return total;
	}

	//returns sum of outputs:
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput tranOutput : outputs) {
			total += tranOutput.getValue();
		}
		return total;
	}	
}
