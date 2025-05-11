package org.drobysh.pixel.services;

import org.drobysh.pixel.dto.request.AuthDto;

public interface AuthService {

    String authenticate(AuthDto authDto);
}
