package com.example.SWP.Repository;

import com.example.SWP.entity.CartDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CartDetails c WHERE c.id = :id")
    void deleteCartItem(@Param("id") long id);


}
