package com.sports_analysis_app.sports_analysis_app.team.dto;

import jakarta.validation.constraints.NotBlank;

public class TeamRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Organization UID is required")
    private String orgUid;

    public TeamRequest() {}

    public TeamRequest(String name, String orgUid) {
        this.name = name;
        this.orgUid = orgUid;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOrgUid() { return orgUid; }
    public void setOrgUid(String orgUid) { this.orgUid = orgUid; }
}
