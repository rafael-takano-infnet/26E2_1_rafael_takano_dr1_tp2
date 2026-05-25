package com.example.passagemcli.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passagem {
    private Long id;
    private String passageiro;
    private Integer assento;
    private String origem;
    private String destino;
    private LocalDate data;
    private String status;
}
