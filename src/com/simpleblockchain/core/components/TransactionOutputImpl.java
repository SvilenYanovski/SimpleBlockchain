package com.simpleblockchain.core.components;

import java.security.PublicKey;

import com.simpleblockchain.core.contracts.TransactionOutput;
import com.simpleblockchain.utils.StringUtil;

public class TransactionOutputImpl implements TransactionOutput{
	private String id;
	private PublicKey reciepient; //also known as the new owner of these coins.
	private float value; //the amount of coins they own
	private String parentTransactionId; //the id of the transaction this output was created in
	
	//Constructor
	private TransactionOutputImpl(PublicKey reciepient, float value, String parentTransactionId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = StringUtil.digestSha256(StringUtil.getStringFromKey(reciepient)
											+ Float.toString(value)
											+ parentTransactionId);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PublicKey getReciepient() {
		return reciepient;
	}

	public void setReciepient(PublicKey reciepient) {
		this.reciepient = reciepient;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getParentTransactionId() {
		return parentTransactionId;
	}

	public void setParentTransactionId(String parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}

	//Check if coin belongs to you
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == reciepient);
	}
	
	public static TransactionOutputImpl createOutput(PublicKey reciepient, float value, String parentTransactionId) {
		return new TransactionOutputImpl(reciepient, value, parentTransactionId);
	}
}
