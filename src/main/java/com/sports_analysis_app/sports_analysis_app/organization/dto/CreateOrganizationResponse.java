package com.sports_analysis_app.sports_analysis_app.organization.dto;

public class CreateOrganizationResponse {
    private String uid;
    private String name;

    public CreateOrganizationResponse(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
