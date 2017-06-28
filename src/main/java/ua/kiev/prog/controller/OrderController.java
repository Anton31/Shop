package ua.kiev.prog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kiev.prog.model.Cart;
import ua.kiev.prog.model.Device;
import ua.kiev.prog.model.Order;
import ua.kiev.prog.model.User;
import ua.kiev.prog.security.UserService;
import ua.kiev.prog.service.DeviceService;
import ua.kiev.prog.service.OrderService;

import java.util.List;

/**
 * Controller for order management
 */

@Controller
@RequestMapping("/")
public class OrderController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/cart_page", method = RequestMethod.GET)
    public String cart(
            Model model) {
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("carts", orderService.listCarts(findUser()));
        model.addAttribute("total", orderService.totalPrice(findUser()));
        model.addAttribute("items", orderService.totalItems(findUser()));
        return "cart_page";
    }

    @RequestMapping(value = "/to-cart/{deviceId}/{n}", method = RequestMethod.GET)
    public String toCart(@PathVariable int deviceId, @PathVariable int n, Model model) {
        Device device = deviceService.findDeviceById(deviceId);
        Cart cart = new Cart(findUser(), device, 1);
        List<Cart> l = orderService.listCarts(findUser());
        int count = 0;
        for (Cart c : l) {
            if (c.getDevice().equals(device)) {
                c.setItems(c.getItems() + n);
                if (c.getItems() > 0)
                    orderService.addCart(c);
                count++;
            }
        }
        if (count == 0 && n == 1) {
            orderService.addCart(cart);
        }
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("items", orderService.totalItems(findUser()));
        model.addAttribute("carts", orderService.listCarts(findUser()));
        model.addAttribute("total", orderService.totalPrice(findUser()));
        return "cart_page";
    }

    @RequestMapping(value = "/add-order", method = RequestMethod.POST)
    public String orderAdd(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String phone,
            Model model) {
        List<Cart> carts = orderService.listCarts(findUser());
        for (Cart c : carts) {
            Order order = new Order(findUser(), name, address, phone, c);
            orderService.addOrder(order);
        }
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("items", orderService.totalItems(findUser()));
        model.addAttribute("orders", orderService.listOrders(findUser()));
        model.addAttribute("total", orderService.totalPrice(findUser()));
        return "result_page";
    }

    @RequestMapping("/result_page")
    public String result(Model model) {
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("items", orderService.totalItems(findUser()));
        model.addAttribute("orders", orderService.listOrders(findUser()));
        model.addAttribute("total", orderService.totalPrice(findUser()));
        return "result_page";
    }

    @RequestMapping(value = "/order_page", method = RequestMethod.GET)
    public String order(Model model) {
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("items", orderService.totalItems(findUser()));
        model.addAttribute("carts", orderService.listCarts(findUser()));

        return "order_page";
    }

    @RequestMapping(value = "/cart/delete/{cartId}")
    public String deleteCart(@PathVariable int cartId, Model model) {
        deviceService.deleteCart(cartId);
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("items", orderService.totalItems(findUser()));
        model.addAttribute("carts", orderService.listCarts(findUser()));
        model.addAttribute("total", orderService.totalPrice(findUser()));
        return "cart_page";
    }

    public User findUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
        return user;
    }
}
