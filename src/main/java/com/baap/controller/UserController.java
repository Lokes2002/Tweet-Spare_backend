package com.baap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baap.dto.UserDto;
import com.baap.dto.mapper.UserDtoMapper;
import com.baap.exception.UserException;
import com.baap.model.User;
import com.baap.service.UserService;
import com.baap.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private User getRequestUser(String jwt) throws UserException {
        return userService.findUserProfileByJwt(jwt);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = getRequestUser(jwt);
        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setReqUser(true); // Mark this as the request user's profile
        return new ResponseEntity<>(userDto, HttpStatus.OK); // Use HttpStatus.OK for successful response
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId,
                                               @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = getRequestUser(jwt);
        User user = userService.findUserById(userId);
        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setReqUser(UserUtil.isReqUser(reqUser, user));
        userDto.setFollowed(UserUtil.isFollowedReqUser(reqUser, user)); // Corrected method name
        return new ResponseEntity<>(userDto, HttpStatus.OK); // Changed to OK status
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUser(@RequestParam String query,
                                                    @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = getRequestUser(jwt);
        List<User> users = userService.searchUser(query);
        List<UserDto> userDtos = UserDtoMapper.toUserDtos(users);
        return new ResponseEntity<>(userDtos, HttpStatus.OK); // Use HttpStatus.OK for consistency
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody User req,
                                                      @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = getRequestUser(jwt);
        User updatedUser = userService.updateUserProfile(reqUser.getId(), req);
        UserDto userDto = UserDtoMapper.toUserDto(updatedUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK); // Return OK instead of ACCEPTED
    }

    @PutMapping("/{userId}/follow")
    public ResponseEntity<UserDto> followUser(@PathVariable Long userId,
                                              @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = getRequestUser(jwt);
        User user = userService.followUser(userId, reqUser);
        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setFollowed(UserUtil.isFollowedReqUser(reqUser, user));
        return new ResponseEntity<>(userDto, HttpStatus.OK); // Return OK instead of ACCEPTED
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserDto>> getFollowers(@PathVariable Long userId,
                                                       @RequestHeader("Authorization") String jwt) throws UserException {
        logger.info("Fetching followers for userId {}", userId);
        User reqUser = getRequestUser(jwt);
        List<User> followers = userService.getFollowers(userId);
        List<UserDto> followerDtos = UserDtoMapper.toUserDtos(followers);
        return new ResponseEntity<>(followerDtos, HttpStatus.OK); // Use OK for followers response
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserDto>> getFollowing(@PathVariable Long userId,
                                                       @RequestHeader("Authorization") String jwt) throws UserException {
        logger.info("Fetching following for userId: {}", userId);
        User reqUser = getRequestUser(jwt);
        List<User> following = userService.getFollowing(userId);
        List<UserDto> followingDtos = UserDtoMapper.toUserDtos(following);
        return new ResponseEntity<>(followingDtos, HttpStatus.OK); // Use OK for following response
    }
    
    @GetMapping("/all-members")
    public ResponseEntity<List<UserDto>> getAllMembers(@RequestHeader("Authorization") String jwt) throws UserException {
        // Get the request user (for possible authentication or authorization purposes)
        User reqUser = getRequestUser(jwt);
        
        // Get all users from the service
        List<User> allUsers = userService.getAllMembers();
        
        // Map users to UserDto
        List<UserDto> userDtos = UserDtoMapper.toUserDtos(allUsers);
        
        return new ResponseEntity<>(userDtos, HttpStatus.OK); // Return OK instead of ACCEPTED
    }

    @PutMapping("/{userId}/unfollow")
    public ResponseEntity<UserDto> unfollowUser(@PathVariable Long userId,
                                                @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = getRequestUser(jwt);
        User user = userService.unfollowUser(userId, reqUser);
        UserDto userDto = UserDtoMapper.toUserDto(user);
        userDto.setFollowed(UserUtil.isFollowedReqUser(reqUser, user));
        return new ResponseEntity<>(userDto, HttpStatus.OK); // Return OK instead of ACCEPTED
    }
}
