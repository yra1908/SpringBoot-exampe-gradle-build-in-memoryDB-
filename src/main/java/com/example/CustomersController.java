package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by hanz on 04.03.2016.
 */
@RestController //головна анатоція яка робить з класса рест класс
public class CustomersController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/customers") //урл щоб http протокол знал на який метод звертатися
    public List<Customer> getCustomers() {
        String sql = "SELECT * FROM customers";
        List customer = jdbcTemplate.query(
                sql, new BeanPropertyRowMapper(Customer.class));
        return customer;
    }

    //http://localhost:8080/add?name=Yura&lastName=Yan - приклад лінка передпча даних
    @RequestMapping("/add")
    public List<Customer> setCustomers(@RequestParam(value = "name", defaultValue = "Ivan") String name,
                                       @RequestParam(value = "lastName", defaultValue = "Trut") String lastName) {

        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES ( '" + name + "','" + lastName + "')");
        return getCustomers();
    }

    @RequestMapping("/customer")
    public Customer getCustomersId(@RequestParam(value = "id", defaultValue = "1") int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        return(Customer) jdbcTemplate.queryForObject(
                sql, new Object[] { id }, new Customer());
    }

}
