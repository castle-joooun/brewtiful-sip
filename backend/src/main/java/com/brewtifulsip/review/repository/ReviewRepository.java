package com.brewtifulsip.review.repository;

import com.brewtifulsip.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByOrderItem_Id(Long orderItemId);

    List<Review> findByOrderItem_IdIn(Collection<Long> orderItemIds);

    List<Review> findAllByOrderByIdDesc();

    List<Review> findByOrderItem_Menu_IdOrderByIdDesc(Long menuId);
}
