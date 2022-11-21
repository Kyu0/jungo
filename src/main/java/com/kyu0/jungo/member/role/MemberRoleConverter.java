package com.kyu0.jungo.member.role;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MemberRoleConverter implements AttributeConverter<MemberRole, String> {
    @Override
    public String convertToDatabaseColumn(MemberRole role) {
        if (role == null) {
            return null;
        }

        return role.getName();
    }

    @Override
    public MemberRole convertToEntityAttribute(String role) {
        if (role == null) {
            return null;
        }
        
        return Stream.of(MemberRole.values())
            .filter(memberRole -> memberRole.getName().equals(role))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
