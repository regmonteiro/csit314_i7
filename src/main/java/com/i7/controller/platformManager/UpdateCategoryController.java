package com.i7.controller.platformManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.i7.entity.ServiceCategory;

@Controller
@RequestMapping("manager")
public class UpdateCategoryController {
    public boolean updateCategory(String name, ServiceCategory updatedCategory){
        return updateCategory(name, updatedCategory);
    }
    public ServiceCategory getCategoryByName(String name){
        return getCategoryByName(name);
    }
}
