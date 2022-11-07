package com.walletdb.walletappDB.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.walletdb.walletappDB.dao.DaoUtility;
import com.walletdb.walletappDB.dao.WalletDao;
import com.walletdb.walletappDB.dao.WalletDaoImpl;
import com.walletdb.walletappDB.dto.Wallet;
import com.walletdb.walletappDB.exception.WalletException;
import com.walletdb.walletappDB.exception.RegistrationException;
import com.walletdb.walletappDB.service.WalletService;
import com.walletdb.walletappDB.service.WalletServiceImpl;

public class Controller {

	public static void main(String[] args) {

		WalletService walletService = new WalletServiceImpl();
		Scanner sc = new Scanner(System.in);

		try {

			Integer choice;
			Boolean flag = true;
			while (flag) {
				System.out.println("************");
				System.out.println("Press 1 to Register wallet\n" + "Press 2 to Login\n" + "Press 3 to Show balance\n"
						+ "Press 4 to Add fund\n" + "Press 5 to Transfer fund\n" + "Press 6 to Unregister wallet\n"
						+ "Press 7 to Withdraw wallet\n" + "Press 8 to Logout\n" + "Press 9 to Exit");
				System.out.println("************");
				choice = sc.nextInt();
				Integer id;
				String password;
				Double amount;
				Integer toId;
				Boolean islogged;
				switch (choice) {

				case 1:
					Wallet wallet = null;
					System.out.println("Enter Wallet id : ");
					id = sc.nextInt();
					System.out.println("Enter name : ");
					String name = sc.next();
					sc.nextLine();
					System.out.println("Enter balance : ");
					amount = sc.nextDouble();
					System.out.println("Enter password : ");
					password = sc.next();
					sc.nextLine();
					try {
						wallet = walletService.validateWallet(id, name, amount, password);
						System.out.println(walletService.registerWallet(wallet) != null ? "Registered Successfully"
								: "Registration failed");
					} catch (RegistrationException e) {
						System.out.println(e.getMessage());
						System.out.println("Validation failed !! Registration cannot be done");
					} catch (WalletException e) {
						System.out.println(e.getMessage());
						System.out.println("Registration Failed");
					}

					break;
				case 2:
					System.out.println("Enter Wallet id");
					id = sc.nextInt();
					System.out.println("Enter password");
					password = sc.next();
					try {
						islogged = walletService.login(id, password);
						System.out.println(islogged ? "Login successful !!" : "Login failed");
					} 
					catch (WalletException e) {
						System.out.println(e.getMessage());
						System.out.println("Login failed !!!");
					}
					
					break;
				case 3:
					try {
						Double balance = walletService.showWalletBalance();
						System.out.println("Balance for your id is : " + balance);
					} catch (WalletException e) {
						System.out.println(e.getMessage());
					}
			
					break;
				case 4:
					System.out.println("Enter amount");
					amount = sc.nextDouble();
					try {
						Double fundAdded = walletService.addFundsToWallet(amount);
						System.out.println("Fund added successfully...Available balance : " + fundAdded);
					} catch (WalletException e) {
						System.out.println(e.getMessage());
					}
				
					break;
				case 5:
					System.out.println("Enter Receiver Wallet id");
					toId = sc.nextInt();
					System.out.println("Enter amount");
					amount = sc.nextDouble();
					try {
						if (walletService.fundTransfer(toId, amount))
							System.out.println("Fund transferred successfully !!!");
					} catch (WalletException e) {
						System.out.println(e.getMessage());
					}
					
					break;
				case 6:
					System.out.println("Enter password to confirm ");
					password = sc.next();
					try {
						Wallet deleteWallet = walletService.unRegisterWallet(password);
						if (deleteWallet == null)
							System.out.println("Unregistered successfull !!!");
					} catch (WalletException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 7:
					System.out.println("Enter amount to withdraw : ");
					amount = sc.nextDouble();
					try {
						Double withdrawFund = walletService.withdrawFunds(amount);
						System.out.println("Amount withdrawn successfully...Available Balance : " + withdrawFund);
					} catch (WalletException e) {
						System.out.println(e.getMessage());
					}
					
					break;
				case 8:
					try {
						if (walletService.logout() == false)
							System.out.println("Logout successful !!!");
					} catch (WalletException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 9:
					System.out.println("Exiting..");
					flag = false;
					break;
				default:
					System.out.println("Invalid choice");
					break;

				}
			}
		}
		catch (InputMismatchException e) {
			System.out.println("Wallet id or amount contains only numbers !");
		}
		catch (Exception e) {
			System.out.println(e.getClass());
		} finally {
			System.out.println("Program terminated !");
			sc.close();

		}
	}

}
