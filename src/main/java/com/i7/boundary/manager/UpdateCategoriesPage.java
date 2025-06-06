package com.i7.boundary.manager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.i7.controller.platformManager.SuspendCategoryController;
import com.i7.controller.platformManager.UpdateCategoryController;
import com.i7.entity.ServiceCategory;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class UpdateCategoriesPage {

    UpdateCategoryController updateCategoryController = new UpdateCategoryController();
    SuspendCategoryController suspendCategoryController = new SuspendCategoryController();

    @GetMapping("/updateCategory")
    public String showUpdateForm(@RequestParam("name") String name,
                                    Model model,
                                    HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
        session.setAttribute("tab", "categories");
        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "viewSingleCategory");
        ServiceCategory category = updateCategoryController.getCategoryByName(name);
        model.addAttribute("category", category);
        return "manager/updateCategory";
    }

    @PostMapping("/updateCategory")
    public String handleUpdate(@ModelAttribute("category") ServiceCategory category,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
        boolean success = updateCategoryController.updateCategory(
                category.getId(),
                category
        );
        if (success) {  
            redirectAttributes.addFlashAttribute("message", "Category updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update category.");
        }
        return "redirect:/manager/viewSingleCategory?id=" + category.getId();
    }
    @GetMapping("/suspendCategory")
    public String suspendCategory(@RequestParam("id") int id, Model model, HttpSession session){
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login"; // Redirect if no user is logged in
        }
        session.setAttribute("tab", "categories");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "deleteCategory");
        ServiceCategory category = suspendCategoryController.getCategoryById(id);
        model.addAttribute("category", category);
        return "manager/suspendCategory"; 
    }
    @PostMapping("/suspendCategory")
    public String handleSuspend(@ModelAttribute("category") ServiceCategory chosenCategory, HttpSession session, RedirectAttributes redirectAttributes) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
        boolean success = suspendCategoryController.suspendCategory(chosenCategory.getId(), chosenCategory);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Category suspended successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to suspend category.");
        }
        return "redirect:/manager/viewCategory";
    }

}
