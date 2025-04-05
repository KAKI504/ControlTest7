package org.example.controltest7.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String phoneNumber;
    private String username;
    private String password;
    private boolean enabled;
    private boolean blocked;
}