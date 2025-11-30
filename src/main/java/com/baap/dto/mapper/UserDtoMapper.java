package com.baap.dto.mapper;

import com.baap.dto.UserDto;
import com.baap.dto.SimpleUserDto;
import com.baap.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDtoMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null; // Handle null user appropriately
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setLocation(user.getLocation());
        userDto.setWebsite(user.getWebsite());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setEmail(user.getEmail());
        userDto.setMobile(user.getMobile());
        userDto.setImage(user.getImage());
        userDto.setVideo(user.getVideo());
        userDto.setBackgroundImage(user.getBackgroundImage());
        userDto.setBio(user.getBio());
        userDto.setReqUser(user.isReqUser());
        userDto.setLoginWithGoogle(user.isLoginWithGoogle());

        // Map followers and following using SimpleUserDto
        userDto.setFollowers(toSimpleUserDtos(user.getFollowers()));
        userDto.setFollowing(toSimpleUserDtos(user.getFollowing()));

        return userDto;
    }

    public static List<UserDto> toUserDtos(List<User> users) {
        return users.stream().map(UserDtoMapper::toUserDto).collect(Collectors.toList());
    }

    public static SimpleUserDto toSimpleUserDto(User user) {
        if (user == null) {
            return null; // Handle null user appropriately
        }
        SimpleUserDto simpleUserDto = new SimpleUserDto();
        simpleUserDto.setId(user.getId());
        simpleUserDto.setFullName(user.getFullName());
        simpleUserDto.setEmail(user.getEmail());
        simpleUserDto.setImage(user.getImage());
        return simpleUserDto;
    }

    public static List<SimpleUserDto> toSimpleUserDtos(List<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        return users.stream().map(UserDtoMapper::toSimpleUserDto).collect(Collectors.toList());
    }
}
