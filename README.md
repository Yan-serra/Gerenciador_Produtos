
# GerenciadorProdutos

API REST desenvolvida em Java e Spring Boot para gerenciar produtos e categorias, com armazenamento de dados no PostgreSQL.

O projeto permite cadastrar, consultar, atualizar e excluir registros, além de aplicar validações e autenticação.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA / Hibernate
- Spring Security
- PostgreSQL
- Maven
- Postman
- Git e GitHub

## Funcionalidades

**Categorias**
- Cadastrar, listar, buscar, atualizar e excluir.
- Impedir nomes duplicados.
- Impedir exclusão de categorias com produtos vinculados.

**Produtos**
- Cadastrar, listar, buscar, atualizar e excluir.
- Filtrar produtos por categoria.
- Validar nome, preço e categoria.
- Impedir nomes duplicados.

## Estrutura do projeto

O projeto utiliza uma organização em camadas:

- **Model:     ** representa as entidades do banco.
- **Repository:** realiza operações no PostgreSQL.
- **Service:   ** contém as regras de negócio.
- **Controller:** recebe e responde às requisições HTTP.
- **Config:    ** configura a segurança e o CORS.

O relacionamento entre Produto e Categoria é do tipo `ManyToOne`, permitindo que vários produtos pertençam à mesma categoria.

## Como executar

**Pré-requisitos:**
- JDK 21
- PostgreSQL
- Eclipse ou outra IDE Java
- Maven

**Passos:**

1. Clone o repositório e importe o projeto como Maven.

2. Crie o banco no PostgreSQL:

    CREATE DATABASE gerenciador_produtos;

3. Configure a conexão no arquivo `src/main/resources/application.properties`.

Exemplo de configuração:

    spring.datasource.url=jdbc:postgresql://localhost:5432/gerenciador_produtos
    spring.datasource.username=postgres
    spring.datasource.password=${DB_PASSWORD}

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    server.port=8080

4. Configure as variáveis de ambiente.

No Eclipse, utilize `Run > Run Configurations > Environment`.

| Variável       | Descrição                 | Exemplo                     |
|----------------|---------------------------|-----------------------------|
| `DB_PASSWORD`  | Senha do PostgreSQL       | Sua senha do PostgreSQL     |
| `APP_USER`     | Usuário para autenticação | `admin`                     |
| `APP_PASSWORD` | Senha para autenticação   | Uma senha definida por você |

5. Execute a classe `GerenciadorProdutosApplication.java`.

**Endereço da API:** `http://localhost:8080`

O Hibernate realiza o mapeamento das entidades para as tabelas do PostgreSQL.

## Endpoints

### Categorias

| Método | Endpoint           | Descrição         |
|--------|--------------------|-------------------|
| GET    | `/categorias`      | Listar categorias |
| GET    | `/categorias/{id}` | Buscar por ID     |
| POST   | `/categorias`      | Cadastrar         |
| PUT    | `/categorias/{id}` | Atualizar         |
| DELETE | `/categorias/{id}` | Excluir           |

### Produtos

| Método | Endpoint                            | Descrição             |
|--------|-------------------------------------|-----------------------|
| GET    | `/produtos`                         | Listar produtos       |
| GET    | `/produtos/{id}`                    | Buscar por ID         |
| GET    | `/produtos/categoria?categoriaId=1` | Filtrar por categoria |
| POST   | `/produtos`                         | Cadastrar             |
| PUT    | `/produtos/{id}`                    | Atualizar             |
| DELETE | `/produtos/{id}`                    | Excluir               |

As requisições GET são públicas. POST, PUT e DELETE exigem autenticação.

## Segurança e validações

A API utiliza **HTTP Basic Authentication**, com credenciais configuradas por variáveis de ambiente e senha codificada com BCrypt.

O CORS permite requisições da origem `http://localhost:3000`.

**Principais validações:**
- Campos obrigatórios.
- Preços maiores que zero.
- Nomes não duplicados.
- Categoria existente para cadastrar produtos.
- Tratamento de JSON inválido.
- Bloqueio da exclusão de categorias com produtos vinculados.

**Principais códigos HTTP:**
- `200 OK`           — operação realizada.
- `201 Created`      — registro criado.
- `400 Bad Request`  — dados inválidos.
- `401 Unauthorized` — autenticação ausente ou inválida.
- `403 Forbidden`    — requisição bloqueada pela configuração de acesso ou CORS.
- `404 Not Found`    — registro não encontrado.

## Testes

Os testes foram realizados no Postman, verificando:

- Operações CRUD de Produto e Categoria.
- Validação dos dados enviados.
- Relacionamento entre as entidades.
- Persistência no PostgreSQL.
- Autenticação HTTP Basic.
- Configuração de CORS.
- Tratamento de erros HTTP.

## Versionamento

O desenvolvimento foi organizado utilizando Git e GitHub.

- `main`            — versão final do projeto.
- `Desenvolvimento` — integração das funcionalidades.
- `feature-*`       — desenvolvimento de funcionalidades específicas.

Essa organização permitiu implementar e testar cada parte da API separadamente.