# 📦 Projeto Exemplo - Arquitetura Hexagonal com Spring Boot

Este projeto demonstra a aplicação da **Arquitetura Hexagonal (Ports and Adapters)** utilizando o ecossistema **Spring Boot**, com um CRUD simples de usuários persistido em banco **H2**, documentação com **Swagger**, e estrutura orientada à separação de responsabilidades.

---

## 🧠 Conceito

A **Arquitetura Hexagonal** promove o isolamento da **regra de negócio** dos detalhes de infraestrutura, facilitando testes, manutenção e evolução do sistema.

### Componentes principais:

- **Domínio**: Regras de negócio (modelo, casos de uso, portas).
- **Adapters**: Interfaces com o mundo externo (REST, JPA, etc.).
- **Ports**: Contratos de entrada e saída.
- **Config**: Orquestração (injeção de dependência e Spring Boot).

---

## 🧱 Estrutura do Projeto

```
src
└── main
    ├── java
    │   └── com.example.hexagonal
    │       ├── domain
    │       │   ├── model
    │       │   │   ├── Order.java
    │       │   │   ├── OrderItem.java
    │       │   │   └── Product.java
    │       │   ├── port
    │       │   │   ├── in
    │       │   │   │   ├── OrderUseCase.java
    │       │   │   │   └── ProductUseCase.java
    │       │   │   └── out
    │       │   │       ├── OrderRepositoryPort.java
    │       │   │       └── ProductRepositoryPort.java
    │       │   └── service
    │       │       ├── OrderService.java
    │       │       └── ProductService.java
    │       ├── adapter
    │       │   ├── in
    │       │   │   └── web
    │       │   │       ├── dto
    │       │   │       │   ├── OrderDTO.java
    │       │   │       │   ├── OrderDTOMapper.java
    │       │   │       │   ├── OrderItemDTO.java
    │       │   │       │   ├── OrderItemDTOMapper.java
    │       │   │       │   ├── ProductDTO.java
    │       │   │       │   └── ProductDTOMapper.java
    │       │   │       ├── OrderController.java
    │       │   │       └── ProductController.java
    │       │   └── out
    │       │       └── persistence
    │       │           ├── entity
    │       │           │   ├── OrderEntity.java
    │       │           │   ├── OrderItemEntity.java
    │       │           │   └── ProductEntity.java
    │       │           ├── mapper
    │       │           │   ├── OrderEntityMapper.java
    │       │           │   ├── OrderItemEntityMapper.java
    │       │           │   └── ProductEntityMapper.java
    │       │           ├── repository
    │       │           │   ├── OrderJpaRepository.java
    │       │           │   └── ProductRepository.java
    │       │           ├── OrderRepositoryAdapter.java
    │       │           └── ProductRepositoryAdapter.java
    │       └── config
    │           ├── AppConfig.java
    │           └── SpringDocConfig.java
    └── resources
        ├── application.properties
        ├── static
        └── templates
```

---

## 💠 Tecnologias Usadas

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 Database (memória)
- Swagger/OpenAPI (Springdoc)
- Maven

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Passos

```bash

# Compile e execute
mvn spring-boot:run
```

A aplicação será iniciada em:\
📍 `http://localhost:8080`

---

## 📖 Documentação da API

Após iniciar a aplicação, acesse:\
👉 **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📃 Banco de Dados

Este projeto utiliza o **H2 em memória** para fins de teste.

- Console H2: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:hexadb`
- Usuário: `sa`
- Senha: *(em branco)*

---

## 🧪 Testes

Os testes unitários focam na **camada de domínio**, desacoplados de frameworks.

```bash
mvn test
```

---

## 📌 Exemplo de Caso de Uso

- Porta de entrada: `CreateUserUseCase`
- Porta de saída: `UserRepository`
- Adaptador de entrada: `UserController`
- Adaptador de saída: `UserRepositoryImpl`

---

## 📚 Referências

- [Arquitetura Hexagonal - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Spring Boot + Swagger UI](https://springdoc.org/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

---

## 📝 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

