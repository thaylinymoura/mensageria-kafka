package com.example.inventoryservice.notificationservice.sercice;

import org.springframework.stereotype.Service;

@Service
public class SmsService {


    public void enviarSMS(Long id, String status){
        System.out.println("================= NOTIFICAÇÃO SMS =================");
        System.out.println("Para Pedido ID: " + id);

        if ("SUCESSO".equals(status)) {
            System.out.println("Status: SUCESSO");
            System.out.println("Mensagem: Olá! O seu pedido foi confirmado com Sucesso. ");

        } else if("PENDENTE".equals(status)){
            System.out.println("Status: PENDENTE");
            System.out.println("Mensagem: Olá! O seu pedido esta Pendente ");
        }
        System.out.println("==============================================");
    }
}
