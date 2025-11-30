package com.baap.service;

import java.util.List;

import com.baap.exception.UserException;
import com.baap.model.User;

public interface UserService {

    /**
     * Find a user by their ID.
     * @param userId ID of the user to find.
     * @return User object if found.
     * @throws UserException if user is not found.
     */
    User findUserById(Long userId) throws UserException;

    /**
     * Find user profile using JWT token.
     * @param jwt JWT token of the user.
     * @return User object if token is valid.
     * @throws UserException if token is invalid or user is not found.
     */
    User findUserProfileByJwt(String jwt) throws UserException;

    /**
     * Update user profile.
     * @param userId ID of the user to update.
     * @param req User object containing updated information.
     * @return Updated User object.
     * @throws UserException if user is not found or update fails.
     */
    User updateUserProfile(Long userId, User req) throws UserException;

    /**
     * Follow/unfollow another user.
     * @param userId ID of the user to follow/unfollow.
     * @param reqUser User object of the requesting user.
     * @return Updated User object of the followed/unfollowed user.
     * @throws UserException if user is not found or follow/unfollow action fails.
     */
    User followUser(Long userId, User reqUser) throws UserException;

    /**
     * Search for users based on a query string.
     * @param query Query string to search users.
     * @return List of User objects matching the query.
     */
    List<User> searchUser(String query);

    /**
     * Find user profile by another user's ID.
     * @param userId ID of the user to find profile.
     * @param reqUser User object of the requesting user.
     * @return User object of the profile user.
     * @throws UserException if user is not found.
     */
    User findUserProfileById(Long userId, User reqUser) throws UserException;
    
    public List<User> getFollowers(Long userId) throws UserException;
        // Logic to fetch followers
       
    public List<User> getFollowing(Long userId) throws UserException;
    
        // Logic to fetch following    

    public User unfollowUser(Long targetUserId, User reqUser) throws UserException;
    
    List<User> getAllMembers() throws UserException;
     

}
