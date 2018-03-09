package com.simpleblockchain.core.contracts;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public interface Transaction {
	public void generateSignature(PrivateKey privateKey);
	public String getTransactionId();
	public boolean processTransaction();
	public void setTransactionId(String string);
	public void setOutputs(List<TransactionOutput> outputs);
	public List<TransactionOutput> getOutputs();
	public PublicKey getReciepient();
	public float getValue();
	public boolean verifiySignature();
	public float getInputsValue();
	public float getOutputsValue();
	public List<TransactionInput> getInputs();
	public PublicKey getSender();
}
