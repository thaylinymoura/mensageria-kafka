package com.example.orderservice.service;
import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.OrderItemDTO;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.kafka.OrderProducer;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.Produto;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderProducer orderProducer;
    private final ProdutoService produtoService; // Injeção de ProdutoService para buscar detalhes do produto

    /**
     * Processa um novo pedido recebido.
     * Realiza a persistência do pedido e seus itens, e envia o pedido para o Kafka.
     * @param request O DTO OrderRequest contendo os itens do pedido.
     * @return O objeto Order persistido.
     * @throws RuntimeException se um produto não for encontrado ou não houver estoque.
     */
        @Transactional
        public Order placeOrder(OrderRequest request) {
            Order order = new Order();
            // ID será gerado automaticamente pelo banco de dados (AUTO_INCREMENT)
            order.setDataPedido(LocalDateTime.now());
            order.setStatus("PENDENTE");

            // Remove a inicialização de totalPedido aqui, ele será calculado depois
            // BigDecimal totalPedido = BigDecimal.ZERO;
            List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
            // 1. Criar e associar os OrderItems ao Order, verificando produtos
            List<OrderItem> orderItems = request.getItems().stream()
                    .map(itemDto -> {
                        // Busca o produto para obter seu preço e verificar estoque
                        Produto produto = produtoService.getProdutoById(itemDto.getProductId())
                                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProductId()));

                        // Verifica se há estoque suficiente (lógica básica, será melhor tratada no Inventory-Service)
                        if (produto.getEstoque_quantidade() < itemDto.getQuantity()) {
                            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
                        }


                        // ID será gerado automaticamente pelo banco de dados (AUTO_INCREMENT)
                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order); // Associa o item ao pedido
                        orderItem.setProdutoId(itemDto.getProductId());
                        orderItem.setQuantidade(itemDto.getQuantity());
                        orderItem.setPrecoUnitario(produto.getPreco()); // Salva o preço unitário atual do produto

                        OrderItemDTO orderItemDTO = new OrderItemDTO();
                        orderItemDTO.setProductId(orderItem.getProdutoId());
                        orderItemDTO.setQuantity(orderItem.getQuantidade());
                        orderItemDTOList.add(orderItemDTO);

                        // A acumulação do totalPedido não acontece mais dentro deste lambda
                        return orderItem;
                    })
                    .collect(Collectors.toList());

            // 2. Calcula o total do pedido APÓS a criação de todos os OrderItems
            BigDecimal totalPedido = orderItems.stream()
                    .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma todos os subtotais

            order.setTotal(totalPedido); // Define o total do pedido

            System.out.println("Salavdno Pedido");

            System.out.println("Pedido:"+order.toString());


            // 3. Persistir o Order (e OrderItems em cascata)
            Order savedOrder = orderRepository.save(order);// Salva o pedido principal, que cascateia para os itens





            OrderDTO orderDTO = OrderDTO.builder()
                    .id(savedOrder.getId())
                    .items(orderItemDTOList)
                    .dataPedido(savedOrder.getDataPedido())
                    .status(savedOrder.getStatus())
                    .total(savedOrder.getTotal())
                    .build();


            // 4. Enviar o pedido completo para o Kafka
            orderProducer.sendOrder(orderDTO);

            System.out.println("Pedido " + savedOrder.getId() + " criado e enviado ao Kafka.");
            return savedOrder;
        }
    }