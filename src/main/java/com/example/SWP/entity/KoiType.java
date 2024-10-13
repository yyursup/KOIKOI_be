package com.example.SWP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class KoiType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String category;

    boolean isDeleted = false;

    @OneToMany(mappedBy = "koiType", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Koi> kois;

}
