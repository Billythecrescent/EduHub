package com.demo.mms.service;

import com.demo.mms.common.domain.Cart;
import com.demo.mms.dao.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Override
    public List<Cart> findGoodsIdByCustomerId(String customer_id) {
        return cartMapper.selectByCustomerId(customer_id);
    }

    @Override
    @Transactional
    public int addItem(Cart cart) {
        return cartMapper.insert(cart);
    }
}
