package com.demo.mms.controller;

import com.demo.mms.common.domain.Cart;
import com.demo.mms.common.domain.Customer;
import com.demo.mms.common.utils.IDGenerator;
import com.demo.mms.common.domain.Goods;
import com.demo.mms.service.CartService;
import com.demo.mms.service.CustomerService;
import com.demo.mms.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CartService cartService;

    @RequestMapping("/tologin")
    public String toLogin() {
        return "customerLogin";
    }

    @RequestMapping("/login")
    public String login(ModelMap modelMap, String name, String password, HttpSession session) {
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
                session.setAttribute("curr_customer", customer);
                return "customerHome";
            } else {
                msg = "Password error";
                modelMap.put("msg", msg);
                return "customerLogin";
            }
        }
    }

    @RequestMapping("/cart")
    public String toCart(ModelMap modelMap, HttpSession session) {
        Customer curr_customer = (Customer) session.getAttribute("curr_customer");
        String curr_customer_id = curr_customer.getId();
        List<Cart> cart_list = cartService.findGoodsIdByCustomerId(curr_customer_id);
        int size = cart_list.size();
        List<Goods> all_goods_in_cart = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String good_id = cart_list.get(i).getGoodsId();
            all_goods_in_cart.add(goodsService.findGoodsById(good_id));
        }
        modelMap.put("goods_in_cart", all_goods_in_cart);
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

    @RequestMapping("/info")
    public String info(){
        return "customerInfo";
    }

    @RequestMapping("/home")
    public String home(){
        return "customerHome";
    }
}