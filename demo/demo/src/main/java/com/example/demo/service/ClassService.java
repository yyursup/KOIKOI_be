package com.example.demo.service;

import com.example.demo.entity.ClassEntity;
import com.example.demo.model.ClassRequest;
import com.example.demo.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassService {
    @Autowired
    ClassRepository classRepository;

    public ClassEntity createNewClass (ClassRequest classRequest) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName(classRequest.getNameClass());
        return classRepository.save(classEntity);
    }

}
