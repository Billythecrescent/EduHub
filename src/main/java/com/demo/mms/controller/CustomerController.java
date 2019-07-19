package com.demo.mms.controller;

import com.demo.mms.common.domain.Customer;
import com.demo.mms.common.utils.IDGenerator;
import com.demo.mms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping("/tologin")
    public String toLogin() {
        return "customerLogin";
    }

    @RequestMapping("/login")
    public String login(ModelMap modelMap, String name, String password) {
        String msg = null;
        Customer customer = customerService.findCustomerByName(name);
        if (customer == null) {
            msg = "User name doesn't exist";
            modelMap.put("msg", msg);
            return "customerLogin";
        } else {
            String pwd = customer.getPassword();
            if (pwd != null && pwd.equals(password)) {
                msg = "Login success";
                modelMap.put("msg", msg);
                return "customerHome";
            } else {
                msg = "Password error";
                modelMap.put("msg", msg);
                return "customerLogin";
            }
        }
    }

    @RequestMapping("/cart")
    public String toCart() {
        return "customerCart";
    }

    @RequestMapping("/toregister")
    public String register(){
        return "customerRegister";
    }

    @RequestMapping("/register")
    public String register(ModelMap modelMap, Customer customer){
        String msg = null;
        //if the user haven't registered
        String name = customer.getName();
        String tel = customer.getTel();
        String email = customer.getEmail();
        Customer customer_db = customerService.findCustomerByName(name);
        if (customer_db != null){
            if (email.equals(customer_db.getEmail())){
                msg = "You have been registered, please do not register again.";
                modelMap.put("msg",msg);
                return "customerRegister";
            }
            msg = "This name has been used, please choose another.";
            modelMap.put("msg",msg);
            return "customerRegister";
        }
        //register
        customer.setId(IDGenerator.getId());
        customerService.insertCustomer(customer);
        msg = "Register successfully.";
        modelMap.put("msg",msg);
        return "customerLogin";
    }
}
