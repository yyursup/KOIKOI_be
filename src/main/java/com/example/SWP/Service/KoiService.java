package com.example.SWP.Service;

import com.example.SWP.Enums.Author;
import com.example.SWP.Repository.CertificateRepository;
import com.example.SWP.Repository.KoiRepository;
import com.example.SWP.Repository.KoiTypeRepository;
import com.example.SWP.entity.IdentificationCertificate;
import com.example.SWP.entity.Koi;
import com.example.SWP.entity.KoiType;

import com.example.SWP.model.request.KoiRequest;
import com.example.SWP.model.response.KoiResponse;
import com.example.SWP.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.JMoleculesConverters;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KoiService {

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    KoiTypeRepository koiTypeRepository;

    public List<KoiResponse> getAllKoi() {
        List<Koi> koiList = koiRepository.findKoiByAuthor(Author.SHOP);
        return koiList.stream().map(koi ->
                modelMapper.map(koi, KoiResponse.class)).collect(Collectors.toList());
    }

    public IdentificationCertificate getCertificateByKoiId(long koiId) {
        return koiRepository.findCertificateByKoiId(koiId);
    }

    public KoiResponse createKoi(KoiRequest koiRequest, Long koiTypeId, Long identificationCertificateId) {
        Koi koi = modelMapper.map(koiRequest, Koi.class);
        koi.setAccount(accountUtils.getCurrentAccount());
        koi.setAuthor(Author.SHOP);


        KoiType koiType = koiTypeRepository.findById(koiTypeId)
                .orElseThrow(() -> new RuntimeException("KoiType not found"));

        IdentificationCertificate identificationCertificate = certificateRepository.findById(identificationCertificateId)
                .orElseThrow(() -> new RuntimeException("Not found"));


        koi.setKoiType(koiType);
        koi.setQuantity(1);
        koi.setCategory(koiType.getCategory());
        koi.setIdentificationCertificate(identificationCertificate);

        try {
            Koi newKoi = koiRepository.save(koi);
            koiRepository.save(newKoi);
            return modelMapper.map(newKoi, KoiResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Koi deleteKoi(Long koi) {
        Koi oldKoi = koiRepository.findKoiById(koi);
        if(oldKoi == null) {
            throw new RuntimeException("Koi not found");
        }
        oldKoi.setDeleted(true);
        return koiRepository.save(oldKoi);
    }

    public KoiResponse updateKoi(Long koi, KoiRequest koiRequest, Long id2) {
        try{
            Koi oldKoi = koiRepository.findKoiById(koi);

            IdentificationCertificate identificationCertificate = certificateRepository.findById(id2)
                    .orElseThrow(() -> new RuntimeException("Not found"));

            if(oldKoi != null) {
                oldKoi.setName(koiRequest.getName());
                oldKoi.setDescription(koiRequest.getDescription());
                oldKoi.setPrice(koiRequest.getPrice());
                oldKoi.setAge(koiRequest.getAge());
                oldKoi.setSize(koiRequest.getSize());
                oldKoi.setStatus(koiRequest.getStatus());
                oldKoi.setOrigin(koiRequest.getOrigin());
                oldKoi.setQuantity(koiRequest.getQuantity());
                oldKoi.setGender(koiRequest.getGender());
                oldKoi.setImage(koiRequest.getImage());
                oldKoi.setIdentificationCertificate(identificationCertificate);


                 koiRepository.save(oldKoi);
                 return modelMapper.map(oldKoi,KoiResponse.class);
            } else {
                throw new RuntimeException("Koi with id " + koi + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
