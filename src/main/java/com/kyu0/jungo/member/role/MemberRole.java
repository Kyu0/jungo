package com.kyu0.jungo.member.role;

public enum MemberRole {
    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN");

    private String name;

    private MemberRole(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
