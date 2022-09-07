package com.example.productservice.Controller;


import com.example.productservice.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    Environment env;

    List<Product> productList = new ArrayList<>();

    @PostMapping("/add")
    @Secured("ROLE_producer")
    public void addProduct(@ModelAttribute Product product){
        this.productList.add(product);
    }

    @GetMapping("/deleteProduct")
    @PreAuthorize("hasRole('producer')")
    public void deleteProduct(@RequestParam(name = "product_id") int productId){
        this.productList.removeIf(item->item.getProductId()==productId);
    }

    @GetMapping("/all")
    public List<Product> getProducts(){
        this.productList.add(new Product(1,"pro_1"));
        this.productList.add(new Product(2,"pro_2"));
        return this.productList;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") int id){
        return this.productList.stream().filter(pro->pro.getProductId()==id).findAny().orElse(null);
    }

    @GetMapping("/port")
    public String getPort(){
        return env.getProperty("local.server.port");
    }


}
