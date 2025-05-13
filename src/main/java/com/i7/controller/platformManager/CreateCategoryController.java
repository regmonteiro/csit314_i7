package com.i7.controller.platformManager;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.i7.entity.ServiceCategory;

@Controller
@RequestMapping("manager")
public class CreateCategoryController {

    public ServiceCategory createServiceCategory(int id, String name, String description) {
        boolean success = ServiceCategory.createCategory(id, name, description);
        if (success) {
            return ServiceCategory.getCategoryByName(name);
        } else {
            return null;
        }
    }
    public List<ServiceCategory> getCategories() {
        return ServiceCategory.fetchCategories();
    }
}
