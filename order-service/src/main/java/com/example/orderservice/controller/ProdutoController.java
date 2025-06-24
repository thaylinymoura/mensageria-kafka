package com.example.orderservice.controller;

import com.example.orderservice.model.Produto;
import com.example.orderservice.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Marca esta classe como um controlador REST
@RequestMapping("/products") // Define o caminho base para os endpoints
@RequiredArgsConstructor // Gera um construtor para injeção de dependência do ProdutoService
@CrossOrigin(origins = "*") // Permite requisições CORS de qualquer origem para o front-end
public class ProdutoController {

    private final ProdutoService produtoService; // Injeção do serviço de Produto

    /**
     * Endpoint para listar todos os produtos disponíveis.
     * Acessível via GET em /products.
     * @return ResponseEntity contendo a lista de produtos.
     */
    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {
        List<Produto> produtos = produtoService.getAllProdutos();
        return ResponseEntity.ok(produtos); // Retorna status 200 OK com a lista de produtos
    }
}
