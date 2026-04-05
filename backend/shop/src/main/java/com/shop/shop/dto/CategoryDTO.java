package com.shop.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shop.shop.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long categoryId;
    private String categoryName;
    private Long parentId;
    private boolean viewStatus;

    // 자식 카테고리를 DTO 리스트로 포함
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryDTO> child;

    public CategoryDTO(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getCategoryName();
        this.parentId = category.getParent() != null ? category.getParent().getId() : null;
        this.viewStatus = category.isViewStatus();

        // 자식 카테고리 DTO 변환 (카테고리 아이템은 포함하지 않음)
        if (category.getChild() != null && !category.getChild().isEmpty()) {
            this.child = category.getChild().stream()
                    .map(childCategory -> {
                        CategoryDTO dto = new CategoryDTO();
                        dto.setCategoryId(childCategory.getId());
                        dto.setCategoryName(childCategory.getCategoryName());
                        dto.setViewStatus(childCategory.isViewStatus());
                        dto.setParentId(childCategory.getParent() != null ? childCategory.getParent().getId() : null);
                        dto.setChild(null); // 자식의 자식까지는 포함하지 않도록 설정
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }
}
