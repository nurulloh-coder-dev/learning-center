package org.example.crm.entity.login;

import lombok.*;
import org.example.crm.entity.dto.IdNameDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private Long expiry;
    private boolean requiresOrganizationSelection;
    private List<IdNameDto> organizations;
}
