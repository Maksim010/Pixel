package org.drobysh.pixel.services;

import jakarta.validation.constraints.Pattern;
import org.drobysh.pixel.dto.UserSearchCriteria;
import org.drobysh.pixel.dto.request.EmailDto;
import org.drobysh.pixel.dto.request.PhoneDto;
import org.drobysh.pixel.dto.response.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {

    String deleteEmail(Long id, String email);

    String updateEmail(Long id, EmailDto request);

    String createEmail(Long id, String email);

    String deletePhoneNumber(Long id, String phoneNumber);

    String updatePhone(Long userId, PhoneDto request);

    String createPhone(Long userId, @Pattern(regexp = "^\\d{11}$", message = "Телефон должен состоять из 11 цифр") String phone);

    Page<UserDto> getUsers(UserSearchCriteria criteria, int offset, int size);

    void increaseBalance();
}
