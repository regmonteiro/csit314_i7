package com.i7.controller.platformManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.i7.entity.ServiceCategory;

@Controller
@RequestMapping("manager")
public class UpdateCategoryController {
    public boolean updateCategory(int id, ServiceCategory category) {
        return ServiceCategory.updateCategory(id, category);
    }
    public ServiceCategory getCategoryByName(String name){
        return ServiceCategory.getCategoryByName(name);
    }
    public ServiceCategory getCategorybyId(int id){
        return getCategorybyId(id);
    }
}
