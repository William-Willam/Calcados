# 👟 ShoeStock — Sistema de Controle de Estoque de Calçados

![Java](https://img.shields.io/badge/Java-22-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?style=for-the-badge&logo=springboot)
![Angular](https://img.shields.io/badge/Angular-17-red?style=for-the-badge&logo=angular)
![JavaFX](https://img.shields.io/badge/JavaFX-22-blue?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)

> Sistema completo de controle de estoque de calçados com aplicação desktop para gestão interna e loja virtual web para clientes.

---

## 📋 Sobre o Projeto

O **ShoeStock** é um sistema full-stack composto por três módulos:

- **Backend (API REST)** — Spring Boot com autenticação JWT e banco PostgreSQL
- **Desktop (JavaFX)** — Aplicação para gestores e estoquistas gerenciarem o estoque
- **Web (Angular)** — Loja virtual para clientes navegarem e comprarem calçados

---

## 🏗️ Arquitetura

```
┌─────────────────────────┐
│    Spring Boot API       │  ← API REST centralizada
│    (porta 8080)          │
└────────────┬────────────┘
             │ HTTP / JSON + JWT
        ┌────┴────┐
        │         │
┌───────▼───┐ ┌───▼──────────┐
│  Angular  │ │   JavaFX     │
│  (Web)    │ │  (Desktop)   │
│ Loja      │ │  Gestão      │
└───────────┘ └──────────────┘
```

---

## 👥 Perfis de Usuário

| Perfil | Plataforma | Permissões |
|--------|-----------|------------|
| **Gestor** | Desktop | CRUD de estoquistas, visualizar estoque, deletar produtos |
| **Estoquista** | Desktop | Cadastrar, editar e listar produtos |
| **Cliente** | Web | Navegar na loja, filtrar calçados, gerenciar carrinho |

---

## ✨ Funcionalidades

### 🖥️ Aplicação Desktop (JavaFX)

**Gestor:**
- ✅ Login com autenticação segura
- ✅ Cadastrar, editar, listar e remover estoquistas
- ✅ Visualizar todos os produtos e quantidades em estoque
- ✅ Deletar produtos do sistema

**Estoquista:**
- ✅ Login com autenticação segura
- ✅ Cadastrar novos calçados (nome, descrição, preço, imagem, quantidade, tipo)
- ✅ Editar dados dos produtos
- ✅ Listar todos os produtos

### 🌐 Loja Virtual (Angular)

- ✅ Layout moderno e responsivo
- ✅ Vitrine de calçados em destaque
- ✅ Filtro por categoria: **Feminino** | **Masculino**
- ✅ Carrinho de compras
- ✅ Visualização detalhada do produto

---

## 🛠️ Tecnologias

### Backend
| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 22 | Linguagem principal |
| Spring Boot | 3.4.x | Framework da API |
| Spring Security + JWT | - | Autenticação e autorização |
| Spring Data JPA | - | Acesso ao banco de dados |
| PostgreSQL | 16 | Banco de dados |
| Lombok | - | Redução de boilerplate |
| Maven | - | Gerenciador de dependências |
| JUnit 5 + MockMvc | - | Testes |

### Desktop
| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| JavaFX | 22 | Framework de interface |
| Scene Builder | 25.x | Design visual das telas |
| Java HttpClient | 11+ | Consumo da API REST |

### Web
| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Angular | 17 | Framework frontend |
| Angular Material | - | Componentes de UI |
| TypeScript | - | Linguagem principal |
| RxJS | - | Programação reativa |

---

## 📁 Estrutura do Projeto

```
shoestock/
├── backend/                  # API Spring Boot
│   └── src/
│       ├── main/java/com/shoestock/
│       │   ├── entity/       # Entidades JPA
│       │   ├── repository/   # Repositórios
│       │   ├── service/      # Regras de negócio
│       │   ├── controller/   # Endpoints REST
│       │   ├── dto/          # Objetos de transferência
│       │   └── security/     # JWT e configurações
│       └── test/             # Testes JUnit
│
├── desktop/                  # Aplicação JavaFX
│   └── src/
│       ├── main/java/com/shoestock/
│       │   ├── controller/   # Controllers das telas
│       │   ├── service/      # Chamadas à API
│       │   └── model/        # Modelos de dados
│       └── resources/fxml/   # Arquivos de tela
│
└── frontend/                 # Loja Angular
    └── src/app/
        ├── components/       # Componentes visuais
        ├── services/         # Serviços HTTP
        ├── models/           # Interfaces TypeScript
        └── pages/            # Páginas da loja
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 22+
- Node.js 18+
- PostgreSQL 16+
- Maven 3.9+
- Angular CLI 17+

### 1. Banco de Dados

```sql
CREATE DATABASE shoestock;
```

### 2. Backend

```bash
cd backend
# Configure o application.properties com suas credenciais do PostgreSQL
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### 3. Desktop

```bash
cd desktop
mvn javafx:run
```

### 4. Web

```bash
cd frontend
npm install
ng serve
```

A loja estará disponível em: `http://localhost:4200`

---

## 📦 Modelo de Dados

### Product (Produto)
```
- id
- name          (nome do calçado)
- description   (descrição)
- price         (preço)
- imageUrl      (imagem)
- quantity      (quantidade em estoque)
- type          (FEMININO | MASCULINO)
- createdAt
- updatedAt
```

### User (Usuário)
```
- id
- name
- email
- password      (criptografado com BCrypt)
- role          (GESTOR | ESTOQUISTA | CLIENTE)
- createdAt
```

---

## 🧪 Testes

```bash
# Testes do backend
cd backend
mvn test

# Testes do frontend Angular
cd frontend
ng test
```

---

## 📸 Screenshots

> *Em breve*

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

---

## 👨‍💻 Autor

Desenvolvido por **William** como projeto de aprendizado full-stack.

[![GitHub](https://img.shields.io/badge/GitHub-William--Willam-black?style=flat&logo=github)](https://github.com/William-Willam)
