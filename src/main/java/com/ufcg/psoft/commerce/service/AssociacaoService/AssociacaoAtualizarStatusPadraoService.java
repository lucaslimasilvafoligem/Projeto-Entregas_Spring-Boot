package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.exception.AssociacaoException.AssociacaoNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociacaoAtualizarStatusPadraoService implements AssociacaoAtualizarStatusService {
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Associacao atualizarStatus(
            Long entregadorId, Long estabelecimentoId, String codigoAcessoEstabelecimento
    ) throws EntregadorNaoExisteException, EstabelecimentoNaoEncontradoException, EntregadorIdInvalidoException,
            EstabelecimentoIdInvalidoException,
            AssociacaoNaoExisteException, CodigoDeAcessoInvalidoException

    {
        validarIds(entregadorId, estabelecimentoId);

        Entregador entregador = this.entregadorRepository
                .findById(entregadorId)
                .orElseThrow(EntregadorNaoExisteException::new);

        Estabelecimento estabelecimento = this.estabelecimentoRepository
                .findById(estabelecimentoId)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        validarCodigoAcesso(estabelecimento.getCodigoAcesso(), codigoAcessoEstabelecimento);
        Associacao associacao = this.associacaoRepository.retornarAssociacao(entregadorId, estabelecimentoId);
        validarAssociacao(associacao);

        associacao.setStatus(!associacao.isStatus());
        atualizarListaEntregadores(associacao.isStatus(), estabelecimento, entregador);
        return this.associacaoRepository.save(associacao);
    }

    private void atualizarListaEntregadores(boolean status, Estabelecimento estabelecimento, Entregador entregador) {
        if (status && entregador.isDisponibilidade()) {
            estabelecimento.addEntregador(entregador);
            this.estabelecimentoRepository.save(estabelecimento);
        }  else {
            estabelecimento.removerEntregador(entregador);
            this.estabelecimentoRepository.save(estabelecimento);
        }
    }

    private void validarAssociacao(Associacao associacao) {
        if (FuncoesValidacao.isNull(associacao)) throw new AssociacaoNaoExisteException();
    }

    private void validarIds(Long entregadorId, Long estabelecimentoId) {
        if (!FuncoesValidacao.validarId(entregadorId)) throw new EntregadorIdInvalidoException();

        if (!FuncoesValidacao.validarId(estabelecimentoId)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }

}
