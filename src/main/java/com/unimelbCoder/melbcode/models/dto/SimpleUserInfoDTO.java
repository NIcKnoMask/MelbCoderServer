package com.unimelbCoder.melbcode.models.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpleUserInfoDTO {
    private static final long serialVersionUID = 4802653694786272120L;

    private String username;
    private int age;
    private String role;
}
