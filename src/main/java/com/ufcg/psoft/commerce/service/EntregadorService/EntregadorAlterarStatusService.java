package com.ufcg.psoft.commerce.service.EntregadorService;

@FunctionalInterface
public interface EntregadorAlterarStatusService {
    void alterarStatus(Long id, String codigoAcesso, String novoStatus);
}
