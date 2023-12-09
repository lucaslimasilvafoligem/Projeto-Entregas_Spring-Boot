package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorAlterarStatusPadraoService implements EntregadorAlterarStatusService {
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public void alterarStatus(Long id, String codigoAcesso, String novoStatus)
            throws CodigoDeAcessoInvalidoException, EntregadorNaoExisteException, EntregadorIdInvalidoException
    {
        validarID(id);
        Entregador entregador = this.entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcesso);
        entregador.setStatus(novoStatus);

        this.entregadorRepository.save(entregador);
    }

    private void validarID(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}
