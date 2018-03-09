package com.simpleblockchain.core;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.simpleblockchain.core.contracts.Transaction;
import com.simpleblockchain.utils.StringUtil;

public class TransactionImpl implements Transaction{
	public String transactionId; // this is also the hash of the transaction.
	public PublicKey sender; // senders address/public key.
	public PublicKey reciepient; // Recipients address/public key.
	public float value;
	public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	
	public List<TransactionInputImpl> inputs = new ArrayList<TransactionInputImpl>();
	public List<TransactionOutputImpl> outputs = new ArrayList<TransactionOutputImpl>();
	
	private static int sequence = 0; // a rough count of how many transactions have been generated. 
	
	// Constructor: 
	public TransactionImpl(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInputImpl> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.digestSha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	}
}
