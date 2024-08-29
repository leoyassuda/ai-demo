# Aplicação com Spring AI e Ollama local

## Descrição

Esta aplicação utiliza Spring AI e Ollama local como uma forma de estudo. A ideia é criar um chatbot que responde às perguntas com base em uma base de dados.

## Dependências

- Java 21
- Spring Boot
- Spring AI
- Ollama local
- Lombok

## Como rodar a aplicação

1. Configure o ambiente de desenvolvimento seguindo os passos abaixo:
   - É necessário ter o Ollama local instalado e configurado corretamente.
     - Para obter informações sobre como instalar e configurar o Ollama, visite o site oficial do [Ollama](https://ollama.com/).
   - Para instalar e configurar o Spring Boot, acesse os tutoriais disponíveis no site oficial do [Spring Initializr](https://start.spring.io/), que incluem instruções para download e configuração.
   - Além disso, certifique-se de ter instalado as dependências necessárias para desenvolver aplicações com Spring AI. Para obter mais informações sobre a instalação do Spring AI, visite o site oficial do [Spring Framework](https://docs.spring.io/spring/docs/current/reference/html/).
   - Para instalar e configurar Lombok, acesse o site oficial do [Project Lombok](https://projectlombok.org/).
2. Execute a aplicação usando o comando abaixo:

   - Verifique se tem o modelo llama3.1, basta executar o comando abaixo:
     - `ollama list`
   - Caso não exista, execute o comando abaixo para baixar o modelo:
     - `ollama pull llama3.1`
   - Caso queira rodar via Docker, execute o comando abaixo:
     - `docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
     - Referência: [docker/ollama](https://hub.docker.com/r/ollama/ollama)
   - Para rodar a aplicação: `mvn spring-boot:run`
     - Isso iniciará a aplicação e ela estará disponível para uso no endereço [http://localhost:9292](http://localhost:9292).
   - Um exemplo de chamada da API pode ser feita usando o comando abaixo:

     ```curl -X POST \
        http://localhost:9292/api/v1/generate \
        -H 'Content-Type: application/json' \
        -d '{"query": "Quero saber até que horas fica aberto e se tem wifi para eu usar?"}'
     ```

## Exemplos de uso

- Na pasta `docs` existe um arquivo `faq_cafeteria.txt` com algumas perguntas e respostas para simular informações de uma Cafeteria.
- Você pode altera-lo mas lembre de trocar a referência do arquivo no código para que ele funcione corretamente. Em `RagConfiguration.java`.

Lembre-se de que este é apenas um exemplo básico e você deve adaptá-lo às necessidades específicas do seu projeto. Boa sorte com o desenvolvimento!
