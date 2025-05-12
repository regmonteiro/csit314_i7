package com.i7.controller.platformManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.i7.entity.ServiceCategory;

@Controller
@RequestMapping("manager")
public class DeleteCategoryController {
    public boolean deleteCategory(int id, ServiceCategory category){
        return ServiceCategory.deleteCategory(id, category);
    }
    public ServiceCategory getCategoryByName(String name){
        return ServiceCategory.getCategoryByName(name);
    }
    public ServiceCategory getCategoryById(int id){
        return ServiceCategory.getCategoryById(id);
    }
}
