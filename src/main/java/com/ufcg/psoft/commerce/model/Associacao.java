package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.constraints.CodigoAcessoConstraint.CodigoAcessoConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.web.bind.annotation.PathVariable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "associacaoes")
public class Associacao {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("status")
    @Column(nullable = false)
    private boolean status;

    @JsonProperty("codigoAcesso")
    @Column(nullable = true)
    private String codigoAcesso;

    @JsonProperty("estabelecimentoId")
    @Column(nullable = false)
    private Long estabelecimentoId;

    @JsonProperty("entregadorId")
    @Column(nullable = false)
    private Long entregadorId;

}
