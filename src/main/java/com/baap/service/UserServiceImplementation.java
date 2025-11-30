package com.baap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baap.Config.JwtProvider;
import com.baap.exception.UserException;
import com.baap.model.User;
import com.baap.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with id " + userId));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found with email " + email);
        }
        return user;
    }

    @Override
    public User updateUserProfile(Long userId, User req) throws UserException {
        User user = findUserById(userId);

        // Update user profile fields if provided in the request
        if (req.getFullName() != null) {
            user.setFullName(req.getFullName());
        }
        if (req.getImage() != null) {
            user.setImage(req.getImage());
        }
        if (req.getBackgroundImage() != null) {
            user.setBackgroundImage(req.getBackgroundImage());
        }
        if (req.getBirthDate() != null) {
            user.setBirthDate(req.getBirthDate());
        }
        if (req.getLocation() != null) {
            user.setLocation(req.getLocation());
        }
        if (req.getBio() != null) {
            user.setBio(req.getBio());
        }
        if (req.getWebsite() != null) {
            user.setWebsite(req.getWebsite());
        }

        // Save and return updated user profile
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User followUser(Long userId, User reqUser) throws UserException {
        // Fetch the user to be followed
        User followToUser = findUserById(userId);
        
        // Check if either user is null
        if (followToUser == null || reqUser == null) {
            throw new UserException("User not found.");
        }

        // Toggle follow/unfollow based on current relationship
        if (reqUser.getFollowing().contains(followToUser)) {
            reqUser.getFollowing().remove(followToUser);
            followToUser.getFollowers().remove(reqUser);
        } else {
            reqUser.getFollowing().add(followToUser);
            followToUser.getFollowers().add(reqUser);
        }

        // Save changes to both users
        userRepository.save(reqUser);
        userRepository.save(followToUser);

        return followToUser;
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }

    @Override
    public User findUserProfileById(Long userId, User reqUser) throws UserException {
        User userProfile = findUserById(userId);

        // Check if reqUser is allowed to view userProfile details
        if (!reqUser.equals(userProfile)) {
            throw new UserException("Unauthorized to view user profile");
        }

        return userProfile;
    }

	@Override
	public List<User> getFollowers(Long userId) throws UserException {
		 User user = findUserById(userId);
	        return user.getFollowers(); // Return the list of followers
	    }

	@Override
	public List<User> getFollowing(Long userId) throws UserException {
		  User user = findUserById(userId);
	        return user.getFollowing(); // Return the list of following users
	    }

	@Override
	public User unfollowUser(Long targetUserId, User reqUser) throws UserException {
		 User targetUser = findUserById(targetUserId);

	        if (targetUser == null || reqUser == null) {
	            throw new UserException("User not found.");
	        }

	        // Remove target user from the following list
	        if (reqUser.getFollowing().contains(targetUser)) {
	            reqUser.getFollowing().remove(targetUser);
	            targetUser.getFollowers().remove(reqUser);
	        } else {
	            throw new UserException("Not following this user.");
	        }

	        userRepository.save(reqUser);
	        userRepository.save(targetUser);

	        return targetUser;
	    }
	 @Override
	    public List<User> getAllMembers() throws UserException {
	        try {
	            List<User> allUsers = userRepository.findAll();
	            if (allUsers == null || allUsers.isEmpty()) {
	                throw new UserException("No users found.");
	            }
	            return allUsers;
	        } catch (Exception e) {
	            throw new UserException("Error retrieving all users.");
	        }
	    }
}
