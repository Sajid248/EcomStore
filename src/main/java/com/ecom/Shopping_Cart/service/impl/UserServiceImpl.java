package com.ecom.Shopping_Cart.service.impl;

import com.ecom.Shopping_Cart.model.userDetails;
import com.ecom.Shopping_Cart.repository.UserRepository;
import com.ecom.Shopping_Cart.service.UserService;
import com.ecom.Shopping_Cart.util.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public userDetails saveUser(userDetails user) {
        user.setRole("ROLE_USER");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userDetails saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public userDetails getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<userDetails> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {
        Optional<userDetails> findByUser = userRepository.findById(id);

        if(findByUser.isPresent())
        {
            userDetails userDetails = findByUser.get();
            userDetails.setIsEnable(status);
            userRepository.save(userDetails);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempt(userDetails user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(userDetails user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);

    }

    @Override
    public boolean unlockAccountTimeExpired(userDetails user) {
        long lockTime = user.getLockTime().getTime();
        long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if(unlockTime < currentTime)
        {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resetAttempt(int userId) {

    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {

        userDetails findByEmail = userRepository.findByEmail(email);
        findByEmail.setResetToken(resetToken);
        userRepository.save(findByEmail);
    }

    @Override
    public userDetails getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public userDetails updateUser(userDetails user) {
        return userRepository.save(user);
    }

//    public userDetails updateUserProfile(userDetails user, MultipartFile img) {
//        userDetails dbUser = userRepository.findById(user.getId()).get();
//
//        if(!img.isEmpty())
//        {
//            dbUser.setProfileImage(img.getOriginalFilename());
//        }
//        if(!ObjectUtils.isEmpty(dbUser))
//        {
//            dbUser.setName(user.getName());
//            dbUser.setMobNo(user.getMobNo());
//            dbUser.setAddress(user.getAddress());
//            dbUser.setCity(user.getCity());
//            dbUser.setPincode(user.getPincode());
//            dbUser = userRepository.save(dbUser);
//        }
//
//        try{
//            if(!img.isEmpty())
//            {
//                File saveFile = new ClassPathResource("static/img").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + img.getOriginalFilename());
//                Files.copy(img.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dbUser;
//    }
    @Override
    public userDetails updateUserProfile(userDetails user, MultipartFile img) {

        userDetails dbUser = userRepository.findById(user.getId()).get();

        dbUser.setName(user.getName());
        dbUser.setMobNo(user.getMobNo());
        dbUser.setAddress(user.getAddress());
        dbUser.setCity(user.getCity());
        dbUser.setPincode(user.getPincode());

        try {

            if (!img.isEmpty()) {

                String fileName = img.getOriginalFilename();

                File saveFile = new ClassPathResource("static/img").getFile();

                File profileDir = new File(
                        saveFile,
                        "profile_img"
                );

                if (!profileDir.exists()) {
                    profileDir.mkdirs();
                }

                Path path = Paths.get(
                        profileDir.getAbsolutePath(),
                        fileName
                );

                Files.copy(
                        img.getInputStream(),
                        path,
                        StandardCopyOption.REPLACE_EXISTING
                );

                dbUser.setProfileImage(fileName);
            }

            dbUser = userRepository.save(dbUser);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbUser;
    }

    @Override
    public userDetails saveAdmin(userDetails user) {
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userDetails saveUser = userRepository.save(user);
        return saveUser;
    }

    @Override
    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
