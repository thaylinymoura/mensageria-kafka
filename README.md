# Projeto de E-commerce com Microsserviços e Apache Kafka

**Disciplina:** Software Concorrente e Distribuído
**Curso:** Bacharelado em Engenharia de Software

## Componentes do Grupo
* [Thayliny Alves de Moura]


---

## 1. Descrição do Projeto

Este projeto implementa uma arquitetura de microsserviços para um sistema de e-commerce simulado. A comunicação entre os serviços é assíncrona, utilizando o Apache Kafka como broker de mensageria para processar pedidos em tempo real.

A arquitetura é composta por três serviços principais:
* **Order-Service:** Expõe uma API REST para o front-end, recebe os pedidos e os publica no tópico Kafka `orders`.
* **Inventory-Service:** Consome os pedidos do tópico `orders`, verifica e atualiza o estoque dos produtos no banco de dados e publica o resultado (sucesso ou falha) no tópico `inventory-events`.
* **Notification-Service:** Consome os eventos do tópico `inventory-events` e simula o envio de notificações (e-mail/SMS) para o cliente, registrando a ação no console.

## 2. Arquitetura

```
[Front-end (JS/HTML)] ----> [Order-Service (Porta 8080)] ----> [Kafka Topic: orders]
                                                                    |
                                                                    v
                  [Kafka Topic: inventory-events] <---- [Inventory-Service (Porta 8081)]
                               |
                               v
                  [Notification-Service (Porta 8082)] ----> (Log no Console)

```

## 3. Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot
* **Mensageria:** Apache Kafka
* **Banco de Dados:** MySQL
* **Build Tool:** Maven
* **Front-end:** HTML, CSS, JavaScript

## 4. Pré-requisitos

Antes de executar, certifique-se de que você tem os seguintes softwares instalados e em execução:
* Java (JDK 17 ou superior)
* Maven
* MySQL Server
* Apache Zookeeper
* Apache Kafka

## 5. Como Executar a Solução

1.  **Banco de Dados:**
    * Crie um schema/banco de dados no MySQL chamado `Produtos`.
    * (Opcional) Popule a tabela `produto` com dados iniciais. As tabelas serão criadas ou atualizadas automaticamente pelo Hibernate (`ddl-auto: update`).

2.  **Apache Kafka:**
    * Inicie o Zookeeper e o servidor Kafka.
    * Crie os tópicos necessários com os seguintes comandos:
      ```sh
      # Tópico para novos pedidos
      bin/kafka-topics.sh --create --topic orders --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

      # Tópico para eventos de resultado do inventário
      bin/kafka-topics.sh --create --topic inventory-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
      ```

3.  **Configuração dos Serviços:**
    * Em cada um dos três projetos (`order-service`, `inventory-service`), verifique o arquivo `src/main/resources/application.properties` (ou `.yml`) e ajuste as credenciais do banco de dados (`spring.datasource.password`).

4.  **Executando os Microsserviços:**
    * Abra um terminal para cada um dos três projetos.
    * Navegue até a pasta raiz de cada projeto e execute o seguinte comando Maven:
      ```sh
      mvn spring-boot:run
      ```
    * Certifique-se de que os três serviços (`order-service`, `inventory-service`, `notification-service`) iniciaram com sucesso em suas respectivas portas (8080, 8081, 8082).

5.  **Testando a Aplicação:**
    * Abra um navegador e acesse a interface da loja em `http://localhost:8080`.
    * Adicione produtos ao carrinho e finalize uma compra.
    * Observe os logs nos terminais de cada microsserviço para ver o fluxo de mensagens acontecendo em tempo real.

## 6. Respostas

### 6.1. Escalabilidade
... Explique como você poderia conseguir escalabilidade com o Broker utilizado?
No Apache Kafka, a escalabilidade é uma característica fundamental e é alcançada principalmente através de dois conceitos: Partições de Tópicos e Grupos de Consumidores.
#1. Particionamento de Tópicos: Um tópico no Kafka pode ser dividido em múltiplas partições. Cada partição é uma sequência ordenada e imutável de mensagens. Ao criar um tópico,
como o **orders**, podemos dividi-lo em várias partições.Essas partições podem ser distribuídas entre diferentes "brokers" (servidores) do cluster Kafka, 
permitindo que a carga de escrita e leitura seja distribuída horizontalmente.
#2. Paralelismo com Grupos de Consumidores (Consumer Groups): Podemos iniciar múltiplas instâncias do mesmo serviço (por exemplo, três instâncias do **Inventory-Service**) e 
configurá-las para pertencer ao mesmo group-id (ex: inventory-group). O Kafka garante que cada partição do tópico será lida por apenas uma instância de consumidor dentro do grupo.

A escalabilidade é alcançada aumentando o número de partições de um tópico e adicionando mais instâncias de consumidores no mesmo grupo para processar essas partições em paralelo.

### 6.2. Tolerância à Falha
... O que significa? Explique uma situação de falha que poderia ocorrer e como o Broker poderia tratá-la.

O cluster Kafka de 3 brokers (servidores). O tópico orders foi criado com 1 partição e um fator de replicação de 3 (replication-factor=3).

Isso significa que existem 3 cópias (réplicas) idênticas dessa partição, cada uma em um broker diferente.
Uma dessas réplicas é eleita a "Líder" (Leader). Todos os produtores escrevem e os consumidores leem apenas da réplica líder.
As outras duas réplicas são "Seguidoras" (Followers) e sua única função é copiar os dados da líder para se manterem sincronizadas.

Se o servidor desliga 

**Detecção da Falha:** O Kafka (através do Zookeeper ou do seu próprio protocolo Raft em versões mais novas) detecta que o broker líder ficou indisponível.
**Eleição de um Novo Líder:** O controlador do cluster automaticamente elege uma das réplicas seguidoras que estava sincronizada para se tornar a nova líder da partição.
**Redirecionamento:** Produtores e consumidores são informados sobre a nova líder e passam a se comunicar com ela.



### 6.3. Idempotência
... Explique esse conceito e como fazer para garanti-lo.

Idempotência é uma propriedade de uma operação que garante que, se ela for executada múltiplas vezes, o resultado será o mesmo como se tivesse sido executada apenas uma vez.
Em sistemas de mensagens como o Kafka, que por padrão garantem entrega "pelo menos uma vez" (at-least-once), a idempotência é crucial para evitar erros de processamento duplicado.

O Inventory-Service consome a mensagem do pedido 123. Ele deduz o estoque do produto A do banco de dados com sucesso. No entanto, antes que ele possa "avisar" ao Kafka que terminou 
de processar (fazer o "commit do offset"), o serviço falha e reinicia. Ao reiniciar, ele vai consumir a mensagem do pedido 123 novamente, e se a lógica não for idempotente, ele deduzirá
o estoque do produto A pela segunda vez, causando uma inconsistência grave.




