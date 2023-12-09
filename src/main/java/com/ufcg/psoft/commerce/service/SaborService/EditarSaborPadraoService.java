package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborTipoInvalidoException;
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

@Service
public class EditarSaborPadraoService implements EditarSaborService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Override
    public Sabor editar(
            SaborPostPutRequestDTO saborPostPutRequestDTO,
            Long idSabor,
            Long idEstabelecimento,
            String codigoAcessoEstabelecimento
    ) throws
            EstabelecimentoIdInvalidoException,
            SaborIdInvalidoException,
            EstabelecimentoNaoEncontradoException,
            SaborInexistenteException,
            CodigoDeAcessoInvalidoException
    {
        validarIds(idSabor, idEstabelecimento);
        Estabelecimento estabelimentoSabor = this.estabelecimentoRepository
                .findById(idEstabelecimento).orElseThrow(EstabelecimentoNaoEncontradoException::new);

        validarCodigoAcesso(estabelimentoSabor.getCodigoAcesso(),codigoAcessoEstabelecimento);
        Sabor saborAlterado = modelMapper.map(saborPostPutRequestDTO,Sabor.class);

        saborRepository.findById(idSabor).orElseThrow(SaborInexistenteException::new);

        if (saborAlterado.getTipo().equals("tipo invalido")) throw new SaborTipoInvalidoException();

        return this.saborRepository.save(saborAlterado);
    }

    private void validarIds(Long idSabor, Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idSabor)) throw new SaborIdInvalidoException();

        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}