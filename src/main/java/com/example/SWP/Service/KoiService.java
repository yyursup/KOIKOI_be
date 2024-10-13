package com.example.SWP.Service;

import com.example.SWP.Repository.KoiRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.Koi;
import com.example.SWP.model.KoiRequest;
import com.example.SWP.model.KoiResponse;
import com.example.SWP.model.ViewProfileResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    TokenService tokenService;

    public List<KoiResponse> getAllKoi() {
        List<Koi> koiList = koiRepository.findKoisByIsDeletedFalse();
        return koiList.stream().map(koi ->
                modelMapper.map(koi, KoiResponse.class)).collect(Collectors.toList());
    }

    public KoiResponse createKoi(KoiRequest koiRequest) {
        Koi koi = modelMapper.map(koiRequest,Koi.class);
        try {
            Koi newKoi = koiRepository.save(koi);

            return modelMapper.map(newKoi,KoiResponse.class);

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

    public KoiResponse updateKoi(Long koi, KoiRequest koiRequest) {
        try{
            Koi oldKoi = koiRepository.findKoiById(koi);
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
