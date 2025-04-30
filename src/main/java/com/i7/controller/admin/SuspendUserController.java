package com.i7.controller.admin;

import org.springframework.stereotype.Component;

import com.i7.entity.UserAccount;

@Component
public class SuspendUserController {
    public boolean suspendAccount(String uid) {
        return UserAccount.updateAccountStatus(uid, "suspended");
    }
}
