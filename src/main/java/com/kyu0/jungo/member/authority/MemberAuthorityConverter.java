package com.kyu0.jungo.member.authority;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MemberAuthorityConverter implements AttributeConverter<MemberAuthority, String> {
    @Override
    public String convertToDatabaseColumn(MemberAuthority authority) {
        if (authority == null) {
            return null;
        }

        return authority.getName();
    }

    @Override
    public MemberAuthority convertToEntityAttribute(String authority) {
        if (authority == null) {
            return null;
        }
        
        return Stream.of(MemberAuthority.values())
            .filter(memberRole -> memberRole.getName().equals(authority))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
