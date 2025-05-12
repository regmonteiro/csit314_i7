package com.i7.controller.platformManager;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.i7.entity.ServiceCategory;
@Controller
@RequestMapping("manager")
public class SearchCategoryController {
    public List<ServiceCategory> getCategories() {
        return ServiceCategory.fetchCategories();
    }

    public ServiceCategory getCategoryByName(String name){
        return getCategoryByName(name);
    }
    public List<ServiceCategory> searchCategories(String searchQuery){
        return ServiceCategory.searchByQuery(searchQuery);
    }
}
