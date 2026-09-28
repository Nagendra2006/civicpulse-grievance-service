package com.example.GrievanceService.repository;



import com.example.GrievanceService.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNull(); // major categories

    List<Category> findByParentId(Long parentId);
}
