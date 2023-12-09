package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entregadores")
public class Entregador {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("placaVeiculo")
    @Column(nullable = false)
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    @Column(nullable = false)
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")
    @Column(nullable = false)
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @Column(nullable = false)
    @Size(min = 6, max = 6)
    private String codigoAcesso;

    @JsonProperty("disponibilidade")
    @Column(nullable = false)
    @Builder.Default
    private boolean disponibilidade = true;

    @JsonProperty("status")
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'Descanso'")
    private String status;
}
