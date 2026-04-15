# 🚀 Estoque API

API REST para controle de estoque, permitindo gerenciamento de produtos e movimentações de entrada e saída, com cálculo automático de custo médio e controle de valor total em estoque.

---

## 📌 Sobre o projeto

Este projeto foi desenvolvido com o objetivo de praticar:

- Desenvolvimento de APIs REST com Spring Boot
- Regras de negócio aplicadas em backend
- Arquitetura em camadas (Controller, Service, Repository)
- Uso de DTOs para entrada e saída de dados
- Mapeamento de objetos com Mapper
- Tratamento global de exceções
- Validação de dados com Bean Validation

---

## 🧰 Tecnologias utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- Bean Validation
- Maven
- PostgreSQL (ou H2 em desenvolvimento)

---

## 📦 Funcionalidades

### 🟢 Produtos
- Criar produto
- Listar produtos
- Buscar produto por ID
- Atualizar produto
- Remover produto

### 📊 Movimentações de estoque
- Entrada de estoque (stock in)
- Saída de estoque (stock out)
- Controle automático de quantidade
- Cálculo automático de custo médio
- Atualização do valor total do estoque

---

## 📐 Regras de negócio

- Não é permitido saída maior que o estoque disponível
- O custo médio é recalculado a cada entrada de estoque
- Quando o estoque chega a zero, o custo médio e valor total são zerados
- O valor unitário deve ser maior que zero
- Quantidade mínima de movimentação é 1

---

## 📥 Exemplo de requisição

### Entrada de estoque

```json
{
  "productId": 1,
  "quantity": 10,
  "unitCost": 25.50
}
