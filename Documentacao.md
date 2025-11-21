# 📚 Documentação Técnica - Delivery Tracker

## 🏗️ Arquitetura do Sistema

### Diagrama de Componentes
O sistema utiliza uma arquitetura baseada em microsserviços e mensageria assíncrona.

```text
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│     Client      │    │   Spring Boot    │    │     Oracle      │
│   (Frontend/    │───▶│   Application    │───▶│    Database     │
│  API Consumer)  │    │                  │    │                 │
└─────────────────┘    └─────────┬────────┘    └─────────────────┘
                                 │
                                 ▼
                       ┌──────────────────┐
                       │     RabbitMQ     │
                       │  Message Broker  │
                       └──────────────────┘
                                 │
         ┌───────────────┼───────────────┐
         ▼               ▼               ▼
┌─────────────┐   ┌─────────────┐   ┌─────────────┐
│ Notification│   │  Analytics  │   │    Audit    │
│   Service   │   │   Service   │   │   Service   │
└─────────────┘   └─────────────┘   └─────────────┘
```

### Fluxo de Dados
1. **Client** → **Spring Boot App** (HTTP REST)
2. **Spring Boot App** → **Oracle Database** (JPA/Hibernate)
3. **Spring Boot App** → **RabbitMQ** (Event Publishing)
4. **RabbitMQ** → **Future Services** (Event Consumption)

## 🗃️ Modelo de Dados

### Entidade: Order
```sql
TABLE orders (
    id NUMBER PRIMARY KEY,
    customer_name VARCHAR2(255) NOT NULL,
    status VARCHAR2(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
)
```

### Entidade: OrderEvent
```sql
TABLE order_events (
    id NUMBER PRIMARY KEY,
    order_id NUMBER NOT NULL REFERENCES orders(id),
    event_type VARCHAR2(50) NOT NULL,
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR2(500)
)
```

### Enum: OrderStatus
```java
CREATED          // Pedido criado
DISPATCHED       // Pedido despachado
IN_TRANSIT       // Em trânsito
OUT_FOR_DELIVERY // Saiu para entrega
DELIVERED        // Entregue
CANCELLED        // Cancelado
```

## 🔄 Fluxos de Negócio

### 1. Criação de Pedido
1. `POST /orders` → `OrderService.createOrder()`
2. Valida dados de entrada
3. Persiste Order no banco
4. Cria OrderEvent "ORDER_CREATED"
5. Publica evento no RabbitMQ
6. Retorna Order criado

### 2. Atualização de Status
1. `PUT /orders/{id}/status` → `OrderService.updateStatus()`
2. Busca Order existente
3. Valida transição de status
4. Atualiza Order no banco
5. Cria OrderEvent "STATUS_CHANGED"
6. Publica evento no RabbitMQ
7. Retorna Order atualizado

## 📡 API Reference

**Base URL:** `http://localhost:8080`

### Schemas Comuns

**OrderResponse**
```json
{
  "id": 1,
  "customerName": "João Silva",
  "status": "IN_TRANSIT",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00"
}
```

**OrderEventResponse**
```json
{
  "id": 1,
  "eventType": "STATUS_CHANGED",
  "eventTime": "2024-01-15T11:45:00",
  "description": "Status atualizado para: IN_TRANSIT"
}
```

### Endpoints Detalhados

#### POST /orders
**Descrição:** Cria um novo pedido.

**Request Body:**
```json
{
  "customerName": "string (obrigatório)"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "customerName": "João Silva",
  "status": "CREATED",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

#### PUT /orders/{id}/status
**Descrição:** Atualiza o status de um pedido.

**Path Parameters:**
* `id`: number (obrigatório) - ID do pedido

**Request Body:**
```json
{
  "newStatus": "string (obrigatório, valores: CREATED|DISPATCHED|IN_TRANSIT|OUT_FOR_DELIVERY|DELIVERED|CANCELLED)"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "customerName": "João Silva",
  "status": "IN_TRANSIT",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00"
}
```

#### GET /orders/{id}/events
**Descrição:** Retorna a timeline de eventos de um pedido.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "eventType": "ORDER_CREATED",
    "eventTime": "2024-01-15T10:30:00",
    "description": "Pedido criado com sucesso"
  },
  {
    "id": 2,
    "eventType": "STATUS_CHANGED", 
    "eventTime": "2024-01-15T11:45:00",
    "description": "Status atualizado para: IN_TRANSIT"
  }
]
```

#### GET /events
**Descrição:** Retorna todos os eventos do sistema (auditoria global).

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "eventType": "ORDER_CREATED",
    "eventTime": "2024-01-15T10:30:00",
    "description": "Pedido criado com sucesso"
  }
]
```

## 🐇 Configuração RabbitMQ

### Exchange e Queue
```yaml
Exchange: "orders.exchange" (Topic)
Queue: "orders.queue" (Durable)
Routing Key: "orders.routingkey"
```

### Mensagens Publicadas

**Evento: ORDER_CREATED**
```json
{
  "orderId": 1,
  "eventType": "ORDER_CREATED",
  "timestamp": "2024-01-15T10:30:00",
  "customerName": "João Silva",
  "description": "Pedido criado com sucesso"
}
```

**Evento: STATUS_CHANGED**
```json
{
  "orderId": 1,
  "eventType": "STATUS_CHANGED", 
  "timestamp": "2024-01-15T11:45:00",
  "oldStatus": "CREATED",
  "newStatus": "IN_TRANSIT",
  "description": "Status atualizado para: IN_TRANSIT"
}
```

## 🧪 Estratégia de Testes

### Testes Unitários (`OrderServiceTest`)
* ✅ Criação de pedido
* ✅ Atualização de status
* ✅ Busca de pedido existente
* ✅ Tratamento de pedido não encontrado
* ✅ Validação de comportamentos com Mockito

### Testes de Integração
* ✅ Testcontainers com Oracle real
* ✅ Configuração automática de banco
* ✅ Isolamento completo entre testes

### Cobertura de Cenários
```java
// Casos positivos
testCreateOrder()
testUpdateStatus()
testFindById_OrderExists()

// Casos negativos  
testUpdateStatus_OrderNotFound()
testFindById_OrderNotFound()
```

## 🔧 Configurações

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/XEPDB1
    username: system
    password: oracle
  jpa:
    hibernate:
      ddl-auto: update
  rabbitmq:
    host: localhost
    port: 5672
    username: user
    password: password
```

### docker-compose.yml
```yaml
services:
  oracle:
    image: gvenzl/oracle-xe:21-slim
    environment:
      ORACLE_PASSWORD: oracle
    ports: ["1521:1521"]
    
  rabbitmq:
    image: rabbitmq:3-management  
    ports: ["5672:5672", "15672:15672"]
    environment:
      RABBITMQ_DEFAULT_USER: user
      RABBITMQ_DEFAULT_PASS: password
```

## 🚀 Deployment

### Ambiente Local
```bash
docker-compose up -d (infraestrutura)

./mvnw spring-boot:run (aplicação)
```

### Verificação
```bash
# Health Check
curl http://localhost:8080/orders

# RabbitMQ Management
http://localhost:15672 (user/user)
```

## 🔍 Troubleshooting

### Problemas Comuns

**Oracle não conecta:**
* Verificar se container está rodando: `docker ps`
* Validar credenciais no `application.yml`

**RabbitMQ connection refused:**
* Verificar se management interface está acessível
* Validar usuário/senha

**Testes falhando:**
* Verificar se Docker está rodando
* Testcontainers requer Docker disponível