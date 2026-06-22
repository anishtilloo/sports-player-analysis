package com.sports_analysis_app.sports_analysis_app.organization.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sports_analysis_app.sports_analysis_app.common.exception.ResourceNotFoundException;
import com.sports_analysis_app.sports_analysis_app.organization.dto.CreateOrganizationRequestDto;
import com.sports_analysis_app.sports_analysis_app.organization.dto.CreateOrganizationResponse;
import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;
import com.sports_analysis_app.sports_analysis_app.organization.repository.OrganizationRepository;
import com.sports_analysis_app.sports_analysis_app.user.dto.AuthResponse;
import com.sports_analysis_app.sports_analysis_app.user.entity.User;
import com.sports_analysis_app.sports_analysis_app.user.service.AuthService;
import com.sports_analysis_app.sports_analysis_app.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock private OrganizationRepository repository;
    @Mock private AuthService authService;
    @Mock private UserService userService;

    @InjectMocks
    private OrganizationService organizationService;

    @Test
    void getOrganizationByUid_found() {
        Organization org = new Organization();
        org.setName("Sports Club");
        when(repository.findByOrgUidContainingIgnoreCase("uid-123")).thenReturn(org);

        Organization result = organizationService.getOrganizationByUid("uid-123");

        assertEquals("Sports Club", result.getName());
    }

    @Test
    void getOrganizationByUid_notFound_throwsException() {
        when(repository.findByOrgUidContainingIgnoreCase("bad-uid")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> organizationService.getOrganizationByUid("bad-uid"));
    }

    @Test
    void createOrganization_success() {
        CreateOrganizationRequestDto dto = new CreateOrganizationRequestDto();
        dto.setName("Sports Club");
        dto.setAddress("123 Main St");
        dto.setEmail("owner@example.com");
        dto.setPassword("password123");
        dto.setUserName("Owner");

        AuthResponse authResponse = new AuthResponse(1L, "access", "refresh", "owner@example.com", "registered");
        User user = new User("uid", "Owner", "owner@example.com", "hashed", null, null);

        when(authService.registerUser(anyString(), anyString(), anyString())).thenReturn(authResponse);
        when(userService.getUserByEmail("owner@example.com")).thenReturn(user);
        when(repository.save(any(Organization.class))).thenAnswer(i -> i.getArgument(0));

        CreateOrganizationResponse response = organizationService.createOrganization(dto);

        assertNotNull(response);
        assertEquals("Sports Club", response.getName());
        assertNotNull(response.getUid());
    }
}
