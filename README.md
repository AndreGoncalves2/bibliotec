<h1 align="center"> Bibliotec 📓</h1>

  https://github.com/AndreGoncalves2/bibliotec/assets/106348438/d434fd97-75af-4e72-bceb-44b7f638a5f4

## 🚀 Tecnologias

- **Java 21**: A linguagem robusta e versátil usada para o backend.

- **Spring Boot 3.2.3**: Spring Boot 3.2.3 é uma versão do popular framework Spring que oferece melhorias em desempenho e funcionalidades para desenvolvimento ágil de aplicações Java.

- **Vaadin 24.3.7**: Vaadin é um framework open-source para construção de interfaces de usuário web em Java.

- **MySQL**: Sistema de gerenciamento de banco de dados relacional de código aberto, amplamente utilizado por sua confiabilidade, desempenho e facilidade de uso.

- **JPA (Java Persistence API)**: Uma especificação do Java EE que define um padrão de mapeamento objeto-relacional para persistência de dados em aplicações Java.

## 💻 Projeto

Sistema de biblioteca que permite gerenciar registros de livros e alunos, além de possibilitar empréstimos.

## Principais funcionalidades:
### Usuários Comum
- **Registrar alunos:** Registra informações dos alunos, como número de matrícula, nome e turma.
- **Registrar livros:** Registra informações dos livros, como imagem da capa, código do livro, título, autor e sinopse.
- **Registrar empréstimo de livros:** Registra informações dos empréstimos, como aluno, livro, data do empréstimo e data do vencimento. 
- **Devolver um livro emprestado:** Registra a devolução do livro à biblioteca.

## Instalação 
- **Pré-requisitos**:
   - Certifique-se de ter o **Java 21** e o **Maven (mvn)** instalados em sua máquina. Você pode verificar a instalação executando os seguintes comandos no terminal:
     ```
     java -version
     mvn -version
     ```
- **Configuração do Arquivo `bibliotec.properties`**:
   - Baixe o arquivo chamado `bibliotec.properties` que esta no projeto.
   - Preencha esse arquivo com as informações necessárias, como a **conexão com o banco de dados** e o **diretório onde as imagens serão salvas**.
     
- **Localização do Arquivo de Configuração**:
   - Coloque o arquivo `bibliotec.properties` no diretório apropriado:
     - **Windows**: `C:\bibliotec`
     - **Linux**: `/etc/bibliotec.properties`

## Executando o Projeto
- Clone o repositório:

```shell
git clone https://github.com/AndreGoncalves2/bibliotec.git
```

- Navegue até o diretório do projeto:
  
```shell
cd bibliotec/
```

- Gere um build:
  
```shell
mvn clean package -Pproduction -Dvaadin.force.production.build=true -DskipTests=true
```

- Navegue até o diretório do app:
```shell
cd target/
```
-Inicie o servidor de desenvolvimento:

```shell
java -jar biblio-tec-0.0.1.jar
```
Para acessar o projeto em desenvolvimento, basta abrir o navegador e digitar a URL http://localhost:8080/. Isso permitirá que você visualize e interaja com a aplicação. 🚀
