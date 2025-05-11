package org.drobysh.pixel.dao;

import org.drobysh.pixel.dto.UserSearchCriteria;
import org.drobysh.pixel.model.EmailData;
import org.drobysh.pixel.model.PhoneData;
import org.drobysh.pixel.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    void removeEmail(Long id, String email);

    List<User> findUsersByFilters(UserSearchCriteria criteria, int offset, int size);

    Long countUsersByFilters(UserSearchCriteria criteria);

    void removePhone(Long id, String phone);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<List<String>> findEmailsByUserId(Long id);

    Optional<List<String>> findPhonesByUserId(Long id);

    Optional<EmailData> findEmail(Long userId, String email);

    Optional<PhoneData> findPhone(Long userId, String email);

    Optional<User> findUserById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    void save(EmailData emailData);

    void save(PhoneData phoneData);

}
