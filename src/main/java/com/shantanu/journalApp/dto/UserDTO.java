package com.shantanu.journalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private String email;
    private boolean sentimentAnalysis;

    // roles not needed in DTO, service will hardcode
}
