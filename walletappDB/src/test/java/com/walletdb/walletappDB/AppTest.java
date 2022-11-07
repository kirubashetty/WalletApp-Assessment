package com.walletdb.walletappDB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.walletdb.walletappDB.dao.DaoUtility;
import com.walletdb.walletappDB.dao.WalletDao;
import com.walletdb.walletappDB.dao.WalletDaoDBImpl;
import com.walletdb.walletappDB.dao.WalletDaoImpl;
import com.walletdb.walletappDB.dto.Wallet;
import com.walletdb.walletappDB.exception.RegistrationException;
import com.walletdb.walletappDB.exception.WalletException;
import com.walletdb.walletappDB.service.WalletService;
import com.walletdb.walletappDB.service.WalletServiceImpl;

/**
 * Unit test for simple App.
 */
public class AppTest {
	WalletService walletService = new WalletServiceImpl();

	@BeforeAll
	public static void setupTestData() throws WalletException {
		System.out.println("Create all test data");
		WalletService walletService = new WalletServiceImpl();
		Wallet storedWallet1 = new Wallet(1, "abc", 3000.0, "123Abc@df");
		Wallet storedWallet2 = new Wallet(2, "xxx", 5000.0, "5t55Dbcdf#");
		Wallet storedWallet3 = new Wallet(3, "xyz", 10000.0, "123Abc@df");
		Wallet storedWallet4 = new Wallet(4, "yyy", 8000.0, "5t55Dbcdf#");
		Wallet storedWallet5 = new Wallet(5, "xyz", 4000.0, "123Abc@df");
		Wallet storedWallet6 = new Wallet(6, "yyy", 6000.0, "5t55Dbcdf#");
		walletService.registerWallet(storedWallet1);
		walletService.registerWallet(storedWallet2);
		walletService.registerWallet(storedWallet3);
		walletService.registerWallet(storedWallet4);
		walletService.registerWallet(storedWallet5);
		walletService.registerWallet(storedWallet6);
	}

	@BeforeEach
	public void sampleLoggedinWallet() throws WalletException {
		walletService.login(1, "123Abc@df");
	}

	@Test
	public void walletValidationTest() throws RegistrationException {
		Wallet walletTestActual1 = walletService.validateWallet(1, "abc", 100.0, "123Abcd@");
		assertNotNull(walletTestActual1);
		Wallet walletTestActual2 = walletService.validateWallet(1, "abc", 100.0, "Abc@1234");
		assertNotNull(walletTestActual2);
		Wallet walletTestActual3 = new Wallet();
		walletTestActual3.setId(10);
		walletTestActual3.setName("XXX");
		walletTestActual3.setPassword("Xxx@12344");
		walletTestActual3.setBalance(2000.0);
		assertNotNull(walletTestActual3);
		System.out.println(walletTestActual3.toString());

	}

	@Test
	public void walletValidationExceptionTest() throws RegistrationException {

		assertThrows(RegistrationException.class, () -> walletService.validateWallet(1, "abc", 100.0, "123Abc@"));
		assertThrows(RegistrationException.class, () -> walletService.validateWallet(1, "abc", -100.0, "123Abcd@"));
		assertThrows(RegistrationException.class, () -> walletService.validateWallet(1, "abc1", 100.0, "123Abcd@"));

	}

	@Test
	public void registerWalletTest() throws WalletException {
		Wallet regWallet1 = new Wallet(10, "abc", 6000.0, "123Abcd@");
		assertNotNull(walletService.registerWallet(regWallet1));
		Wallet regWallet2 = new Wallet(20, "XYZ", 7000.0, "Abc@1234");
		assertNotNull(walletService.registerWallet(regWallet2));

	}

	@Test
	public void registerWalletExceptionTest() throws WalletException {
		Wallet regWallet1 = new Wallet(30, "abc", 3000.0, "123Abcd@");
		walletService.registerWallet(regWallet1);
		assertThrows(WalletException.class, () -> walletService.registerWallet(new Wallet(regWallet1.getId(),
				regWallet1.getName(), regWallet1.getBalance(), regWallet1.getPassword())));
		assertThrows(WalletException.class,
				() -> walletService.registerWallet(new Wallet(1, "abc", 1000.0, "123Abc@df")));

	}

	@Test
	public void loginTest() throws WalletException {
		walletService.logout();
		assertEquals(true, walletService.login(1, "123Abc@df"));
		walletService.logout();
		assertEquals(true, walletService.login(2, "5t55Dbcdf#"));

	}

	@Test
	public void loginExceptionTest() throws WalletException {
		assertThrows(WalletException.class, () -> walletService.login(1, "123Abc@dff"));
		assertThrows(WalletException.class, () -> walletService.login(10, "123Abc@df"));

	}

	@Test
	public void showBalanceTest() throws WalletException {
		walletService.logout();
		walletService.login(3, "123Abc@df");
		assertEquals(10000.0, walletService.showWalletBalance().doubleValue());
		assertNotEquals(1000.0, walletService.showWalletBalance().doubleValue());
		walletService.logout();
		assertThrows(WalletException.class, () -> walletService.showWalletBalance());

	}

	@Test
	public void showBalanceExceptionTest() throws WalletException {
		walletService.logout();
		assertThrows(WalletException.class, () -> walletService.showWalletBalance());

	}

	@Test
	public void addFundTest() throws WalletException {
		walletService.logout();
		walletService.login(4, "5t55Dbcdf#");
		assertEquals(10000.0, walletService.addFundsToWallet(2000.0).doubleValue());
		assertNotEquals(10000.0, walletService.addFundsToWallet(2000.0).doubleValue());
	}

	@Test
	public void addFundExceptionTest() throws WalletException {
		walletService.logout();
		walletService.login(4, "5t55Dbcdf#");
		assertThrows(WalletException.class, () -> walletService.addFundsToWallet(0.0));
		walletService.logout();
		assertThrows(WalletException.class, () -> walletService.addFundsToWallet(1000.0));
	}

	@Test
	public void withdrawFundTest() throws WalletException {
		walletService.logout();
		walletService.login(5, "123Abc@df");
		assertEquals(3000.0, walletService.withdrawFunds(1000.0).doubleValue());
		assertNotEquals(3000.0, walletService.withdrawFunds(100.0).doubleValue());
	}

	@Test
	public void withdrawFundExceptionTest() throws WalletException {
		walletService.logout();
		walletService.login(5, "123Abc@df");
		assertThrows(WalletException.class, () -> walletService.withdrawFunds(10000.0));
		assertThrows(WalletException.class, () -> walletService.withdrawFunds(-100.0));
		walletService.logout();
		assertThrows(WalletException.class, () -> walletService.withdrawFunds(100.0));
	}

	@Test
	public void fundTransferTest() throws WalletException {
		walletService.logout();
		walletService.login(6, "5t55Dbcdf#");
		assertEquals(true, walletService.fundTransfer(1, 100.0));
		assertNotEquals(false, walletService.fundTransfer(1, 100.0));

	}

	@Test
	public void fundTransferExceptionTest() throws WalletException {
		walletService.logout();
		walletService.login(6, "5t55Dbcdf#");
		assertThrows(WalletException.class, () -> walletService.fundTransfer(6, 10.0));
		assertThrows(WalletException.class, () -> walletService.fundTransfer(2, 10000.0));
		assertThrows(WalletException.class, () -> walletService.fundTransfer(2, -100.0));
		assertThrows(WalletException.class, () -> walletService.fundTransfer(45, 10.0));
		walletService.logout();
		assertThrows(WalletException.class, () -> walletService.fundTransfer(2, 50.0));

	}

	@Test
	public void logoutTest() throws WalletException {
		assertEquals(false, walletService.logout());
		walletService.login(2, "5t55Dbcdf#");
		assertNotEquals(true, walletService.logout());

	}

	@Test
	public void logoutExceptionTest() throws WalletException {
		walletService.logout();
		assertThrows(WalletException.class, () -> walletService.logout());

	}

	@Test
	public void DaoExceptionTest() throws WalletException {
		WalletDao walletRepository = new WalletDaoDBImpl(DaoUtility.getConnectionToMySQL());
		assertThrows(WalletException.class,
				() -> walletRepository.addWallet(new Wallet(1, "abc", 3000.0, "123Abc@df")));
		assertEquals(null, walletRepository.updateWallet(new Wallet(35, "abc", 3000.0, "123Abc@df")));
		assertEquals(null, walletRepository.deleteWalletById(35));
		assertEquals(null, walletRepository.getWalletById(35));
		WalletDao walletRepositoryexception = new WalletDaoDBImpl(null);
		assertEquals(null, walletRepositoryexception.getWalletById(1));
		assertEquals(null, walletRepositoryexception.deleteWalletById(1));
		assertEquals(null, walletRepositoryexception.updateWallet(new Wallet(1, "abcd", 3000.0, "123Abc@df")));
		assertEquals(null, walletRepositoryexception.addWallet(new Wallet(1, "abcd", 3000.0, "123Abc@df")));
	}

	@AfterAll
	public static void unregisterWalletTest() throws WalletException {
		WalletService walletService = new WalletServiceImpl();
		walletService.login(2, "5t55Dbcdf#");
		assertNull(walletService.unRegisterWallet("5t55Dbcdf#"));
	}

	@AfterAll
	public static void unregisterWalletExceptionTest() throws WalletException {
		WalletService walletService = new WalletServiceImpl();
		assertThrows(WalletException.class, () -> walletService.unRegisterWallet("123Abc@d"));
		walletService.login(1, "123Abc@df");
		assertThrows(WalletException.class, () -> walletService.unRegisterWallet("123Abc@"));
		walletService.unRegisterWallet("123Abc@df");
		assertThrows(WalletException.class, () -> walletService.unRegisterWallet("123Abc@df"));

	}

	@AfterAll
	public static void destroyTestData() throws WalletException {
		System.out.println("Clear all test data");
		WalletDao walletRepository = new WalletDaoDBImpl(DaoUtility.getConnectionToMySQL());
		walletRepository.deleteWalletById(3);
		walletRepository.deleteWalletById(4);
		walletRepository.deleteWalletById(5);
		walletRepository.deleteWalletById(6);
		walletRepository.deleteWalletById(10);
		walletRepository.deleteWalletById(20);
		walletRepository.deleteWalletById(30);
	}
}
