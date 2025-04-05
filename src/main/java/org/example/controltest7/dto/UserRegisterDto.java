package org.example.controltest7.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "\\(996 \\d{3}\\) \\d{2}-\\d{2}-\\d{2}", message = "Номер телефона должен быть в формате (996 XXX) XX-XX-XX")
    private String phoneNumber;
}