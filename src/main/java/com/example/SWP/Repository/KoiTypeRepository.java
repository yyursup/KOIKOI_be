package com.example.SWP.Repository;


import com.example.SWP.entity.KoiType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KoiTypeRepository extends JpaRepository<KoiType, Long> {
    KoiType findKoiById(long id);

    List<KoiType> findKoiTypesByIsDeletedFalse();

    KoiType findCategoryById(long id);

}
