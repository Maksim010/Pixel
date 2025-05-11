package org.drobysh.pixel.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.drobysh.pixel.dao.AccountDao;
import org.drobysh.pixel.model.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Account> findAccountsForUpdate() {
        return entityManager.createQuery(
            "SELECT a FROM Account a WHERE a.balance < a.startBalance * 2.07 OR a.balance is null", Account.class)
                .getResultList();
    }

    @Override
    public void saveAll(List<Account> accounts) {
        for (Account account : accounts) {
            entityManager.persist(account);
        }
    }
}
