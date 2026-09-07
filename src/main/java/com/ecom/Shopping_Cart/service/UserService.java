package com.ecom.Shopping_Cart.service;


import com.ecom.Shopping_Cart.model.userDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {
    public userDetails saveUser(userDetails user);
    public userDetails getUserByEmail(String email);
    public List<userDetails> getUsers(String role);
    public Boolean updateAccountStatus(Integer id,Boolean status);
    public void increaseFailedAttempt(userDetails user);
    public void userAccountLock(userDetails user);
    public boolean unlockAccountTimeExpired(userDetails user);
    public void resetAttempt(int userId);
    public void updateUserResetToken(String email,String resetToken);
    public userDetails getUserByToken(String token);
    public userDetails updateUser(userDetails user);
    public userDetails updateUserProfile(userDetails user, MultipartFile img);
    public userDetails saveAdmin(userDetails user);
    public boolean existsEmail(String email);

}
