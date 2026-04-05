package com.shop.shop.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String categoryName; // 카테고리 이름

    // 해당 카테고리에 소속될 상품들
    @OneToMany(mappedBy = "category")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    // 상위 카테고리 + 하위카테고리 : 양방향 관계 설정
    @JsonIgnore // 무한루프 방지 어노테이션
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 하위 카테고리
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public void addChild(Category newChild) {
        this.child.add(newChild); // 하위 카테고리 리스트에 새로운 자식 카테고리 추가
        newChild.changeParent(this); // 변경된 this(=category) 로 다시 설정
    }

    // parent 값 변경
    public void changeParent(Category parent) {
        this.parent = parent;
    }

    public void changeCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
