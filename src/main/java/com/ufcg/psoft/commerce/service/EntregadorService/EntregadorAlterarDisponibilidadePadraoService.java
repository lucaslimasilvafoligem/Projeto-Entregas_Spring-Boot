package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorDisponibilidadeInvalidaException;
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
public class EntregadorAlterarDisponibilidadePadraoService implements EntregadorAlterarDisponibilidadeService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public EntregadorResponseDTO alterarDisponibilidade(Long id, String codigoAcesso, boolean disponibilidade)
            throws
                CodigoDeAcessoInvalidoException,
                EntregadorNaoExisteException,
                EntregadorIdInvalidoException,
                EntregadorDisponibilidadeInvalidaException
    {
        validarID(id);

        Entregador entregador = this.entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);

        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcesso);

        // falta uma verifição: Se a disponibilidade recebida já estiver igual a que estava no sistema.
        // Tipo , a disponibilidade já era true e quer mudar p true. DEVE RETORNAR UMA EXCEÇÃO

        if (disponibilidade) {
            // Adicionar o entregador na lista de entregadores ativos de todos os estabelecimentos que ele está associado
        } else {
            // Remover o entregador na lista de entregadores ativos de todos os estabelecimentos que ele está associado
        }

        entregador.setDisponibilidade(disponibilidade);
        this.entregadorRepository.save(entregador);
        return this.modelMapper.map(entregador, EntregadorResponseDTO.class);
    }

    private void validarID(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}
