package com.i7.boundary.manager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.i7.controller.platformManager.DeleteCategoryController;
import com.i7.controller.platformManager.UpdateCategoryController;
import com.i7.entity.ServiceCategory;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("manager")
public class UpdateCategoriesPage {

    UpdateCategoryController updateCategoryController = new UpdateCategoryController();
    DeleteCategoryController deleteCategoryController = new DeleteCategoryController();

    @GetMapping("/updateCategory")
    public String updateCategory(@RequestParam("name") String name, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login"; // Redirect if no user is logged in
        }
        session.setAttribute("tab", "categories");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewCategory");

        ServiceCategory category = updateCategoryController.getCategoryByName(name);
        model.addAttribute("category", category);

        return "manager/updateCategory"; 
    }

    @PostMapping("/updateCategory")
    public String handleUpdate(@ModelAttribute ServiceCategory updatedCategory, RedirectAttributes redirectAttributes) {
        boolean success = updateCategoryController.updateCategory(updatedCategory.getName(), updatedCategory);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Category updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update category.");
        }
    
        return "redirect:/manager/viewCategory";
    }
    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam("name") String name, Model model, HttpSession session){
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login"; // Redirect if no user is logged in
        }
        session.setAttribute("tab", "categories");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "updateCategory");
        ServiceCategory category = deleteCategoryController.getCategoryByName(name);
        model.addAttribute("category", category);
        return "manager/updateCategory"; 
    }
    @PostMapping("/deleteCategory")
    public String handleDelete(@ModelAttribute ServiceCategory chosenCategory, RedirectAttributes redirectAttributes) {
        boolean success = deleteCategoryController.deleteCategory(chosenCategory.getName());
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Category deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete category.");
        }
    
        return "redirect:/manager/viewCategory";
    }

}
