package com.baap.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String image;
    private String location;
    private String website;
    private String birthDate;
    private String video;
    private String mobile;
    private String backgroundImage;
    private String bio;
    private boolean reqUser;
    private boolean loginWithGoogle;

    private List<SimpleUserDto> followers = new ArrayList<>();
    private List<SimpleUserDto> following = new ArrayList<>();

    private boolean followed;
    private boolean verified;
}
