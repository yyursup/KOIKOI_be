package com.example.SWP.Service;

import com.example.SWP.Repository.KoiTypeRepository;
import com.example.SWP.entity.KoiType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KoiTypeService {
    @Autowired
    KoiTypeRepository koiTypeRepository;

    public List<KoiType> getAllKoiTypes() {
        List<KoiType> koiTypeList = koiTypeRepository.findKoiTypesByIsDeletedFalse();
        return koiTypeList;
    }

    public KoiType createKoiTypes(KoiType koi) {
        try {
            KoiType newKoiType = koiTypeRepository.save(koi);
            return newKoiType;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public KoiType deleteKoiTypes(long koiTypeId) {
        KoiType oldKoiType = koiTypeRepository.findKoiById(koiTypeId);
        if(oldKoiType == null) {
            throw new RuntimeException("Koi not found");
        }
        oldKoiType.setDeleted(true);
        return koiTypeRepository.save(oldKoiType);
    }

    public KoiType updateKoiTypes(Long id, KoiType koiTypeUpdate) {
        try {
            KoiType oldKoiType = koiTypeRepository.findKoiById(id);
            if(oldKoiType != null) {
                oldKoiType.setName(koiTypeUpdate.getName());
                oldKoiType.setDescription(koiTypeUpdate.getDescription());
                return koiTypeRepository.save(oldKoiType);
            } else {
                throw new RuntimeException("Koitype with id " + id + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
