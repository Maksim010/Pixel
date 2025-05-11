package org.drobysh.pixel.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drobysh.pixel.dao.AccountDao;
import org.drobysh.pixel.dao.UserDao;
import org.drobysh.pixel.dto.UserSearchCriteria;
import org.drobysh.pixel.dto.request.EmailDto;
import org.drobysh.pixel.dto.request.PhoneDto;
import org.drobysh.pixel.dto.response.UserDto;
import org.drobysh.pixel.enums.EmailBadRequestExceptionMessage;
import org.drobysh.pixel.enums.EmailNotFoundExceptionMessage;
import org.drobysh.pixel.enums.PhoneBadRequestExceptionMessage;
import org.drobysh.pixel.enums.PhoneNotFoundExceptionMessage;
import org.drobysh.pixel.enums.UserNotFoundExceptionMessage;
import org.drobysh.pixel.exceptions.BadRequestException;
import org.drobysh.pixel.exceptions.NotFoundException;
import org.drobysh.pixel.model.Account;
import org.drobysh.pixel.model.EmailData;
import org.drobysh.pixel.model.PhoneData;
import org.drobysh.pixel.model.User;
import org.drobysh.pixel.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AccountDao accountDao;

    private final UserDao userDao;

    private static final BigDecimal PERCENT_190 = new BigDecimal("2.90");

    private static final BigDecimal PERCENT_207 = new BigDecimal("3.07");

    private static final BigDecimal INCREASED_10_PERCENT = new BigDecimal("1.10");

    @Override
    @Transactional
    public String deleteEmail(Long userId, String email) {
        log.info("deleteEmail is started with email - {} and userId - {}", email, userId);
        List<String> emails = userDao.findEmailsByUserId(userId)
                .orElseThrow(() -> new NotFoundException(EmailNotFoundExceptionMessage.EMAIL_NOT_FOUND.getMessage()));
        if (emails.size() > 1 && emails.contains(email)) {
            userDao.removeEmail(userId, email);
        } else {
            throw new BadRequestException(EmailBadRequestExceptionMessage.ONLY_ONE_EMAIL.getMessage());
        }
        return email;
    }

    @Override
    @Transactional
    public String updateEmail(Long userId, EmailDto request) {
        log.info("updateEmail is started with userId - {} and request - {} ", userId, request);
        EmailData emailData = userDao.findEmail(userId, request.email()).orElseThrow(
                () -> new NotFoundException(EmailNotFoundExceptionMessage.EMAIL_NOT_FOUND.getMessage())
        );
        if (!emailData.getEmail().equals(request.newEmail())) {
            emailData.setEmail(request.newEmail());
        } else {
            throw new BadRequestException(EmailBadRequestExceptionMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }
        return emailData.getEmail();
    }

    @Override
    @Transactional
    public String createEmail(Long userId, String email) {
        log.info("createEmail is started with userId - {} and email - {} ", userId, email);
        User user = userDao.findUserById(userId).orElseThrow(
                () -> new NotFoundException(UserNotFoundExceptionMessage.USER_NOT_FOUND.getMessage())
        );
        if (userDao.existsByEmail(email)){
            throw new BadRequestException(EmailBadRequestExceptionMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

        EmailData emailData = createEmailData(email, user);
        return emailData.getEmail();
    }

    @Override
    @Transactional
    public String deletePhoneNumber(Long userId, String phoneNumber) {
        log.info("deletePhoneNumber is started with phone - {} and userId - {}", phoneNumber, userId);
        List<String> phones = userDao.findPhonesByUserId(userId)
                .orElseThrow(() -> new NotFoundException(PhoneNotFoundExceptionMessage.PHONE_NOT_FOUND.getMessage()));
        if (phones.size() > 1 && phones.contains(phoneNumber)) {
            userDao.removePhone(userId, phoneNumber);
        } else {
            throw new BadRequestException(PhoneBadRequestExceptionMessage.ONLY_ONE_PHONE.getMessage());
        }
        return phoneNumber;
    }

    @Override
    @Transactional
    public String updatePhone(Long userId, PhoneDto request) {
        log.info("updatePhone is started with userId - {} and request - {} ", userId, request);
        PhoneData phoneData = userDao.findPhone(userId, request.phone()).orElseThrow(
                () -> new NotFoundException(PhoneNotFoundExceptionMessage.PHONE_NOT_FOUND.getMessage())
        );
        if (!Objects.equals(phoneData.getPhone(), request.newPhone())) {
            phoneData.setPhone(request.newPhone());
        } else {
            throw new BadRequestException(PhoneBadRequestExceptionMessage.PHONE_ALREADY_EXISTS.getMessage());
        }
        return phoneData.getPhone();
    }

    @Override
    @Transactional
    public String createPhone(Long userId, String phone) {
        log.info("createPhone is started with userId - {} and phone - {} ", userId, phone);
        User user = userDao.findUserById(userId).orElseThrow(
                () -> new NotFoundException(UserNotFoundExceptionMessage.USER_NOT_FOUND.getMessage())
        );

        if (userDao.existsByPhone(phone)){
            throw new BadRequestException(PhoneBadRequestExceptionMessage.PHONE_ALREADY_EXISTS.getMessage());
        }

        PhoneData phoneData = createPhoneData(phone, user);
        return phoneData.getPhone();
    }

    @Override
    public Page<UserDto> getUsers(UserSearchCriteria criteria, int offset, int size) {
        List<User> users = userDao.findUsersByFilters(criteria, offset, size);

        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(userDtos);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0/30 * * * * *")
    public void increaseBalance() {

        log.info("Started increaseBalance");

        List<Account> accounts = accountDao.findAccountsForUpdate();
        if (CollectionUtils.isEmpty(accounts)){
            return;
        }

        for (Account account : accounts) {
            if (account.getBalance() == null) {
                account.setBalance(account.getStartBalance());
            }

            BigDecimal startBalance = account.getStartBalance();

            BigDecimal currentBalance = account.getBalance();

            BigDecimal amount190Percent = getAmountFor190Percent(startBalance);

            BigDecimal amount207Percent = getAmountFor207Percent(startBalance);

            BigDecimal increasedBy10Percent = getIncreasedBy10Percent(currentBalance);


            if (currentBalance.compareTo(amount190Percent) <= 0) {
                // Если после увеличения на 10% превысим 207% - ставим 207%
                if (increasedBy10Percent.compareTo(amount207Percent) > 0) {
                    account.setBalance(amount207Percent);
                } else {
                    // Иначе просто увеличиваем на 10%
                    account.setBalance(increasedBy10Percent);
                }
            } else if (currentBalance.compareTo(amount207Percent) < 0) {
                // Если между 190% и 207% - ставим 207%
                account.setBalance(amount207Percent);
            }
        }
        accountDao.saveAll(accounts);
    }

    private BigDecimal getAmountFor190Percent(BigDecimal startBalance) {
        return startBalance.multiply(PERCENT_190)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getAmountFor207Percent(BigDecimal startBalance) {
        return startBalance.multiply(PERCENT_207)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getIncreasedBy10Percent (BigDecimal currentBalance) {
        return currentBalance.multiply(INCREASED_10_PERCENT)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmails().stream().map(EmailData::getEmail).collect(Collectors.joining(" ,")))
                .phone(user.getPhones().stream().map(PhoneData::getPhone).collect(Collectors.joining(" ,")))
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }


    private EmailData createEmailData(String email, User user) {
        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        emailData.setUser(user);
        userDao.save(emailData);
        return emailData;
    }

    private PhoneData createPhoneData(String phone, User user) {
        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        phoneData.setUser(user);
        userDao.save(phoneData);
        return phoneData;
}
}
