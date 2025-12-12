# 📌 Jurispay API

### Sistema de Controle de Transações de Empréstimo

**Java 17 | Spring Boot | Clean Architecture | Liquibase | Docker | PostgreSQL**

---

## 📖 Visão Geral

O **Jurispay API** é um sistema backend projetado para gerenciar, controlar e auditar o ciclo completo de empréstimos pessoais.
O foco principal está na **automação de processos**, **rastreabilidade financeira**, **rigor nas regras de negócio**, e **estrutura profissional de arquitetura**, garantindo expansão futura.

Este serviço faz parte do ecossistema **Jurispay**, uma plataforma para gestão de crédito, onboarding, cobrança e relatórios financeiros.

---

## 🎯 Objetivos Principais

* Controlar todo o fluxo de empréstimos (solicitação → aprovação → contrato → pagamento → cobrança).
* Registrar e validar documentos obrigatórios de clientes.
* Calcular automaticamente juros, multas por atraso e ROI.
* Disponibilizar relatórios operacionais e gerenciais.
* Armazenar arquivos (comprovantes, fotos, PDFs) em repositório externo, gravando apenas o caminho no banco.
* Garantir conformidade com regras de negócio detalhadas e rastreabilidade de todas as ações.

---

## 🏛️ Arquitetura

O projeto segue **Clean Architecture**, com foco em isolamento da regra de negócio, testabilidade e alta manutenibilidade.

```
br.com.jurispay
├── api             → Controladores REST, DTOs, Handlers
├── application     → Casos de uso (UseCases), validações, mapeamentos
├── domain          → Entidades, modelos, exceções e regras de negócio
└── infrastructure  → JPA, repositórios, Liquibase, configs, file storage
```

### Benefícios da Clean Architecture

* Domínio independente de frameworks.
* Camadas bem definidas e desacopladas.
* Produto preparado para crescimento e novos módulos.
* Possibilidade futura de portar para microsserviços sem reescrever regra de negócio.

---

## 🧩 Modelos de Domínio

As entidades principais do sistema são:

* **Cliente** – dados pessoais, renda, ocupação, contatos.
* **DocumentoCliente** – comprovantes obrigatórios.
* **AnaliseCredito** – workflow de aprovação.
* **Emprestimo** – informações principais e condições contratuais.
* **Pagamento** – registros, atrasos e valores recebidos.
* **RecebimentoLog** – auditoria de pagamentos.
* **UsuarioSistema** – controle de acesso e papéis.

---

## 📦 Tecnologias Utilizadas

| Categoria                | Tecnologia                      |
| ------------------------ | ------------------------------- |
| Linguagem                | **Java 17**                     |
| Framework                | **Spring Boot 3.4+**            |
| Arquitetura              | **Clean Architecture**          |
| ORM                      | JPA/Hibernate                   |
| Banco de dados           | PostgreSQL                      |
| Migração de banco        | **Liquibase**                   |
| Documentação da API      | **Springdoc OpenAPI (Swagger)** |
| Mapeamento de objetos    | **MapStruct**                   |
| Processador de anotações | Lombok                          |
| Testes                   | JUnit 5 + Testcontainers        |
| Containerização          | **Docker**                      |
| Empacotamento            | Maven                           |

---

## 🧱 Estrutura de Pastas (completa)

```text
src/main/java/br/com/jurispay
├── api
│   ├── controller
│   ├── dto
│   ├── mapper
│   └── handler
│
├── application
│   ├── customer
│   ├── creditanalysis
│   ├── loan
│   ├── payment
│   ├── collection
│   ├── document
│   ├── user
│   └── common
│
├── domain
│   ├── customer
│   ├── creditanalysis
│   ├── loan
│   ├── payment
│   ├── collection
│   ├── document
│   ├── user
│   └── common
│
└── infrastructure
    ├── config
    ├── persistence
    │   ├── jpa
    │   │   ├── entity
    │   │   └── repository
    │   └── mapper
    ├── migration
    ├── filestorage
    ├── logging
    └── security
```

---

## 🛠️ Como Executar o Projeto

### 1. Clonar o projeto

```bash
git clone https://github.com/seuusuario/jurispay-api.git
cd jurispay-api
```

---

### 2. Subir o ambiente com Docker (API + PostgreSQL)

Crie o arquivo `docker-compose.yml`:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: jurispay
      POSTGRES_PASSWORD: jurispay
      POSTGRES_DB: jurispaydb
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  api:
    build: .
    container_name: jurispay-api
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
    depends_on:
      - postgres

volumes:
  pgdata:
```

---

### 3. Gerar imagem e iniciar containers

```bash
docker compose up --build
```

---

### 4. Acessar a API

Swagger UI disponível em:

```
http://localhost:8080/swagger-ui.html
```

ou

```
/swagger-ui/index.html
```

---

## 🧪 Testes com Testcontainers

O projeto está preparado para rodar testes de integração utilizando containers reais do PostgreSQL:

```bash
mvn test
```

Não é necessário ter PostgreSQL instalado localmente.

---

## 🔄 Controle de Migrações (Liquibase)

Os arquivos de changelog ficam em:

```
src/main/resources/db/changelog/
```

Changelog master:

```
db.changelog-master.yml
```

Cada mudança deve seguir o padrão:

```
001-create-customer-table.yml
002-create-loan-table.yml
...
```

---

## 🚀 Releases Planejadas

### **v0.1 – MVP Operacional**

* Cadastro básico de cliente
* Cadastro manual de empréstimo
* Cálculo automático de juros
* Pagamento simples
* Docker funcional

### **v0.2 – Onboarding Completo**

* Checklist de documentos
* Upload de arquivos
* Workflow de análise de crédito

### **v0.3 – Cobrança**

* Agenda de vencimentos
* Inadimplência
* Logs de recebimento

### **v0.4 – Relatórios e Dashboard**

* Indicadores financeiros
* Exportações CSV/PDF

### **v1.0 – Produção**

* Hardening de segurança
* Melhorias de performance
* Documentação completa
* Observabilidade

---

## 🔐 Segurança e LGPD

O sistema está preparado para:

* Mascaramento de dados sensíveis (CPF, chave PIX);
* Autenticação e autorização via Spring Security;
* Auditoria de ações relevantes;
* Boa prática de segregação de camadas e logs.

---

## 🤝 Contribuições

Contribuições, sugestões e pull requests são bem-vindos para a evolução do Jurispay.

---

## 📄 Licença

Projeto proprietário. Todos os direitos reservados.

