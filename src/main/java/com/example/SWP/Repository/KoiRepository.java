package com.example.SWP.Repository;

import com.example.SWP.Enums.Author;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.IdentificationCertificate;
import com.example.SWP.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KoiRepository extends JpaRepository<Koi, Long> {
    Koi findKoiById(long id);

    List<Koi> findKoiByAuthor(Author author);
    List<Koi> findKoisByIsDeletedFalse();

    @Query("select sum(k.quantity) from Koi k")
    long getTotalFishCount();

    @Query("SELECT k.identificationCertificate FROM Koi k WHERE k.id = :koiId")
    IdentificationCertificate findCertificateByKoiId(long koiId);






}
