package academy.digitallab.store.product.controller;

import academy.digitallab.store.product.Service.ProductService;
import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value ="/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public ResponseEntity<List<Product>> listProduct(@RequestParam(name ="categoryId", required = false)Long categoryId){
      List<Product> products = new ArrayList<>();

        log.info("ESTE ES category id:" + categoryId);
        if (null == categoryId) {
            products = productService.listAllproduct();
            if (products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }
        else{
            products = productService.findByCategory(Category.builder().id(categoryId).build());
        }
        if (products.isEmpty()){
            return ResponseEntity.notFound().build();
        }
     return ResponseEntity.ok(products);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product product = productService.getProduct(id);
        if (null==product){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result){
        log.info("este es Name :" + product.getName());
        log.info("este es Stock :" + product.getStock());
        log.info("este es price :" + product.getPrice());
        log.info("este es Name :" + product.getCategory());
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }

        Product productCreate = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        Product productCreate = productService.createProduct(product);
        product.setId(id);
        Product productDB = productService.updateProduct(product);
        if (productDB == null) {
         return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productDB);
    }

    @DeleteMapping(value ="/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id) {
        Product productDelete = productService.deleteProduct(id);
        if (productDelete == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productDelete);
    }
    @GetMapping(value = "/{id}/stock")
       public ResponseEntity<Product> updateStockProduct(@PathVariable Long id,@RequestParam(name = "quantity",required = true) Double quantity){
        Product product = productService.updateStock(id, quantity);
        if (null == product)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
       }
       private String formatMessage(BindingResult result){
        List <Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String, String > error = new HashMap<>();
                    error.put(err.getField(),err.getDefaultMessage());
                    return error;
                        }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
           ObjectMapper mapper = new ObjectMapper();
           String jsonString="";
           try {
               jsonString = mapper.writeValueAsString(errorMessage);
           } catch(JsonProcessingException e){
               e.printStackTrace();
           }
           return jsonString;
       }
    }
