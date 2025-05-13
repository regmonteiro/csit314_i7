package com.i7.boundary.manager;
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
    private ViewCategoryController viewCategoryController = new ViewCategoryController(); // for single category
    private SearchCategoryController searchCategoryController = new SearchCategoryController();


    // CREATE
    @GetMapping("/createCategory")
    public String showCreateCategoryForm(Model model, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("category", new ServiceCategory());
        model.addAttribute("activePage", "createCategory");
        return "manager/createCategory";
    }    
    @PostMapping("/createCategory")
    public String handleCreate(@ModelAttribute("category") ServiceCategory category, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
        category = createCategoryController.createServiceCategory(
                category.getId(), category.getName(), category.getDescription()
        );
    
        if (category != null) {
            model.addAttribute("success", "Category created successfully.");
            model.addAttribute("category", new ServiceCategory());
        } else {
            model.addAttribute("error", "Category creation failed.");
        }
    
        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "createCategory");
        model.addAttribute("tab", "service_categories");
        model.addAttribute("category", createCategoryController.getCategories());
    
        return "redirect:/manager/viewCategory";
    }   

    // VIEW
    @GetMapping("/viewCategory")
    public String viewCategories(@RequestParam(value="searchQuery", required=false) String searchQuery, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P004")) {
            return "redirect:/login";
        }
        //search
        List<ServiceCategory> categories;
        if (searchQuery != null && !searchQuery.isBlank()) {
            categories = searchCategoryController.searchCategories(searchQuery);
        } else {
            categories = searchCategoryController.getCategories();
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewCategory");
        model.addAttribute("categories", categories);
        model.addAttribute("searchQuery", searchQuery);
        return "manager/viewCategory";
    }
    // View Single Category
    @GetMapping("/viewSingleCategory")
    public String viewSingleCategory(@RequestParam("id") int id, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        ServiceCategory category = viewCategoryController.getCategoryById(id);
        
        if (category == null) {
            model.addAttribute("error", "Category not found.");
            return "redirect:/manager/viewCategory";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewCategory");
        model.addAttribute("category", category);

        return "manager/viewSingleCategory";
    }
    
}

