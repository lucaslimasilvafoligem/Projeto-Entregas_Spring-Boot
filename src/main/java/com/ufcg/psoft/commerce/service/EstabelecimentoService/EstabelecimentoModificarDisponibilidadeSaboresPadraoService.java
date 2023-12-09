package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EstabelecimentoModificarDisponibilidadeSaboresPadraoService implements EstabelecimentoModificarDisponibilidadeSaboresService{
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Boolean modificarDisponibilidadeSabor(Long idEstabelecimento, Long idSabor, Boolean disponibilidade)
            throws SaborInexistenteException, EstabelecimentoNaoEncontradoException, EstabelecimentoIdInvalidoException,
            SaborIdInvalidoException, SaborInexistenteException

    {
        validarId(idEstabelecimento);

        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(idEstabelecimento)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        Sabor sabor = this.saborRepository.findById(idSabor)
                .orElseThrow(SaborInexistenteException::new);

        verificarSabor(estabelecimento.getSabores(), sabor);

        for (Sabor s: estabelecimento.getSabores()) {
            if (s.equals(sabor)){
                if (!s.isDisponivel() && sabor.isDisponivel()) notificarClientes(s);
                s.setDisponivel(disponibilidade);
                this.saborRepository.save(s);
            }
        }
        for (Sabor s: estabelecimento.getSabores()) {
            System.out.println(s.isDisponivel());
        }
        System.out.println("qqqqqqqqqqqqqqq");
        System.out.println(disponibilidade);
        return disponibilidade;
    }

    private void verificarSabor(Set<Sabor> sabores,Sabor sabor) {
        if (!sabores.contains(sabor)) throw new SaborInexistenteException();
    }

    private void notificarClientes(Sabor sabor) {
        Set<Cliente> clientes = sabor.getClientesInteressados();
        StringBuilder mensagem = new StringBuilder("O sabor " + sabor.getNome() + " agora esta disponivel!");
        for (Cliente c: clientes){
            mensagem.append("\n").append(c.getNome());
        }
        System.out.println(mensagem);
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }
}
