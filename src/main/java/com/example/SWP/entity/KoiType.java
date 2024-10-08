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

    @NotBlank(message = "This name can not be empty!")
    String name;

    @NotBlank(message = "This description can not be empty!")
    String description;

    boolean isDeleted = false;

    @OneToMany(mappedBy = "koiType", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Koi> kois;

}
