package com.ecom.Shopping_Cart.config;

import com.ecom.Shopping_Cart.model.userDetails;
import com.ecom.Shopping_Cart.repository.UserRepository;
import com.ecom.Shopping_Cart.service.UserService;
import com.ecom.Shopping_Cart.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException
    {
        String email = request.getParameter("username");

        userDetails userDetails = userRepository.findByEmail(email);

        if(userDetails!=null)
        {
            if (userDetails.getIsEnable())
            {
                if (userDetails.getAccountNonLocked())
                {
                    if (userDetails.getFailedAttempt() < AppConstant.ATTEMPT_TIME)
                    {
                        userService.increaseFailedAttempt(userDetails);
                    }
                    else
                    {
                        userService.userAccountLock(userDetails);
                        exception= new LockedException("Your Account is locked !! failed attempt 3");
                    }
                }
                else
                {
                    if(userService.unlockAccountTimeExpired(userDetails))
                    {
                        exception = new LockedException("Your Account is unlocked !! please try to login");
                    }
                    else
                    {
                        exception = new LockedException("Your Account is locked !! please try after sometime");
                    }
                }
            }
            else
            {
                exception = new LockedException("Your Account is Inactive");
            }
        }
        else
        {
            exception = new LockedException("Email & password are invalid");
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request,response,exception);
    }
}
