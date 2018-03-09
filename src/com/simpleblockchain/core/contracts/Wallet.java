package com.simpleblockchain.core.contracts;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface Wallet {

	public PublicKey getPublicKey();

	public PrivateKey getPrivateKey();

}
