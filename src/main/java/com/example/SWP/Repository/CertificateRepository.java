package com.example.SWP.Repository;

import com.example.SWP.entity.IdentificationCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<IdentificationCertificate, Long> {

    List<IdentificationCertificate> findCertificateByIsDeletedFalse();

    IdentificationCertificate findCertificateById(Long certificate);
}
