package com.walletdb.walletappDB.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.walletdb.walletappDB.dto.Wallet;
import com.walletdb.walletappDB.exception.WalletException;

public class WalletDaoDBImpl implements WalletDao {

	Connection connection = null;

	public WalletDaoDBImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public Wallet addWallet(Wallet newWallet) throws WalletException {
		// TODO Auto-generated method stub
		if (connection != null) {
			String sql = "INSERT INTO wallet (id,name,balance,password) VALUES(?,?,?,?)";
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, newWallet.getId());
				preparedStatement.setString(2, newWallet.getName());
				preparedStatement.setDouble(3, newWallet.getBalance());
				preparedStatement.setString(4, newWallet.getPassword());
				Integer isAdded = preparedStatement.executeUpdate();
				if (isAdded == 1) {
					System.out.println("Wallet added successfully");
				}
			} catch (SQLException e) {

				throw new WalletException(e.getMessage());
			}

		}
		return getWalletById(newWallet.getId());

	}

	@Override
	public Wallet getWalletById(Integer walletId) throws WalletException {
		// TODO Auto-generated method stub
		Wallet wallet = null;
		if (connection != null) {
			String sql = "Select * from wallet where id=?";

			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, walletId);
				ResultSet resultset = preparedStatement.executeQuery();
				while (resultset.next()) {
					wallet = new Wallet(resultset.getInt(1), resultset.getString(2), resultset.getDouble(3),
							resultset.getString(4));
				}

			} catch (SQLException e) {
				throw new WalletException(e.getMessage());
			}

		}
		return wallet;
	}

	@Override
	public Wallet updateWallet(Wallet updateWallet) throws WalletException {
		// TODO Auto-generated method stub
		Wallet wallet;
		if (connection != null) {
			String sql = "Update wallet set name=?,balance=?,password=? where id =?";
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, updateWallet.getName());
				preparedStatement.setDouble(2, updateWallet.getBalance());
				preparedStatement.setString(3, updateWallet.getPassword());
				preparedStatement.setInt(4, updateWallet.getId());
				Integer count = preparedStatement.executeUpdate();
				if (count == 1) {
					System.out.println("Wallet updated.");
				}

			} catch (SQLException e) {
				throw new WalletException(e.getMessage());
			}

		}
		return getWalletById(updateWallet.getId());
	}

	@Override
	public Wallet deleteWalletById(Integer walletID) throws WalletException {
		// TODO Auto-generated method stub
		if (connection != null) {
			String sql = "DELETE FROM wallet WHERE id = ?";

			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, walletID);
				Integer count = preparedStatement.executeUpdate();

				if (count == 1) {
					System.out.println("Wallet deleted Successfully");
				}
			} catch (SQLException e) {
				throw new WalletException(e.getMessage());
			}

		}
		return getWalletById(walletID);
	}

}
