package com.simpleblockchain.core.contracts;

import java.security.PublicKey;

import com.simpleblockchain.core.components.TransactionImpl;
import com.simpleblockchain.core.components.TransactionOutputImpl;

public interface TransactionFactory {
	public TransactionImpl newTransaction();
}
