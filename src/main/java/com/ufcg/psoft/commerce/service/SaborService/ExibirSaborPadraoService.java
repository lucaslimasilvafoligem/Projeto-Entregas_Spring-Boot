package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.stereotype.Service;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;

import java.util.List;
@Service
public class ExibirSaborPadraoService implements ExibirSaborService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Override
    public List<Sabor> listar(
            Long idSabor,
            Long idEstabelecimento,
            String codigoAcessoEstabelecimento
    ) throws
            EstabelecimentoNaoEncontradoException,
            SaborIdInvalidoException,
            SaborInexistenteException,
            EstabelecimentoIdInvalidoException,
            CodigoDeAcessoInvalidoException
    {
        if(FuncoesValidacao.isNull(idSabor)){
            return this.saborRepository.findAll();
        }
        validarIds(idSabor, idEstabelecimento);
        Estabelecimento estabelimentoSabor = this.estabelecimentoRepository
                .findById(idEstabelecimento).orElseThrow(EstabelecimentoNaoEncontradoException::new);
        validarCodigoAcesso(estabelimentoSabor.getCodigoAcesso(),codigoAcessoEstabelecimento);
        return List.of(this.saborRepository.findById(idSabor).orElseThrow(SaborInexistenteException::new));

    }

    private void validarIds(Long idSabor, Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idSabor)) throw new SaborIdInvalidoException();

        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}