package com.shop.shop.repository;

import com.shop.shop.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 모든 부모 카테고리 조회
    @EntityGraph(attributePaths = { "child" })
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.viewStatus = false ORDER BY c.id ASC")
    List<Category> findAllParentCategory();

    // 모든 부모 카테고리 조회 (페이징)
    @EntityGraph(attributePaths = { "child" })
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.viewStatus = false ORDER BY c.id DESC")
    Page<Category> findAllParentCategoryWithPage(Pageable pageable);

    // 특정 부모 카테고리 조회
    @EntityGraph(attributePaths = { "child" })
    @Query("SELECT c FROM Category c WHERE c.id = :categoryId AND c.parent IS NULL AND c.viewStatus = false ORDER BY c.id DESC")
    Category findOneParentCategory(@Param("categoryId") Long categoryId);

    // 특정 부모 카테고리 조회 (페이징)
    @EntityGraph(attributePaths = { "child" })
    @Query("SELECT c FROM Category c WHERE c.id = :categoryId AND c.parent IS NULL AND c.viewStatus = false ORDER BY c.id DESC")
    Page<Category> findOneParentCategory(Pageable pageable, @Param("categoryId") Long categoryId);

    // // 특정 부모 카테고리의 모든 자식 카테고리 조회
    // @Query("SELECT DISTINCT c FROM Category c WHERE c.parent.id = :parentId AND
    // c.viewStatus = false")
    // List<Category> findAllChildCategories(@Param("parentId") Long parentId);

    // 특정 부모 카테고리의 모든 자식 카테고리 조회
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.viewStatus = false ORDER BY c.id DESC")
    List<Category> findAllChildCategories(@Param("parentId") Long parentId);

    // categoryName 의 단어가 포함된 모든 카테고리 조회
    @Query("SELECT c FROM Category c WHERE c.categoryName LIKE CONCAT('%', :categoryName, '%') AND c.viewStatus = false ORDER BY c.id DESC")
    List<Category> findAllByCategoryName(@Param("categoryName") String categoryName);

}
