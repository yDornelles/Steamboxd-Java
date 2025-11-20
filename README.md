# SteamBoxd-Java

![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=java)
![Plataforma](https://img.shields.io/badge/Plataforma-Swing%20(GUI)%20%7C%20Console%20(Textual)-orange)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

## Sobre o Projeto

Este é um gerenciador de mídias de jogos (inspirado no *Letterboxd*), desenvolvido em Java como um projeto acadêmico para a disciplina de Programação Orientada a Objetos. O objetivo principal era construir um sistema completo do zero, aplicando de forma correta e consistente os conceitos de POO, arquitetura de software e padrões de projeto.

O sistema permite o gerenciamento de um catálogo ('Loja') e de bibliotecas pessoais de usuários. O usuário pode cadastrar **Jogos** e **DLCs** na 'loja' do sistema. Em seguida, pode criar perfis de **Usuário**. Cada usuário pode, então, adicionar mídias da loja à sua biblioteca pessoal.

A principal funcionalidade de negócio é que, ao adicionar uma mídia, o sistema cria um **clone** do objeto, permitindo ao usuário atribuir uma **nota pessoal** (de 0 a 10) que não afeta a mídia original da loja.

Todo o estado da aplicação (listas de jogos, DLCs e usuários) é salvo em um arquivo `steamboxd.json` ao sair e recarregado ao iniciar.

## Funcionalidades Principais

* **Interface Dupla:** O sistema pode ser executado em modo **Gráfico (Swing)** ou **Textual (Console)** através de uma única configuração.
* **Gerenciamento da "Loja":** * CRUD completo para Jogos e DLCs.
    * **Vínculo Jogo-DLC:** O sistema gerencia a relação onde um Jogo pode possuir múltiplas DLCs, e uma DLC pertence a um Jogo base.
* **Gerenciamento de Usuários:** CRUD completo para Usuários, incluindo validação de email duplicado.
* **Biblioteca Pessoal:**
    * Adicionar/Remover mídias da "Loja" para a biblioteca de um usuário.
    * Atribuição de notas pessoais (0-10) que são independentes da "Loja".
* **Persistência de Dados:** Salvamento e carregamento automático do estado do sistema em formato JSON.

## Demonstração

Abaixo, uma breve demonstração das duas interfaces do sistema.

#### Interface Gráfica (Swing)
*(Implementada com `JTabbedPane` para separar as entidades e `JSplitPane` para o gerenciamento de usuários e suas bibliotecas)*
![Interface Gráfica do SteamBoxd](https://i.imgur.com/ZN9tM9b.png)

#### Interface Textual (Console)
*(Implementada com um loop de menu robusto e tratamento de entrada de usuário)*
![Interface Textual do SteamBoxd](https://i.imgur.com/zkuJtuN.png)

---

## Arquitetura e Conceitos Técnicos

O projeto foi estruturado para maximizar a **Separação de Responsabilidades (SoC)** e o **Baixo Acoplamento**, resultando em uma arquitetura de múltiplas camadas.

### Separação de Camadas (Multi-tier)

A arquitetura segue um padrão `Controller-Service-Repository` rigoroso, que isola a lógica de negócio das outras partes do sistema.

1.  **`View`** (GUI ou Textual): A camada mais externa. É "burra" e apenas exibe dados e captura a entrada do usuário. **Nunca** contém lógica de negócio.
    * *Destaque:* A GUI implementa `ChangeListeners` para sincronizar dados entre painéis.
2.  **`Controller`**: A "fachada" que a View acessa. Sua única função é traduzir as ações da View (ex: cliques de botão, comandos de texto) em chamadas para o `Service` e retornar o resultado.
3.  **`Service`**: O "cérebro" do sistema. **100% da lógica de negócio** reside aqui (ex: validar se uma nota é de 0-10, verificar se um email já existe, orquestrar a clonagem de mídias).
4.  **`Repository`**: Abstrai a coleção de dados. Gerencia a `ArrayList` de cada entidade, sem saber *por que* está salvando.
5.  **`Data`**: Camada de infraestrutura que lida com a persistência (leitura/escrita de arquivos).

### O Padrão Abstract Factory

Para atender ao requisito de interface flexível (GUI ou Textual), foi usado o padrão **Abstract Factory**:
* O `Main.java` (no pacote padrão) não conhece nenhuma implementação concreta. Ele apenas pede à classe estática `FabricaDeInterfaces` por uma `UIFactory`.
* A `FabricaDeInterfaces` lê uma **configuração estática** (`TIPO_ATUAL`) e decide se instancia uma `TextualUIFactory` ou uma `GraficaUIFactory`.
* Ambas as fábricas produzem um "produto" que obedece à interface `IAppView`, que o `Main.java` então executa.

### Herança e Polimorfismo (O Coração da POO)

* **Abstração:** A classe abstrata `Midia` define o contrato base (atributos como `titulo`, `nota`, e o método `getTipo()`).
* **Herança:** As classes `Jogo` e `DLC` estendem `Midia`.
    * `Jogo`: Possui lista de títulos de suas DLCs (`dlcTitulos`).
    * `DLC`: Possui referência ao seu jogo pai (`jogoBaseTitulo`).
* **Polimorfismo:** O conceito é usado de forma central na `List<Midia>` da classe `Usuario`. Isso permite que a biblioteca armazene ambos, `Jogo` e `DLC`, de forma transparente. As interfaces (`Repository<T>`, `MidiaService<T>`) também fazem uso pesado de genéricos e polimorfismo.

### Padrões de Infraestrutura (Singleton e DAO)

* **Singleton:** A classe `Sistema` é um Singleton, garantindo que todos os `Services` e `Views` acessem a **mesma instância** dos repositórios e do mecanismo de persistência.
* **DAO (Data Access Object):** A interface `PersistenciaDAO` abstrai a lógica de salvamento. A implementação concreta `JsonDAO` lida com os detalhes da biblioteca Gson, e o `Sistema` não precisa saber como o salvamento é feito.

### Desafio de Polimorfismo: Gson e TypeAdapters

Um desafio técnico significativo foi persistir a `List<Midia>`, pois o Gson, ao ler o JSON, não sabe se deve instanciar `new Jogo()` ou `new DLC()` (já que `Midia` é abstrata).

Isso foi resolvido com um `RuntimeTypeAdapterFactory` (uma ferramenta padrão do ecossistema Gson). Essa fábrica "ensina" o Gson a injetar um campo `"type": "Jogo"` ou `"type": "DLC"` no JSON ao salvar, permitindo que o polimorfismo funcione perfeitamente durante o carregamento dos dados.

## Diagrama UML

![Diagrama UML](https://i.imgur.com/fAqrBLg.png)

## Tecnologias e Dependências

* **Java 17**
* **Java Swing** (para a GUI)
* **Gson (Google)** (para persistência JSON)

## Como Executar

1.  Clone o repositório.
2.  Abra a pasta do projeto na sua IDE (ex: IntelliJ IDEA).
    * O IntelliJ detectará automaticamente o arquivo `pom.xml` e baixará as dependências (Gson).
3.  **Para escolher a interface:**
    * Abra o arquivo: `src/main/java/steamboxd/view/FabricaDeInterfaces.java`
    * Mude a constante estática `TIPO_ATUAL` para:
        * `TipoInterface.GRAFICA` (para a interface Swing)
        * `TipoInterface.TEXTUAL` (para a interface de console)
4.  **Execute o projeto:**
    * Encontre o arquivo `src/main/java/Main.java`.
    * Clique com o botão direito e selecione **Run 'Main.main()'**.