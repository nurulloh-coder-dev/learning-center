package org.example.crm.annotation;

import org.example.crm.config.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("orgAuditorAware")
public class OrganizationAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails custom) {
            return Optional.ofNullable(custom.getOrganizationId());
        }
        
        return Optional.empty();
    }
}