package com.example.SWP.Repository;

import com.example.SWP.entity.Account;
import com.example.SWP.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiRepository extends JpaRepository<Koi, Long> {
    Koi findKoiById(long id);

    List<Koi> findKoisByIsDeletedFalse();
}
