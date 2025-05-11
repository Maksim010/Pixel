package org.drobysh.pixel.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(length = 500)
    private String name;

    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @Column(length = 500)
    @Pattern(regexp = "^.{8,500}$", message = "Пароль должен содержать не менее 8 и не более 500 символов")
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @NotNull
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotEmpty
    private List<EmailData> emails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotEmpty
    private List<PhoneData> phones;

}


