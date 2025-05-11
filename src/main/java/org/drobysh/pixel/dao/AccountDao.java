package org.drobysh.pixel.dao;

import org.drobysh.pixel.model.Account;

import java.util.List;

public interface AccountDao {

    List<Account> findAccountsForUpdate();

    void saveAll(List<Account> accounts);
}
