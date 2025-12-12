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

## 🐳 Rodar com Docker

### Build e subir os containers

```bash
docker compose up --build -d
```

Este comando irá:
- Construir a imagem da API usando o Dockerfile multi-stage
- Subir o PostgreSQL 16 em um container
- Aguardar o banco ficar saudável antes de iniciar a API
- Executar as migrações do Liquibase automaticamente

### Ver logs da API

```bash
docker compose logs -f api
```

### Ver logs do banco de dados

```bash
docker compose logs -f db
```

### Parar os containers

```bash
docker compose down
```

### Reset do banco (apagar volume)

⚠️ **Atenção:** Isso apagará todos os dados do banco de dados.

```bash
docker compose down -v
```

### Portas dos Serviços

O projeto utiliza portas não padrão para evitar conflitos com outros projetos Docker:

| Serviço | Porta Externa | Porta Interna | URL |
|---------|---------------|---------------|-----|
| **API** | 18080 | 8080 | http://localhost:18080 |
| **PostgreSQL** | 15432 | 5432 | localhost:15432 |
| **MinIO API** | 19000 | 9000 | http://localhost:19000 |
| **MinIO Console** | 19001 | 9001 | http://localhost:19001 |

**Nota:** As portas internas são mantidas para comunicação entre containers. Apenas as portas externas foram alteradas.

### Acessar a API

Após subir os containers, a API estará disponível em:

- **Swagger UI:** http://localhost:18080/swagger-ui.html
- **API Base:** http://localhost:18080/api

### Smoke Test

Após subir os containers, execute os seguintes testes para validar que tudo está funcionando:

#### 1. Health Check da API

```bash
curl http://localhost:18080/actuator/health
```

Resposta esperada:
```json
{"status":"UP"}
```

#### 2. Swagger UI

Acesse no navegador:
```
http://localhost:18080/swagger-ui/index.html
```

#### 3. MinIO Console

Acesse no navegador:
```
http://localhost:19001
```

**Credenciais:**
- Access Key: `jurispay`
- Secret Key: `jurispay123`

### Estrutura Docker

O projeto inclui:

- **Dockerfile multi-stage:** Build otimizado com cache de dependências Maven
- **docker-compose.yml:** Orquestração de serviços (API + PostgreSQL + MinIO)
- **application-docker.properties:** Configurações específicas para ambiente Docker
- **Health checks:** Garantia de que o banco e MinIO estão prontos antes da API iniciar

---

## 📁 Armazenamento de Arquivos (MinIO)

O Jurispay utiliza **MinIO** como serviço de armazenamento de objetos compatível com S3 para gerenciar documentos e arquivos dos clientes.

### Acesso ao Console MinIO

Após subir os containers, acesse o console web do MinIO em:

**http://localhost:19001**

**Credenciais:**
- **Access Key:** `jurispay`
- **Secret Key:** `jurispay123`

### Bucket Padrão

O bucket `jurispay-documents` é criado automaticamente ao subir os containers. Este bucket é usado para armazenar:

- Documentos de clientes (CPF, RG, comprovantes)
- Comprovantes de pagamento
- Arquivos de análise de crédito
- Outros documentos relacionados ao sistema

### Configuração

As configurações do MinIO estão definidas em `application-docker.properties`:

```properties
jurispay.filestorage.provider=minio
jurispay.filestorage.bucket=jurispay-documents
jurispay.filestorage.endpoint=http://minio:9000
jurispay.filestorage.access-key=jurispay
jurispay.filestorage.secret-key=jurispay123
jurispay.filestorage.region=us-east-1
jurispay.filestorage.public-base-url=http://localhost:19000/jurispay-documents
```

### ⚠️ Segurança e LGPD

**IMPORTANTE:** 

- ⚠️ As credenciais acima são apenas para **desenvolvimento local**. Em produção, utilize variáveis de ambiente ou um gerenciador de segredos.
- 🔒 **Regra fundamental:** No banco de dados, armazenar **apenas o KEY/URL** do arquivo, **nunca o conteúdo binário**.
- 📋 Isso garante conformidade com LGPD e melhor performance do banco de dados.
- 🔐 Os arquivos ficam armazenados no MinIO com controle de acesso adequado.

### Comandos Úteis

```bash
# Subir todos os serviços (incluindo MinIO)
docker compose up --build -d

# Ver logs do MinIO
docker compose logs -f minio

# Parar todos os serviços
docker compose down

# Reset completo (apaga volumes de banco e MinIO)
docker compose down -v
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

