package org.drobysh.pixel.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drobysh.pixel.dao.UserDao;
import org.drobysh.pixel.dto.request.AuthDto;
import org.drobysh.pixel.enums.EmailNotFoundExceptionMessage;
import org.drobysh.pixel.enums.PasswordBadRequestExceptionMessage;
import org.drobysh.pixel.enums.PhoneNotFoundExceptionMessage;
import org.drobysh.pixel.exceptions.BadRequestException;
import org.drobysh.pixel.exceptions.NotFoundException;
import org.drobysh.pixel.model.User;
import org.drobysh.pixel.services.AuthService;
import org.drobysh.pixel.services.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final UserDao userDao;

    @Override
    @Transactional
    public String authenticate(AuthDto request) {

        User user = request.email() != null
                ? userDao.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(EmailNotFoundExceptionMessage.EMAIL_NOT_FOUND.getMessage()))
                : userDao.findByPhone(request.phone())
                .orElseThrow(() -> new NotFoundException(PhoneNotFoundExceptionMessage.PHONE_NOT_FOUND.getMessage()));
        validPassword(request, user);

        return jwtService.generateToken(user.getId());
    }

    private void validPassword(AuthDto request, User user) {

        if (!user.getPassword().equals(request.password())) {
            throw new BadRequestException(PasswordBadRequestExceptionMessage.WRONG_PASSWORD.getMessage());
        }
    }
}
