package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PedidoConfirmadoPorClientePadraoService implements PedidoConfirmadoPorClienteService{
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;

    public Pedido confirmar(Long clienteId, Long pedidoId)
            throws
            ClienteIdInvalidoException,
            PedidoNaoExisteException,
            CodigoDeAcessoInvalidoException
    {
        this.validarId(clienteId);

        if (FuncoesValidacao.validarId(pedidoId)) {
            this.validarAtores(pedidoId, clienteId);
            Pedido pedido = this.pedidoRepository.findById(pedidoId).orElseThrow(PedidoIdInvalidoException::new);
            pedido.setStatusEntrega("Pedido entregue");

            System.out.println(notificar(pedido));

            Entregador entregador = entregadorRepository.findById(pedido.getEntregadorId()).orElseThrow(EntregadorNaoExisteException::new);
            disponibilizarEntregador(entregador);

            return pedido;
        } else if (FuncoesValidacao.isNull(pedidoId)) {
            pedidoRepository.findAll().forEach(item -> {
                if (item.getClienteId().equals(clienteId)) {
                    pedidoRepository.deleteById(item.getId());
                }
            });
        }

        throw new EstabelecimentoIdInvalidoException();
    }

    private  void validarAtores(Long pedidoId, Long clienteId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Cliente clientePedido = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);

        if (!(clientePedido.getCodigoAcesso().equals(cliente.getCodigoAcesso()))) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }

    private String notificar(Pedido pedido) {
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(pedido.getEstabelecimentoId()).orElseThrow(EstabelecimentoNaoExisteException::new);
        return estabelecimento.notifica(pedido);
    }
    private void disponibilizarEntregador(Entregador entregador) {

        List<Estabelecimento> estabelecimentos = this.estabelecimentoRepository.findAllById(
                this.associacaoRepository
                        .retornarEstabelecimentosComAssociacaoEntregador(entregador.getId())
        );

        for (Estabelecimento estabelecimento : estabelecimentos) {
            if (
                    associacaoRepository.retornarAssociacao(entregador.getId(), estabelecimento.getId()).isStatus()
            ) {estabelecimento.addEntregador(entregador);}
        }
        this.estabelecimentoRepository.saveAll(estabelecimentos);

        }
    }