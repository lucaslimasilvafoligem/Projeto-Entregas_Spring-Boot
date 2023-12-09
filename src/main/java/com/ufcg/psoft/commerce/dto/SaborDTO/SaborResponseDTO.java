package com.ufcg.psoft.commerce.dto.SaborDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cliente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import jakarta.validation.constraints.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborResponseDTO {
    @JsonProperty("nome")
    private String nome;

    @JsonProperty("valorM")
    private Double precoM;

    @JsonProperty("valorG")
    private Double precoG;

    @JsonProperty("disponivel")
    @Builder.Default
    private boolean disponivel = true;

    @JsonProperty("clientesInteressados")
    @NotNull
    private Set<Cliente> clientesInteressados;
}