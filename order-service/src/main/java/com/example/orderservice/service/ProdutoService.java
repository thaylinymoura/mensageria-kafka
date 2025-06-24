package com.example.orderservice.service;

import com.example.orderservice.model.Produto;
import com.example.orderservice.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository; // Injeção do repositório de Produto

    /**
     * Retorna uma lista de todos os produtos disponíveis.
     * @return Lista de objetos Produto.
     */
    public List <Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    /**
     * Busca um produto pelo seu ID.
     * @param id O ID do produto.
     * @return Um Optional contendo o Produto, se encontrado.
     */
    public Optional<Produto> getProdutoById(Long id) {
        return produtoRepository.findById(id);}
    }
