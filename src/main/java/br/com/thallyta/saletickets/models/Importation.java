package br.com.thallyta.saletickets.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class Importation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String client;
    private LocalDate birthDate;
    private String event;
    private LocalDate date;
    private String typeTicket;
    private Double value;
    private LocalDateTime importationDate;
}
