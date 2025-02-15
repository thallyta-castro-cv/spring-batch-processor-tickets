# Spring Batch Processor - Tickets

Este projeto é uma aplicação desenvolvida com **Spring Batch** para processar arquivos **CSV**, converter os dados em objetos e armazená-los em um banco de dados.

## Funcionalidades

- 📂 **Leitura de Arquivo CSV**: Lê os dados de um arquivo CSV contendo informações sobre tickets.
- 🔄 **Conversão de Dados**: Mapeia os dados do CSV para objetos Java.
- 🗄️ **Persistência no Banco de Dados**: Armazena os dados processados no banco de dados utilizando **Spring Batch**.
- ⚙️ **Gerenciamento de Processamento**: Controla o fluxo de processamento dos dados, garantindo eficiência e robustez.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Batch**
- **PostgreSQL**
- **Lombok**
- **Maven**

## Como Executar o Projeto

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.x
- Banco de dados configurado (PostgreSQL)

### Passos para execução

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/thallyta-castro-cv/spring-batch-processor-tickets.git
   cd spring-batch-processor-tickets
   ```

2. **Configure o banco de dados**
   - No arquivo `application.properties`, configure as credenciais do banco de dados.

3. **Compile o projeto:**
   ```bash
   mvn clean install
   ```

4. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

## 📜 Estrutura do Projeto

```
📂 spring-batch-processor-tickets
 ┣ 📂 files                    # Diretório de arquivos
 ┣ 📂 imported-files            # Arquivos processados
 ┣ 📂 src/main/java/br/com/thallyta/saletickets
 ┃ ┣ 📂 batch
 ┃ ┃ ┣ 📂 config                # Configurações do Spring Batch
 ┃ ┃ ┣ 📂 importationjob
 ┃ ┃ ┃ ┣ 📂 processor          # Processamento e conversão dos dados
 ┃ ┃ ┃ ┣ 📂 reader             # Leitura e mapeamento dos dados
 ┃ ┃ ┃ ┣ 📂 tasklet            # Tarefas auxiliares do batch
 ┃ ┃ ┃ ┗ 📂 writer             # Escrita dos dados no banco
 ┃ ┃ ┗ 📂 common                # Exceções e classes utilitárias
 ┃ ┣ 📂 controller              # Controllers da aplicação
 ┃ ┣ 📂 domain                  # Modelos de dados
 ┃ ┗ 📂 SaleticketsApplication  # Classe principal da aplicação
 ┣ 📂 src/main/resources
 ┃ ┣ application.properties     # Configurações da aplicação
 ┃ ┗ data.csv                   # Arquivo CSV de exemplo
 ┣ pom.xml                      # Dependências do Maven
 ┗ README.md                    # Documentação do projeto
```

## Contato

Para dúvidas ou sugestões, entre em contato:

📧 [thallytacastro.dev@gmail.com](mailto:thallytacastro.dev@gmail.com)  
🌎 [LinkedIn](https://www.linkedin.com/in/thallyta-castro)  
📂 [GitHub](https://github.com/thallyta-castro-cv)
