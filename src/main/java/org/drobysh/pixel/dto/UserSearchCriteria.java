package org.drobysh.pixel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSearchCriteria {

    LocalDate dateOfBirth;

    String phone;

    String name;

    String email;
}
