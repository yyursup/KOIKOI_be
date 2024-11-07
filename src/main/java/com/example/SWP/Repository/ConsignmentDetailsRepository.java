package com.example.SWP.Repository;

import com.example.SWP.entity.ConsignmentDetails;
import com.example.SWP.entity.Koi;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsignmentDetailsRepository extends JpaRepository<ConsignmentDetails, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ConsignmentDetails c where c.id = :id")
    void deleteConsignmentDetails(@Param("id") long id);

    ConsignmentDetails findByKoi(Koi koi);
}
