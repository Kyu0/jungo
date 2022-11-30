package com.kyu0.jungo.rest.member.role;

public enum MemberRole {
    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN");

    private String name;
    private final static String PREFIX = "ROLE_";

    private MemberRole(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getNameWithoutPrefix() {
        return this.name.substring(PREFIX.length());
    }
}
