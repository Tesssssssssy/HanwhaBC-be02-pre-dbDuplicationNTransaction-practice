package com.example.demo.product.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.member.model.Member;
import com.example.demo.product.model.Product;
import com.example.demo.product.model.ProductImage;
import com.example.demo.product.model.dto.*;
import com.example.demo.product.repository.ProductImageRepository;
import com.example.demo.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private AmazonS3 s3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Transactional(readOnly = false)
    public ProductCreateRes create(Member member, ProductCreateReq productCreateReq, MultipartFile[] uploadFiles) {
        Product product = productRepository.save(Product.builder()
                .name(productCreateReq.getName())
                .brandIdx(member)
                .price(productCreateReq.getPrice())
                .categoryIdx(productCreateReq.getCategoryIdx())
                .salePrice(productCreateReq.getSalePrice())
                .contents(productCreateReq.getContents())
                .deliveryType(productCreateReq.getDeliveryType())
                .build());

        for (MultipartFile uploadFile:uploadFiles) {
//            String uploadPath = uplopadFile(uploadFile);
            productImageRepository.save(ProductImage.builder()
                    .product(product)
                    .filename(uploadFile.getOriginalFilename())
                    .build());
        }
        ProductCreateRes response = ProductCreateRes.builder()
                .code(1000)
                .message("요청 성공.")
                .success(true)
                .isSuccess(true)
                .result(ProductCreateResult.builder().idx(product.getId()).build())
                .build();

        return response;
    }

    @Transactional(readOnly = true)
    public ProductListRes list(Integer page, Integer size) {

        // n+1 문제, 수정 점
//        List<Product> result = productRepository.findAll();
        // Page 기능으로 수정 후
//        Pageable pageable = PageRequest.of(page-1,size);
//        Page<Product> result = productRepository.findAll(pageable);

        // JPQL로 N+1 문제는 해결
//        List<Product> result = productRepository.findAllQuery();
        // JPQL은 페이징 처리가 복잡
                Pageable pageable = PageRequest.of(page-1,size);
        Page<Product> result = productRepository.findList(pageable);

        List<ProductReadRes> productReadResList = new ArrayList<>();

        //          수정 전 or JPQL
//                for (Product product : result) {
        
        // 수정 후
        for (Product product : result.getContent()) {
            

            List<ProductImage> productImages = product.getProductImages();

            String filenames = "";
            for (ProductImage productImage : productImages) {
                String filename = productImage.getFilename();
                filenames += filename + ",";
            }
            filenames = filenames.substring(0, filenames.length() - 1);


            ProductReadRes productReadRes = ProductReadRes.builder()
                    .idx(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .brandIdx(product.getBrandIdx().getId())
                    .categoryIdx(product.getCategoryIdx())
                    .deliveryType(product.getDeliveryType())
                    .salePrice(product.getSalePrice())
                    .isTodayDeal(product.getIsTodayDeal())
                    .like_check(false)
                    .filename(filenames)
                    .build();
            productReadResList.add(productReadRes);
        }


        return ProductListRes.builder()
                .code(1000)
                .message("요청 성공.")
                .success(true)
                .isSuccess(true)
                .result(productReadResList)
                .build();
    }


    public ProductReadRes2 read(Long id) {
        Optional<Product> result = productRepository.findById(id);

        if (result.isPresent()) {
            Product product = result.get();

            List<ProductImage> productImages = product.getProductImages();

            String filenames = "";
            for (ProductImage productImage : productImages) {
                String filename = productImage.getFilename();
                filenames += filename + ",";
            }
            filenames = filenames.substring(0, filenames.length() - 1);


            ProductReadRes productReadRes = ProductReadRes.builder()
                    .idx(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .brandIdx(product.getBrandIdx().getId())
                    .categoryIdx(product.getCategoryIdx())
                    .deliveryType(product.getDeliveryType())
                    .salePrice(product.getSalePrice())
                    .isTodayDeal(product.getIsTodayDeal())
                    .like_check(false)
                    .filename(filenames)
                    .build();
            return ProductReadRes2.builder()
                    .code(1000)
                    .message("요청 성공.")
                    .success(true)
                    .isSuccess(true)
                    .result(productReadRes)
                    .build();
        }

        return null;


    }

    public void update(ProductUpdateReq productUpdateReq) {
        Optional<Product> result = productRepository.findById(productUpdateReq.getId());
        if (result.isPresent()) {
            Product product = result.get();
            product.setName(productUpdateReq.getName());
            product.setPrice(productUpdateReq.getPrice());

            productRepository.save(product);
        }
    }

    public void delete(Long id) {
        productRepository.delete(Product.builder().id(id).build());
    }


    public String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);


        return folderPath;
    }


    public String uplopadFile(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String folderPath = makeFolder();
        String uuid = UUID.randomUUID().toString();
        String saveFileName = folderPath + File.separator + uuid + "_" + originalName;
//        File saveFile = new File(uploadPath, saveFileName);
        InputStream input = null;
        try {
//            file.transferTo(saveFile);
            input = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());


            s3.putObject(bucket, saveFileName.replace(File.separator, "/"), input, metadata);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return s3.getUrl(bucket, saveFileName.replace(File.separator, "/")).toString();
    }

}
