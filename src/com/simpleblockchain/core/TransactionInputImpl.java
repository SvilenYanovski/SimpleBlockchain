package com.simpleblockchain.core;

import com.simpleblockchain.core.contracts.TransactionInput;
import com.simpleblockchain.core.contracts.TransactionOutput;

public class TransactionInputImpl implements TransactionInput{
	private String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	private TransactionOutput utxo; //Contains the Unspent transaction output
	
	public TransactionInputImpl(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

	public String getTransactionOutputId() {
		return transactionOutputId;
	}

	public void setTransactionOutputId(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

	public TransactionOutput getUtxo() {
		return utxo;
	}

	public void setUtxo(TransactionOutput utxo) {
		this.utxo = utxo;
	}
	
	
}
