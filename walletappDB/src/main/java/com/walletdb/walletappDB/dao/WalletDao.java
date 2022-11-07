package com.walletdb.walletappDB.dao;


import java.sql.SQLException;

import com.walletdb.walletappDB.dto.Wallet;
import com.walletdb.walletappDB.exception.WalletException;

public interface WalletDao {
	// CRUD
	Wallet addWallet(Wallet newWallet) throws WalletException;

	Wallet getWalletById(Integer walletId) throws WalletException;

	Wallet updateWallet(Wallet updateWallet) throws WalletException;

	Wallet deleteWalletById(Integer walletID) throws WalletException;
}
