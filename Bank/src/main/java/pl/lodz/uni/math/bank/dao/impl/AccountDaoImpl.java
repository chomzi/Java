package pl.lodz.uni.math.bank.dao.impl;

import pl.lodz.uni.math.bank.dao.AccountDao;
import pl.lodz.uni.math.bank.model.Account;
import pl.lodz.uni.math.bank.model.Client;

import org.apache.log4j.*;
import java.util.*;

public class AccountDaoImpl implements AccountDao {
	private static Logger logger = LogManager.getLogger(AccountDaoImpl.class);

	private static AccountDao instance;

	private List<Account> accounts;

	public AccountDaoImpl() {
		accounts = new ArrayList<Account>();
	}

	public static AccountDao getInstance() {
		if (instance == null) {
			instance = new AccountDaoImpl();
		}

		return instance;
	}

	public Account create(Account account) {
		try {
			accounts.add(account);
		} catch (IllegalArgumentException e) {
			logger.error("Problem with creating account");
		}

		return account;
	}

	public Account delete(Account account) {
		try {
			accounts.remove(account);
		} catch (IllegalArgumentException e) {
			logger.error("Problem with removal client");
		}

		return account;

	}

}
