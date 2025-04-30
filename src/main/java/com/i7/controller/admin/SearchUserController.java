package com.i7.controller.admin;

import java.util.List;

import com.i7.entity.UserAccount;

public class SearchUserController {
        public List<UserAccount> searchUserAccounts(String uid) {
        return UserAccount.searchByQuery(uid);
    }
}
