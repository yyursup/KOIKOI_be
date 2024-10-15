package com.example.SWP.Service;

import com.example.SWP.Repository.KoiTypeRepository;
import com.example.SWP.entity.KoiType;
import com.example.SWP.model.request.KoiTypeRequest;
import com.example.SWP.model.response.KoiTypeResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KoiTypeService {
    @Autowired
    KoiTypeRepository koiTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<KoiTypeResponse> getAllKoiTypes() {
        List<KoiType> koiTypeList = koiTypeRepository.findKoiTypesByIsDeletedFalse();
        return koiTypeList.stream().map(koiTypes ->
                modelMapper.map(koiTypes, KoiTypeResponse.class)).collect(Collectors.toList());
    }

    public KoiTypeResponse createKoiTypes(KoiTypeRequest koiTypeRequest) {

        KoiType koiType = modelMapper.map(koiTypeRequest,KoiType.class);
        try {
            KoiType newKoiType = koiTypeRepository.save(koiType);

            return modelMapper.map(newKoiType, KoiTypeResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public KoiType deleteKoiTypes(Long koiTypes) {
        KoiType oldKoiType = koiTypeRepository.findKoiById(koiTypes);
        if(oldKoiType == null) {
            throw new RuntimeException("Koi not found");
        }
        oldKoiType.setDeleted(true);
        return koiTypeRepository.save(oldKoiType);
    }

    public KoiTypeResponse updateKoiTypes(Long koiTypes, KoiTypeRequest koiTypeRequest) {
        try{
            KoiType oldKoiTypes = koiTypeRepository.findKoiById(koiTypes);
            if(oldKoiTypes != null) {
                oldKoiTypes.setCategory(koiTypeRequest.getCategory());

                koiTypeRepository.save(oldKoiTypes);
                return modelMapper.map(oldKoiTypes,KoiTypeResponse.class);
            } else {
                throw new RuntimeException("Koi with id " + koiTypes + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}