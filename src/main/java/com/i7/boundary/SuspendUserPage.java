package com.i7.boundary;

import com.i7.controller.UserManageController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/suspendUser")
public class SuspendUserPage {

    @Autowired
    private UserManageController userManageController;

    @PostMapping
    public String suspendUser(@RequestParam("uid") String uid) {
        userManageController.suspendUser(uid);
        return "redirect:/admin/viewUser?uid=" + uid;
    }
}