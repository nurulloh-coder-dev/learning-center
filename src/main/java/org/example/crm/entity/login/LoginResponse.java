package org.example.crm.entity.login;

import lombok.*;
import org.example.crm.entity.dto.IdNameDto;
import org.example.crm.entity.dto.organization.OrganizationViewDto;

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
    private List<OrganizationViewDto> organizations;
}
