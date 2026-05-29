package com.sports_analysis_app.sports_analysis_app.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sports_analysis_app.sports_analysis_app.organization.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization findByUidContainingIgnoreCase(String uid);
}
