package com.example.SWP.Repository;


import com.example.SWP.entity.KoiType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiTypeRepository extends JpaRepository<KoiType, Long> {
    KoiType findKoiById(long id);

    List<KoiType> findKoiTypesByIsDeletedFalse();
}
