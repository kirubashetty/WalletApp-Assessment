package com.walletdb.walletappDB.service;

import java.sql.SQLException;
import java.text.MessageFormat;

import com.walletdb.walletappDB.dao.DaoUtility;
import com.walletdb.walletappDB.dao.WalletDao;
import com.walletdb.walletappDB.dao.WalletDaoDBImpl;
import com.walletdb.walletappDB.dao.WalletDaoImpl;
import com.walletdb.walletappDB.dto.Wallet;
import com.walletdb.walletappDB.exception.RegistrationException;
import com.walletdb.walletappDB.exception.WalletException;
import com.walletdb.walletappDB.service.WalletService;

public class WalletServiceImpl implements WalletService {

	private WalletDao walletRepository = new WalletDaoDBImpl(DaoUtility.getConnectionToMySQL());

	// private WalletDao walletRepository = new WalletDaoImpl();

	private Wallet wallet;

	private Boolean isloggedin = false;

	public Wallet validateWallet(Integer id, String name, Double balance, String password)
			throws RegistrationException {
		// TODO Auto-generated method stub
		Wallet wallet = new Wallet();
		String validName = "^[a-zA-Z\\s]+";
		String validPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
		if (!name.matches(validName))
			throw new RegistrationException("Name can contain only alphabets");
		if (balance.toString().charAt(0) == '-')
			throw new RegistrationException("Balance cannot be in negative");
		if (!password.matches(validPassword))
			throw new RegistrationException(
					"Password must contains atleast one digit,one lowercase letter,one uppercase letter, one special character,no whitespace and it should be of length  min 8 and max 20");
		wallet.setId(id);
		wallet.setName(name);
		wallet.setBalance(balance);
		wallet.setPassword(password);
		return wallet;
	}

	public Wallet registerWallet(Wallet newWallet) throws WalletException {
		// TODO Auto-generated method stub
		if (this.isloggedin == true) {
			this.wallet = null;
			this.isloggedin = false;
		}
		Wallet wallet = walletRepository.getWalletById(newWallet.getId());
		if (wallet == null)
			wallet = this.walletRepository.addWallet(newWallet);
		else
			throw new WalletException("Wallet id already exist");

		return wallet;

	}

	public Boolean login(Integer walletId, String password) throws WalletException {
		// TODO Auto-generated method stub
		if (isloggedin) {
			String message = "You are already logged in as id  " + this.wallet.getId();
			throw new WalletException(message);
		}
		this.wallet = null;
		this.isloggedin = false;
		Wallet wallet = this.walletRepository.getWalletById(walletId);
		if (wallet == null)
			throw new WalletException("Wallet id not found");
		if (!wallet.getPassword().equals(password))
			throw new WalletException("Password does't match to wallet id.");
		isloggedin = true;
		this.wallet = wallet;
		return isloggedin;

	}

	public Boolean logout() throws WalletException {
		// TODO Auto-generated method stub
		if (this.wallet == null)
			throw new WalletException("You are not logged in!!!");
		this.wallet = null;
		this.isloggedin = false;
		return this.isloggedin;

	}

	public Double addFundsToWallet(Double amount) throws WalletException {
		// TODO Auto-generated method stub
		if (amount <= 0)
			throw new WalletException("Amount cannot be less than or equal to zero");
		if (wallet == null)
			throw new WalletException("Please log in first");
		wallet.setBalance(wallet.getBalance() + amount);
		this.walletRepository.updateWallet(wallet);
		return wallet.getBalance();
	}

	public Double showWalletBalance() throws WalletException {
		// TODO Auto-generated method stub
		if (wallet == null)
			throw new WalletException("Please login first");
		return wallet.getBalance();

	}

	public Boolean fundTransfer(Integer toId, Double amount) throws WalletException {
		// TODO Auto-generated method stub
		boolean isTransferred = false;
		Wallet fromWallet = this.wallet;
		Wallet toWallet = this.walletRepository.getWalletById(toId);
		if (fromWallet == null)
			throw new WalletException("Please login first");
		if (toWallet == null)
			throw new WalletException("Receiver wallet id not found");
		if (amount <= 0)
			throw new WalletException("Transfer amount cannot be less than or equal to zero");
		if (fromWallet.getBalance() - amount < 0)
			throw new WalletException("Amount insufficient");
		if (fromWallet.getId() == toWallet.getId())
			throw new WalletException("Wallet id cannot be same");
		fromWallet.setBalance(fromWallet.getBalance() - amount);
		toWallet.setBalance(toWallet.getBalance() + amount);
		this.walletRepository.updateWallet(fromWallet);
		this.walletRepository.updateWallet(toWallet);
		this.wallet = fromWallet;
		isTransferred = true;
		return isTransferred;

	}

	public Wallet unRegisterWallet(String password) throws WalletException {
		// TODO Auto-generated method stub
		Wallet deletedWallet = this.wallet;
		if (wallet == null)
			throw new WalletException("Please login first");
		if (!wallet.getPassword().equals(password))
			throw new WalletException("Password does't match to unregister your account.");
		deletedWallet = this.walletRepository.deleteWalletById(wallet.getId());
		logout();
		return deletedWallet;
	}

	public Double withdrawFunds(Double amount) throws WalletException {
		// TODO Auto-generated method stub
		if (wallet == null)
			throw new WalletException("Please login first");
		if (amount <= 0)
			throw new WalletException("Withdraw amount cannot be less than or equal to zero");
		if (wallet.getBalance() - amount < 0)
			throw new WalletException("Amount insufficient");
		wallet.setBalance(wallet.getBalance() - amount);
		this.walletRepository.updateWallet(wallet);
		return wallet.getBalance();

	}


}
