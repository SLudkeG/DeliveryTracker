# 🚀 Delivery Tracker - Sistema de Gestão de Pedidos

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=for-the-badge&logo=springboot)
![Oracle](https://img.shields.io/badge/Oracle-Database-red?style=for-the-badge&logo=oracle)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13-orange?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/Docker-Container-blue?style=for-the-badge&logo=docker)

**Sistema enterprise para gestão e rastreamento de pedidos com arquitetura event-driven**

</div>

## 📋 Sobre o Projeto

O **Delivery Tracker**  é uma solução empresarial completa para gestão de pedidos com rastreabilidade em tempo real, arquitetura preparada para escala e auditoria corporativa.

### 🎯 Diferenciais Técnicos

| Diferencial | Descrição |
|-------------|-----------|
| ⚡ **Event-Driven Architecture** | Mensageria com RabbitMQ para sistemas desacoplados |
| 📊 **Auditoria** | Rastreabilidade completa de todos os eventos do sistema |
| 🐳 **Stack Enterprise** | Oracle Database + Docker + Testcontainers |
| 🧪 **Testing Profissional** | Testes de integração com banco em containers |
| 🏗️ **DDD & Clean Architecture** | Código organizado com separação de responsabilidades |

**Delivery Tracker** é um sistema backend que simula uma parte do TMS de grandes empresas de logística simulando como eles gerenciam pedidos e entregam notificações de entrega.

Ele permite:
- Criar pedidos
- Atualizar o status do pedido (ex.: CREATED → IN_TRANSIT → DELIVERED…)
- Registrar todos os eventos da entrega
- Notificar assincronamente (via RabbitMQ) quando algo acontece
- Consultar a timeline completa do pedido

---

#  Por que este sistema existe? (A História)

Minha experiência em logística me mostrou que sistemas de entrega exigem funcionalidades avançadas como auditoria de eventos e mensageria assíncrona, complementando as operações fundamentais de gestão de dados. Isso me fez construir um sistema que fizesse o seguinte:

- Registrar cada mudança no pedido com histórico completo
- Manter timeline de eventos para auditoria corporativa  
- Integrar serviços externos de forma desacoplada
- Processar mensagens assincronamente para maior resiliência
- Garantir consistência de dados em ambientes distribuídos.


| Funcionalidades                           |
|-------------------------------------------|
| Criar pedidos                             |
| Atualizar status                          |
| Timeline completa de eventos              |
| Publicação de eventos via RabbitMQ        |
| Persistência em Oracle com migrations (Flyway) |
| Testes unitários com Mockito              |
| Testcontainers (Oracle real para testes)  |
| Arquitetura limpa com camadas separadas   |
| Tratamento global de erros                |
| Docker-compose completo                   |

---
# 🧰 Tecnologias Utilizadas

### 🚀 Backend
- **Java 17**
- **Spring Boot 3**
- Spring Web  
- Spring Data JPA  
- Spring Validation  
- Spring AMQP  
- Jackson  

### 🗄️ Persistência
- Oracle XE (Docker)  
- Hibernate  
- Flyway  
- Testcontainers Oracle  

### 🐇 Mensageria
- RabbitMQ 3-management  
- Spring AMQP  
- JSON Message Converter  

### 🧪 Testes
- JUnit 5  
- Mockito  
- Testcontainers  

### 🛠️ Infra
- Docker  
- docker-compose  
- Maven  

---

# ▶️ Como executar

## 1) Subir o Oracle + RabbitMQ

```sh
docker-compose up -d
```
2) Rodar a aplicação
```sh
./mvnw spring-boot:run
```

A API sobe em:
```sh
> http://localhost:8080
```

```SH
RabbitMQ management:
👉 http://localhost:15672
 (user: user / password: password)
```

Como rodar os testes
```sh
./mvnw test
```

Testcontainers sobe um Oracle isolado automaticamente.

📡 Endpoints da API
➤ Criar pedido

POST /orders

Body:

```JSON
{
  "customerName": "João"
}
```
➤ Atualizar status

PUT /orders/{id}/status

```JSON
{
  "newStatus": "IN_TRANSIT"
}
```
➤ Listar pedidos

GET /orders

➤ Buscar pedido

GET /orders/{id}

➤ Timeline de eventos

GET /orders/{id}/events

Retorno:

```JSON
[
  {
    "eventType": "ORDER_CREATED",
    "description": "Pedido criado com sucesso",
    "eventTime": "2025-01-01T10:00:00"
  }
]
```

📂 Estrutura do Projeto
```DIR
src/
├── main/java/com/dt/delivery_tracker/
│ ├── config/         # Configurações (RabbitMQ, etc)
│ ├── controller/     # Endpoints REST
│ ├── domain/         # Domínio e lógica de negócio
│ ├── repository/     # Camada de persistência
│ └── messaging/      # Integração com mensageria
├── test/java/        # Testes unitários e de integração
└── resources/
├── application.yml   # Configurações da aplicação
└── db/migration/     # Scripts de banco
```
## 👨‍💻 Autor

**Sergio Ludke** - [LinkedIn](https://www.linkedin.com/in/sergio-ludke-670262238/) - [GitHub](https://github.com/SLudkeG)

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes completos.

---

<div align="center">

[⬆ Voltar ao topo](#-delivery-tracker---sistema-de-gestão-de-pedidos)

</div>
