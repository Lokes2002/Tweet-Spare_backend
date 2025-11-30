package com.baap.dto;

import lombok.Data;

@Data
public class SimpleUserDto {
    private Long id;
    private String fullName;
    private String email;
    private String image;
}
