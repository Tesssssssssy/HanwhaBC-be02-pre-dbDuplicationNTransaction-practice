package com.example.demo.product.controller;

import com.example.demo.member.model.Member;
import com.example.demo.product.model.Product;
import com.example.demo.product.model.dto.ProductCreateReq;
import com.example.demo.product.model.dto.ProductCreateRes;
import com.example.demo.product.model.dto.ProductCreateResult;
import com.example.demo.product.model.dto.ProductUpdateReq;
import com.example.demo.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity create(@AuthenticationPrincipal Member member,
                                 @RequestPart ProductCreateReq postProductReq, @RequestPart MultipartFile[] uploadFiles) {
        ProductCreateRes response = productService.create(member, postProductReq, uploadFiles);

        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity list(Integer page, Integer size) {
        return ResponseEntity.ok().body(productService.list(page, size));
    }

    @GetMapping("/{idx}")
    public ResponseEntity getProducts(@PathVariable Long idx) {
        return ResponseEntity.ok().body(productService.read(idx));

    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/update")
    public ResponseEntity update(ProductUpdateReq productUpdateReq) {
        productService.update(productUpdateReq);

        return ResponseEntity.ok().body("수정");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity delete(Long id) {
        productService.delete(id);
        return ResponseEntity.ok().body("삭제");

    }
}
