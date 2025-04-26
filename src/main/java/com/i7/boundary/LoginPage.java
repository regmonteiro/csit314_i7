package com.i7.boundary;

import com.i7.controller.LoginController;
import com.i7.entity.UserAccount;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginPage {

    @Autowired
    private LoginController loginController;

    @GetMapping
    public String showLoginForm() {
        return "login";
    }

    @PostMapping
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
    
        UserAccount user = loginController.validateLogin(email, password);
    
        if (user == null) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }
    
        if ("suspended".equalsIgnoreCase(user.getStatus())) {
            model.addAttribute("error", "This account is suspended.");
            return "login";
        }
    
        session.setAttribute("user", user);
    
        return "redirect:/dashboard";
    }    
}
