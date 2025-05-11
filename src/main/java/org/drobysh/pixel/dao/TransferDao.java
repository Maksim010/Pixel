package org.drobysh.pixel.dao;

import jakarta.persistence.LockModeType;
import org.drobysh.pixel.model.Account;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface TransferDao {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findAccountById(Long id);

}
