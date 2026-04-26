package com.grid07.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Bot {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String personaDescription;
}