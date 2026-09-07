//package com.ecom.Shopping_Cart.repository;
//
//import com.ecom.Shopping_Cart.model.Cart;
//import com.ecom.Shopping_Cart.model.userDetails;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//public interface CartRepository extends JpaRepository<Cart,Integer> {
//    public Cart findByProductAndUserId(Integer productId,Integer userId);
//
//    public Integer countByUserId(Integer userId);
//
//    public List<Cart> findByUserId(Integer userId);
//
//    @Transactional
//    @Modifying
//    public void deleteByUser(userDetails user);
//}
package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.Cart;
import com.ecom.Shopping_Cart.model.userDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByProduct_IdAndUserId(Integer productId, Integer userId);

    Integer countByUserId(Integer userId);

    List<Cart> findByUserId(Integer userId);

    @Transactional
    @Modifying
    void deleteByUser(userDetails user);
}