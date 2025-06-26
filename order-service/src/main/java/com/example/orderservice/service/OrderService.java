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
    private final ProdutoService produtoService;

    @Transactional
    public Order placeOrder(OrderRequest request) {

        Order order = new Order();
        order.setDataPedido(LocalDateTime.now());
        order.setStatus("PENDENTE");

        Produto produto;

        List<OrderItem>itens = new ArrayList<>();

        for (OrderItemDTO itensRequest : request.getItems()) {
            produto = produtoService.getProdutoById(itensRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itensRequest.getProductId()));

            if (produto.getEstoque_quantidade() < itensRequest.getQuantity()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // Associa o item ao pedido
            orderItem.setProdutoId(produto.getId());
            orderItem.setQuantidade(itensRequest.getQuantity());
            orderItem.setPrecoUnitario(produto.getPreco());
            itens.add(orderItem);
        }


        order.setItems(itens);


        BigDecimal totalPedido = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPedido);


        Order savedOrder = orderRepository.save(order);


        OrderDTO orderDTO = mapToOrderDTO(savedOrder, savedOrder.getItems());
        orderProducer.sendOrder(orderDTO);

        System.out.println("Pedido " + savedOrder.getId() + " criado e enviado ao Kafka.");
        return savedOrder;
    }

    private OrderDTO mapToOrderDTO(Order order, List<OrderItem> orderItems) {
        List<OrderItemDTO> itemDTOs = orderItems.stream()
                .map(item -> OrderItemDTO.builder()
                        .productId(item.getProdutoId())
                        .quantity(item.getQuantidade())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .items(itemDTOs)
                .dataPedido(order.getDataPedido())
                .status(order.getStatus())
                .total(order.getTotal())
                .build();
    }
}

