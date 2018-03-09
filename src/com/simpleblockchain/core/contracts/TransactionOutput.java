package com.simpleblockchain.core.contracts;

import java.security.PublicKey;

public interface TransactionOutput {

	public float getValue();

	public String getId();

	public boolean isMine(PublicKey key);

	public PublicKey getReciepient();

}
