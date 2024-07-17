package com.example.BookStore.controller;

import com.example.BookStore.providers.Book;
import com.example.BookStore.providers.Cart;
import com.example.BookStore.providers.Order;
import com.example.BookStore.providers.Person;
import com.example.BookStore.services.*;
import com.example.BookStore.util.BookValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BookValidator bookValidator;
    private final BookService bookService;
    private final OrderService orderService;
    private final PersonService personService;
    private final CartService cartService;
    private final OrderItemService orderItemService;


    public AdminController(BookValidator bookValidator, BookService bookService, OrderService orderService, PersonService personService, CartService cartService, OrderItemService orderItemService) {
        this.bookValidator = bookValidator;
        this.bookService = bookService;
        this.orderService = orderService;
        this.personService = personService;
        this.cartService = cartService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

    @GetMapping("/add-book")
    public String addBook(@ModelAttribute("book") Book book){
        return "admin/addBook";
    }

    @PostMapping("/add-book")
    public String createBook(Model model, @Valid @ModelAttribute("book") Book book, BindingResult bindingResult){
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "admin/addBook";
        model.addAttribute("postCorrect", true);
        model.addAttribute("book", new Book());

        bookService.save(book);
        return "admin/addBook";
    }

    @Transactional
    @PostMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        cartService.deleteAll(id);
        orderItemService.deleteAll(id);
        bookService.delete(id);
        return "redirect:/";
    }

    @GetMapping("{id}/edit")
    public String editBook(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "admin/edit";
    }
    @PostMapping("/{id}/edit")
    public String updateBook(Model model, @PathVariable("id") int id, @Valid Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "admin/edit";
        bookService.update(id,book);
        return "redirect:/admin/bookDetails";
    }

    @GetMapping("/bookDetails")
    public String bookDetails(Model model) {
        model.addAttribute("bookList", bookService.findAll());
        return "admin/bookDetails";
    }

    @GetMapping("/orders")
    public String getAllOrders(Authentication authentication, Model model) {
        List<Order> orders = orderService.showAllOrders();
        Map<Person, List<Order>> personOrdersMap = orders.stream().collect(Collectors.groupingBy(Order::getPerson));
        model.addAttribute("personOrdersMap", personOrdersMap);
        return "admin/orders";
    }
    ////////////////////////////////////////////////////////////////////
    @GetMapping("/addAdmin")
    public String showAllUser(Model model) {
        List<Person> users = personService.getUsers("ROLE_USER");
        model.addAttribute("users", users);
        return "admin/allUsers";
    }

    @PostMapping("/addAdmin")
    public String addAdmin(@RequestParam("username") String username) {
        Person user = personService.getPerson(username).get();
        user.setRole("ROLE_ADMIN");
        personService.savePerson(user);
        return "redirect:/admin/addAdmin";
    }

    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") int orderId, @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders";
    }
}
