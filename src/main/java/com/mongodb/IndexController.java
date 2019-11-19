package com.mongodb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2019/11/15.
 */
@Controller
public class IndexController {
    @RequestMapping(value = {"","/","/index"})
    public String index(Model model) {
        return "/index";
    }
}
