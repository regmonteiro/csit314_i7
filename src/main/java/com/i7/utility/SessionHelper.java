package com.i7.utility;

import com.i7.entity.UserAccount;
import jakarta.servlet.http.HttpSession;

public class SessionHelper {
    public static UserAccount getLoggedInUser(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj instanceof UserAccount) {
            return (UserAccount) userObj;
        }
        return null;
    }
}
