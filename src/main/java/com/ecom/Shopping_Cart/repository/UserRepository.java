package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.userDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository  extends JpaRepository<userDetails,Integer> {
    public userDetails findByEmail(String email);
    public List<userDetails> findByRole(String role);
    public userDetails findByResetToken(String token);
    public Boolean existsByEmail(String email);
}
