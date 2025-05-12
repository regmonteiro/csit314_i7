package com.i7.boundary.manager;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.i7.controller.platformManager.CreateCategoryController;
import com.i7.controller.platformManager.SearchCategoryController;
import com.i7.controller.platformManager.ViewCategoryController;
import com.i7.entity.ServiceCategory;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class ViewCategoriesPage {
    private CreateCategoryController createCategoryController = new CreateCategoryController();
    private ViewCategoryController viewCategoryController = new ViewCategoryController();

    private SearchCategoryController searchCategoryController = new SearchCategoryController();


    // CREATE
    @GetMapping("/createCategory")
    public String showCreateCategoryForm(Model model, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("service_category", new ServiceCategory());
        model.addAttribute("activePage", "createCategory");
        return "manager/createCategory";
    }    
    @PostMapping
    public String handleCreate(@ModelAttribute("newCategory") ServiceCategory newCategory, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
        ServiceCategory category = createCategoryController.createServiceCategory(
                newCategory.getId(), newCategory.getName(), newCategory.getDescription()
        );
    
        if (category != null) {
            model.addAttribute("success", "Category created successfully.");
            model.addAttribute("newCategory", new ServiceCategory());
        } else {
            model.addAttribute("error", "Category creation failed.");
        }
    
        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "createCategory");
        model.addAttribute("tab", "service_categories");
        model.addAttribute("category", createCategoryController.getCategories());
    
        return "manager/createCategory";
    }   

    // VIEW
    @GetMapping("/viewCategories")
    public String viewCategories(@RequestParam("name") String name, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login"; 
        }
    
        session.setAttribute("tab", "categories");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewCategory");
        ServiceCategory category = viewCategoryController.getCategoryByName(name);
        model.addAttribute("category", category);
        return "manager/viewCategory";
    }

    // SEARCH
    @GetMapping("/searchCategory")
    public String searchCategory(@RequestParam(required = false) String searchQuery,
                                Model model,
                                HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P004")) {
            return "redirect:/login";
        }
    
        List <ServiceCategory> results = new ArrayList<>();
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            results = searchCategoryController.searchCategories(searchQuery.trim());
        }
    
        model.addAttribute("user", user);
        model.addAttribute("results", results);
        model.addAttribute("activePage", "viewCategory");
        return "manager/viewCategory";
    }
}
