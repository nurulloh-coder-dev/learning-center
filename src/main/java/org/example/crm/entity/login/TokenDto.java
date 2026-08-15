package org.example.crm.entity.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String token;
    private Long expiry;
}