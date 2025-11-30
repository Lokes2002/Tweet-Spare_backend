package com.baap.util;

import com.baap.model.User;

public class UserUtil {

   
    public static boolean isReqUser(User reqUser, User user) {
        if (reqUser == null || user == null) {
            return false;
        }
        return reqUser.getId().equals(user.getId());
    }
    public static boolean isFollowedReqUser(User reqUser, User user) {
        if (reqUser == null || user == null) {
            return false;
        }
        // Ensure that followers are not null
        if (user.getFollowers() == null) {
            return false;
        }
        return user.getFollowers().contains(reqUser);
    }
}
