package org.drobysh.pixel.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.drobysh.pixel.dao.TransferDao;
import org.drobysh.pixel.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TransferDaoImpl implements TransferDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Account> findAccountById(Long id) {

        return Optional.ofNullable(entityManager.createQuery(
                        "SELECT a FROM Account a WHERE a.user.id = :userId", Account.class)
                .setParameter("userId", id)
                .getSingleResult());
    }


}
