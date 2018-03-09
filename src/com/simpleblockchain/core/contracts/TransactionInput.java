package com.simpleblockchain.core.contracts;

public interface TransactionInput {
	public TransactionOutput getUtxo();

	public String getTransactionOutputId();

	public void setUtxo(TransactionOutput transactionOutput);
	
}
