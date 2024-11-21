package com.example.SWP.Service;

import com.example.SWP.Repository.CertificateRepository;
import com.example.SWP.entity.IdentificationCertificate;
import com.example.SWP.model.request.CertificateRequest;
import com.example.SWP.model.response.CertificateResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    ModelMapper modelMapper;

    public CertificateResponse createCertificate(CertificateRequest certificateRequest) {
        IdentificationCertificate identificationCertificate = modelMapper.map(certificateRequest,IdentificationCertificate.class);
        try {
            IdentificationCertificate newCer = certificateRepository.save(identificationCertificate);

            return modelMapper.map(newCer, CertificateResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<CertificateResponse> getAllCertificate() {
        List<IdentificationCertificate> certificateList = certificateRepository.findCertificateByIsDeletedFalse();
        return certificateList.stream().map(certificate ->
                modelMapper.map(certificate, CertificateResponse.class)).collect(Collectors.toList());
    }

    public IdentificationCertificate deleteCertificate(Long certificate) {
        IdentificationCertificate oldCertificate = certificateRepository.findCertificateById(certificate);
        if(oldCertificate == null) {
            throw new RuntimeException("Certificate not found");
        }
        oldCertificate.setDeleted(true);
        return certificateRepository.save(oldCertificate);
    }

    public CertificateResponse updateCertificate(Long certificate, CertificateRequest certificateRequest) {
        try{
            IdentificationCertificate oldCertificate = certificateRepository.findCertificateById(certificate);
            if(oldCertificate != null) {
                oldCertificate.setImage(certificateRequest.getImage());

                certificateRepository.save(oldCertificate);
                return modelMapper.map(oldCertificate,CertificateResponse.class);
            } else {
                throw new RuntimeException("Certificate with id " + certificate + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
