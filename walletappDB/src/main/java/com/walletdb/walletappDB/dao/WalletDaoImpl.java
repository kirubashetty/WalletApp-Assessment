package com.walletdb.walletappDB.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.walletdb.walletappDB.dto.Wallet;
import com.walletdb.walletappDB.exception.WalletException;

public class WalletDaoImpl implements WalletDao {

	// Create collection to store the Wallet information.
	Map<Integer, Wallet> wallets = new HashMap<>(); 

	@Override
	public Wallet addWallet(Wallet newWallet) throws WalletException {

		this.wallets.put(newWallet.getId(), newWallet);
		return this.wallets.get(newWallet.getId());

	}

	@Override
	public Wallet getWalletById(Integer walletId) throws WalletException {
		// TODO Auto-generated method stub

		return this.wallets.get(walletId);
	}

	@Override
	public Wallet updateWallet(Wallet updateWallet) throws WalletException {
		// TODO Auto-generated method stub
		this.wallets.replace(updateWallet.getId(), updateWallet);
		return this.wallets.get(updateWallet.getId());

	}

	@Override
	public Wallet deleteWalletById(Integer walletID) throws WalletException {
		// TODO Auto-generated method stub
		return wallets.remove(walletID);
	}

}
