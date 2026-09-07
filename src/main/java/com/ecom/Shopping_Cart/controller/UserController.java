package com.ecom.Shopping_Cart.controller;

import com.ecom.Shopping_Cart.model.*;
import com.ecom.Shopping_Cart.service.CartService;
import com.ecom.Shopping_Cart.service.CategoryService;
import com.ecom.Shopping_Cart.service.OrderService;
import com.ecom.Shopping_Cart.service.UserService;
import com.ecom.Shopping_Cart.util.CommonUtil;
import com.ecom.Shopping_Cart.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //REMOVE @GetMapping("/")
    // REMOVE home()
    @ModelAttribute
    public void getUserDetails(Principal p, Model m)
    {
        if(p!=null)
        {
            String email = p.getName();
            userDetails userDetails = userService.getUserByEmail(email);
            m.addAttribute("user",userDetails);
            Integer countCart = cartService.getCountCart(userDetails.getId());
            m.addAttribute("CountCart",countCart);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCatgeory();
        m.addAttribute("Categories",allActiveCategory);
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session)
    {
        Cart saveCart =  cartService.saveCart(pid,uid);

        if(ObjectUtils.isEmpty(saveCart))
        {
            session.setAttribute("errorMsg","Product add to cart failed");
        }
        else
        {
            session.setAttribute("succMsg","Product added to cart");
        }
        return "redirect:/product/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p,Model m)
    {
        userDetails user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(user.getId());
        m.addAttribute("carts",carts);
        if(carts.size() > 0)
        {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }
        return "/user/cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy,@RequestParam Integer cid)
    {
        cartService.updateQuantity(sy,cid);
        return "redirect:/user/cart";
    }

    private userDetails getLoggedInUserDetails(Principal p)
    {
        String email = p.getName();
        userDetails userDetails = userService.getUserByEmail(email);
        return userDetails;
    }

    @GetMapping("/orders")
    public String orderPage(Principal p,Model m)
    {
        userDetails user  = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(user.getId());
        m.addAttribute("carts",carts);
        if(carts.size() > 0)
        {
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;
            m.addAttribute("orderPrice",orderPrice);
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p) throws Exception
    {
        userDetails user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(),request);
        return "redirect:/user/success";
    }

    @GetMapping("/success")
    public String success() {
        return "/user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m,Principal p)
    {
        userDetails loginUser = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());
        m.addAttribute("orders",orders);
        return  "/user/my_orders";
    }

    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer id,@RequestParam Integer  st,HttpSession session)
    {

        String status = null;

        for(OrderStatus orderSt : OrderStatus.values())
        {
            if(orderSt.getId().equals(st))
            {
                status = orderSt.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id,status);
        try
        {
            commonUtil.sendMailForProductOrder(updateOrder,status);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!ObjectUtils.isEmpty(updateOrder))
        {
            session.setAttribute("succMsg","Status Updated");
        }
        else
        {
            session.setAttribute("errorMsg","status not updated");
        }
        return "redirect:/user/user-orders";
    }

    @GetMapping("/profile")
    public String profile()
    {
        return "/user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute userDetails user, @RequestParam MultipartFile img,HttpSession session)
    {
        userDetails updateUserProfile =  userService.updateUserProfile(user,img);
        if(ObjectUtils.isEmpty(updateUserProfile))
        {
            session.setAttribute("errorMsg","Profile not updated");
        }
        else
        {
            session.setAttribute("succMsg","Profile Updated");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("change-password")
    public String changePassword(@RequestParam String newPassword,@RequestParam String currentPassword,Principal p,HttpSession session)
    {
        userDetails loggedInuserDetails = getLoggedInUserDetails(p);
        boolean matches = passwordEncoder.matches(currentPassword,loggedInuserDetails.getPassword());

        if(matches)
        {
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInuserDetails.setPassword(encodePassword);
            userDetails updateUser = userService.updateUser(loggedInuserDetails);
            if(ObjectUtils.isEmpty(updateUser))
            {
                session.setAttribute("errorMsg","Password not updated !! Error in server");
            }
            else
            {
                session.setAttribute("succMsg","Password updated successfully");
            }
        }
        else
        {
            session.setAttribute("errorMsg","current password incorrect");
        }
        return "redirect:/user/profile";
    }
}
