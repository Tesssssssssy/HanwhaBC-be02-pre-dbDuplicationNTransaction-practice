package com.example.demo.product.model;

import com.example.demo.member.model.Member;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private Integer salePrice;
    private Integer categoryIdx;
    private String deliveryType;
    private String isTodayDeal;
    private String contents;

    // 낙관적 락
    @Version
    private Integer likeCount;

    @ManyToOne
    @JoinColumn(name = "Member_id")
    private Member brandIdx;


    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductImage> productImages = new ArrayList<>();


    public void increaseLikeCount() {
        this.likeCount = this.likeCount + 1;
    }
}
