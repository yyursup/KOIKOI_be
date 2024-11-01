package com.example.demo.repository;

import com.example.demo.entity.Koi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KoiRepository extends JpaRepository<Koi, UUID > {

    Koi findKoiById(UUID id);

}
