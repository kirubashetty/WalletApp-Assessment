package com.walletdb.walletappDB.service;

import com.walletdb.walletappDB.exception.RegistrationException;

import java.sql.SQLException;

import com.walletdb.walletappDB.dto.Wallet;
import com.walletdb.walletappDB.exception.WalletException;

public interface WalletService {

	Wallet validateWallet(Integer id,String name,Double balance,String password) throws RegistrationException;

	Wallet registerWallet(Wallet newWallet) throws WalletException;

	Boolean login(Integer walletId, String password) throws WalletException;
	
	Boolean logout() throws WalletException;

	Double addFundsToWallet(Double amount) throws WalletException;

	Double showWalletBalance() throws WalletException;

	Boolean fundTransfer(Integer toId, Double amount) throws WalletException;

	Wallet unRegisterWallet(String password) throws WalletException;

	Double withdrawFunds(Double amount) throws WalletException;
	

}
