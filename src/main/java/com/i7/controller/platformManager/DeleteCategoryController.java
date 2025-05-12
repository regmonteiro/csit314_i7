package com.i7.controller.platformManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.i7.entity.ServiceCategory;

@Controller
@RequestMapping("manager")
public class DeleteCategoryController {
    public boolean deleteCategory(String name){
        return deleteCategory(name);
    }
    public ServiceCategory getCategoryByName(String name){
        return getCategoryByName(name);
    }
}
