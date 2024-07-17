package com.example.BookStore.controller;
import com.example.BookStore.providers.Cart;
import com.example.BookStore.providers.Order;
import com.example.BookStore.providers.Person;
import com.example.BookStore.services.BookService;
import com.example.BookStore.services.CartService;
import com.example.BookStore.services.OrderService;
import com.example.BookStore.services.PersonService;
import com.example.BookStore.util.PersonValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class MainController {
    private final BookService bookService;
    private final OrderService orderService;
    private final PersonService personService;
    private final CartService cartService;
    private final PersonValidator personValidator;

    @Autowired
    public MainController(BookService bookService, OrderService orderService, PersonService personService, CartService cartService, PersonValidator personValidator) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.personService = personService;
        this.cartService = cartService;
        this.personValidator = personValidator;
    }

    @GetMapping("/")
    public String listOfBooks(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("person_id", personService.getPerson(authentication.getName()).get().getId());
        }
        model.addAttribute("bookList", bookService.findAll());
        return "listOfBooks";
    }

    @GetMapping("/{id}")
    public String showBook(Authentication authentication, HttpServletRequest request, @PathVariable int id, Model model) {
        if (authentication != null) {
            String name = authentication.getName();
            Person person = personService.getPerson(name).get();
            model.addAttribute("person_id", person.getId());
        }
        model.addAttribute("book", bookService.findOne(id));
        model.addAttribute("referer", request.getRequestURI());
        return "bookInfo";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute Person person){
        return "registrationPage";
    }

    @PostMapping("/process_registration")
    public String registrationPerson(@Valid @ModelAttribute Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "registrationPage";

        personService.save(person);
        return "redirect:/login?registration";
    }

    //    @GetMapping("/cart")
//    public String listOfOrders(Model model) {
//        model.addAttribute("ordersList", cartService.findAll());
//        return "orders";
//    }
    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer oId) {
        cartService.updateQuantity(sy, oId);
        return "redirect:/cart";
    }

    private Person getLoggedInPersonDetails(Authentication p) {
        String name = p.getName();
        return personService.getPerson(name).get();
    }
    @GetMapping("/cart")
    public String loadCartPage(Authentication p, Model model){
        Person person = getLoggedInPersonDetails(p);
        List<Cart> orders = cartService.getListCarts(person);
        model.addAttribute("orders", orders);
        if (!orders.isEmpty()) {
            Double totalOrderPrice = orders.getLast().getTotalOrderPrice();
            model.addAttribute("totalOrderPrice" ,totalOrderPrice);
        }
        return "cart";
    }

    @PostMapping("/cart")
    public String confirmOrder(Authentication authentication) {
        orderService.placeOrder(getLoggedInPersonDetails(authentication));
        return "redirect:/cart";
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model, Authentication authentication) {
        Person person = personService.getPerson(authentication.getName()).get();
        List<Order> orders = orderService.showOrders(person);
        model.addAttribute("orders", orders);
        return "my-orders";
    }

}