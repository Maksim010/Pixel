package org.drobysh.pixel.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drobysh.pixel.dao.TransferDao;
import org.drobysh.pixel.dto.request.TransferDto;
import org.drobysh.pixel.enums.AccountNotFoundExceptionMessage;
import org.drobysh.pixel.enums.TransferExceptionMessage;
import org.drobysh.pixel.exceptions.NotFoundException;
import org.drobysh.pixel.model.Account;
import org.drobysh.pixel.services.TransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferDao transferDao;

    public static final int MONEY_SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BigDecimal transfer(Long idFrom, TransferDto transferDto) {

        log.info("transfer from {} to {}", idFrom, transferDto);

        if (idFrom.equals(transferDto.id()))
            throw new IllegalArgumentException(TransferExceptionMessage.WRONG_ID.getMessage());

        Account accountTo = findAccount(transferDto.id());

        Account accountFrom = findAccount(idFrom);

        findAndCheckAmount(accountFrom, transferDto);

        BigDecimal amountFromAfterTransfer = accountFrom.getBalance().subtract(transferDto.amount())
                .setScale(MONEY_SCALE,ROUNDING_MODE);

        BigDecimal amountToAfterTransfer = accountTo.getBalance().add(transferDto.amount())
                .setScale(MONEY_SCALE,ROUNDING_MODE);

        accountFrom.setBalance(amountFromAfterTransfer);

        accountTo.setBalance(amountToAfterTransfer);

        return amountToAfterTransfer;
    }

    private void findAndCheckAmount(Account accountFrom, TransferDto transferDto) {

        if (accountFrom.getBalance().compareTo(transferDto.amount()) < 0) {
            throw new IllegalArgumentException(TransferExceptionMessage.AMOUNT_FROM_EXCEPTION.getMessage());
        }
    }

    private Account findAccount(Long id) {
        return transferDao.findAccountById(id).orElseThrow(
                () -> new NotFoundException(AccountNotFoundExceptionMessage
                        .ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE.getMessage())
        );
    }
}
