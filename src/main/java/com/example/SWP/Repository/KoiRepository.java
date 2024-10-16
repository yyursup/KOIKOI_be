package com.example.SWP.Repository;

import com.example.SWP.entity.Account;
import com.example.SWP.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KoiRepository extends JpaRepository<Koi, Long> {
    Koi findKoiById(long id);

    List<Koi> findKoisByIsDeletedFalse();



}
