package org.example.controltest7.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String phoneNumber;
    private boolean enabled;
}