package org.drobysh.pixel.service;

import org.drobysh.pixel.dao.TransferDao;
import org.drobysh.pixel.dto.request.TransferDto;
import org.drobysh.pixel.exceptions.NotFoundException;
import org.drobysh.pixel.model.Account;
import org.drobysh.pixel.model.User;
import org.drobysh.pixel.services.impl.TransferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.drobysh.pixel.TestData.createAccount;
import static org.drobysh.pixel.TestData.createAccount2;
import static org.drobysh.pixel.TestData.createInavalidTransferDto;
import static org.drobysh.pixel.TestData.createTransferDto;
import static org.drobysh.pixel.TestData.createTransferDtoForAmountException;
import static org.drobysh.pixel.TestData.createUser;
import static org.drobysh.pixel.TestData.createUser2;
import static org.drobysh.pixel.enums.AccountNotFoundExceptionMessage.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE;
import static org.drobysh.pixel.enums.TransferExceptionMessage.AMOUNT_FROM_EXCEPTION;
import static org.drobysh.pixel.enums.TransferExceptionMessage.WRONG_ID;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    TransferDao transferDao;

    @InjectMocks
    TransferServiceImpl transferService;

    private static final Long USER_ID = 1L;

    @Test
    void test_transfer_successfulTransfer_returnsCorrectBalance() {

        TransferDto transferDto = createTransferDto();
        User user = createUser();
        Account accountTo = createAccount(user);
        User user2 = createUser2();
        Account accountFrom = createAccount2(user2);

        when(transferDao.findAccountById(accountFrom.getId())).thenReturn(Optional.of(accountFrom));
        when(transferDao.findAccountById(transferDto.id())).thenReturn(Optional.of(accountTo));

        BigDecimal result = transferService.transfer(accountFrom.getId(), transferDto);

        Assertions.assertEquals(new BigDecimal("1100.00"), result);
        Assertions.assertEquals(new BigDecimal("900.00"), accountFrom.getBalance());
        Assertions.assertEquals(new BigDecimal("1100.00"), accountTo.getBalance());
        verify(transferDao).findAccountById(accountFrom.getId());
        verify(transferDao).findAccountById(accountTo.getId());
    }

    @Test
    void test_transfer_sameAccountIds_throwsIllegalArgumentException() {

        TransferDto transferDto = createTransferDto();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(USER_ID, transferDto));

        Assertions.assertEquals(WRONG_ID.getMessage(), exception.getMessage());
        verifyNoInteractions(transferDao);
    }

    @Test
    void test_transfer_accountToNotFound_throwsNotFoundException() {

        User user = createUser();
        Account accountFrom = createAccount(user);
        TransferDto transferDto = createInavalidTransferDto();

        when(transferDao.findAccountById(transferDto.id())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> transferService.transfer(accountFrom.getId(), transferDto));

        Assertions.assertEquals(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE.getMessage(), exception.getMessage());

        verify(transferDao).findAccountById(transferDto.id());
    }

    @Test
    void test_transfer_insufficientFunds_throwsIllegalArgumentException() {

        TransferDto transferDto = createTransferDtoForAmountException();
        User user = createUser();
        Account accountTo = createAccount(user);
        User user2 = createUser2();
        Account accountFrom = createAccount2(user2);

        when(transferDao.findAccountById(accountFrom.getId())).thenReturn(Optional.of(accountFrom));
        when(transferDao.findAccountById(accountTo.getId())).thenReturn(Optional.of(accountTo));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(accountFrom.getId(), transferDto));

        Assertions.assertEquals(AMOUNT_FROM_EXCEPTION.getMessage(), exception.getMessage());
        verify(transferDao).findAccountById(accountFrom.getId());
        verify(transferDao).findAccountById(accountTo.getId());
    }

}
