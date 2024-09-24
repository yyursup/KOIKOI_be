package com.example.SWP.Service;

import com.example.SWP.Repository.KoiRepository;
import com.example.SWP.entity.Account;
import com.example.SWP.entity.Koi;
import com.example.SWP.model.ViewProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KoiService {

    @Autowired
    KoiRepository koiRepository;

    public List<Koi> getAllKois() {
        List<Koi> koiList = koiRepository.findKoisByIsDeletedFalse();
        return koiList;
    }

    public Koi createKoi(Koi koi) {
        try {
            Koi newKoi = koiRepository.save(koi);
            return newKoi;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Koi deleteKoi(long koi) {
        Koi oldKoi = koiRepository.findKoiById(koi);
        if(oldKoi == null) {
            throw new RuntimeException("Koi not found");
        }
        oldKoi.setDeleted(true);
        return koiRepository.save(oldKoi);
    }

    public Koi updateKoi(Long koi, Koi koiUpdate) {
        try{
            Koi koiToUpdate = koiRepository.findKoiById(koi);
            if(koiToUpdate != null) {
                koiToUpdate.setName(koiUpdate.getName());
                koiToUpdate.setDescription(koiUpdate.getDescription());
                koiToUpdate.setPrice(koiUpdate.getPrice());
                koiToUpdate.setAge(koiUpdate.getAge());
                koiToUpdate.setSize(koiUpdate.getSize());
                koiToUpdate.setGender(koiUpdate.getGender());
                koiToUpdate.setStatus(koiUpdate.getStatus());
                koiToUpdate.setOrigin(koiUpdate.getOrigin());
                return koiRepository.save(koiToUpdate);
            } else {
                throw new RuntimeException("Koi with id " + koi + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
