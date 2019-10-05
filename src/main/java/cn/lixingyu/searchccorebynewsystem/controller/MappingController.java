package cn.lixingyu.searchccorebynewsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lxxxxxxy
 * @time 2019/10/05 12:57
 */
@Controller
public class MappingController {


    @RequestMapping(value = "/")
    public String toList(){
        return "index";
    }


}
