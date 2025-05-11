package org.drobysh.pixel;

import org.drobysh.pixel.dto.request.TransferDto;
import org.drobysh.pixel.model.Account;
import org.drobysh.pixel.model.EmailData;
import org.drobysh.pixel.model.PhoneData;
import org.drobysh.pixel.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    private static final Long USER_ID_1 = 1L;
    private static final Long INVALID_USER_ID_1 = 1123123L;
    private static final Long USER_ID_2 = 2L;
    private static final Long ACCOUNT_ID = 1L;
    private static final Long ACCOUNT_ID_2 = 2L;
    private static final Long PHONE_ID = 1L;
    private static final Long EMAIL_ID = 1L;
    private static final Long PHONE_ID_2 = 2L;
    private static final Long EMAIL_ID_2 = 2L;
    private static final BigDecimal AMOUNT_FOR_TRANSFER = BigDecimal.valueOf(100.00);
    private static final BigDecimal INVALID_AMOUNT_FOR_TRANSFER = BigDecimal.valueOf(10000000.00);
    private static final BigDecimal START_BALANCE = BigDecimal.valueOf(1000.00);
    private static final BigDecimal BALANCE = BigDecimal.valueOf(1000.00);
    private static final String NAME = "IVAN";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
    private static final String PASSWORD = "1234567891212";
    private static final String EMAIL = "max_dro01@mail.ru";
    private static final String PHONE_NUMBER = "12345678910";
    private static final String EMAIL_2 = "max_dro101@mail.ru";
    private static final String PHONE_NUMBER_2 = "73345678910";

    public static TransferDto createTransferDto() {
        return TransferDto.builder()
                .id(USER_ID_1)
                .amount(AMOUNT_FOR_TRANSFER)
                .build();
    }

    public static TransferDto createInavalidTransferDto() {
        return TransferDto.builder()
                .id(INVALID_USER_ID_1)
                .amount(AMOUNT_FOR_TRANSFER)
                .build();
    }

    public static TransferDto createTransferDtoForAmountException() {
        return TransferDto.builder()
                .id(USER_ID_1)
                .amount(INVALID_AMOUNT_FOR_TRANSFER)
                .build();
    }

    public static Account createAccount(User user) {
        return Account.builder()
                .id(ACCOUNT_ID)
                .startBalance(START_BALANCE)
                .balance(BALANCE)
                .user(user)
                .build();
    }

    public static User createUser() {
        User user = User.builder()
                .id(USER_ID_2)
                .name(NAME)
                .dateOfBirth(DATE_OF_BIRTH)
                .password(PASSWORD)
                .emails(createEmailData())
                .phones(createPhoneData())
                .build();


        Account account = createAccount(user);
        user.setAccount(account);

        return user;
    }

    public static Account createAccount2(User user) {
        return Account.builder()
                .id(ACCOUNT_ID_2)
                .startBalance(START_BALANCE)
                .balance(BALANCE)
                .user(user)
                .build();
    }

    public static User createUser2() {
        User user = User.builder()
                .id(USER_ID_1)
                .name(NAME)
                .dateOfBirth(DATE_OF_BIRTH)
                .password(PASSWORD)
                .emails(createEmailData2())
                .phones(createPhoneData2())
                .build();


        Account account = createAccount(user);
        user.setAccount(account);

        return user;
    }

    public static List<EmailData> createEmailData2() {
        List<EmailData> emailDataList = new ArrayList<>();
        emailDataList.add(EmailData.builder()
                .email(EMAIL_2)
                .id(EMAIL_ID_2)
                .build());
        return emailDataList;
    }

    public static List<PhoneData> createPhoneData2() {
        List<PhoneData> phoneDataList = new ArrayList<>();
        phoneDataList.add(PhoneData.builder()
                .phone(PHONE_NUMBER_2)
                .id(PHONE_ID_2)
                .build());
        return phoneDataList;
    }

    public static List<EmailData> createEmailData() {
        List<EmailData> emailDataList = new ArrayList<>();
        emailDataList.add(EmailData.builder()
                .email(EMAIL)
                .id(EMAIL_ID)
                .build());
        return emailDataList;
    }

    public static List<PhoneData> createPhoneData() {
        List<PhoneData> phoneDataList = new ArrayList<>();
        phoneDataList.add(PhoneData.builder()
                .phone(PHONE_NUMBER)
                .id(PHONE_ID)
                .build());
        return phoneDataList;
    }
}