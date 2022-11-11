package com.kyu0.jungo.member.authority;

public enum MemberAuthority {
    USER("USER"), ADMIN("ADMIN");

    private String name;

    private MemberAuthority(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
