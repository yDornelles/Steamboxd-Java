# SteamBoxd

Este é um gerenciador de mídias de jogos (inspirado no *Letterboxd*), desenvolvido em Java como um projeto acadêmico para a disciplina de Programação Orientada a Objetos.

O objetivo principal era construir um sistema completo do zero, aplicando de forma correta e consistente os conceitos de POO, arquitetura de software e padrões de projeto.

## O que ele faz?

O sistema é um gerenciador de "catálogo" e "biblioteca" de mídias digitais, onde você pode:

* **Gerenciar a "Loja":** Criar, editar, listar e excluir **Jogos** e **DLCs**.
* **Gerenciar Usuários:** Criar, editar, listar e excluir **Usuários** (com validação de email duplicado).
* **Gerenciar Bibliotecas Pessoais:**
    * Adicionar/Remover mídias da "Loja" para a biblioteca de um usuário.
    * Atribuir uma **nota pessoal** (0-10) para uma mídia *dentro* da sua biblioteca, sem afetar a mídia original da loja.
* **Salvar e Carregar:** Todo o estado do sistema (jogos, DLCs e usuários) é salvo em um arquivo `steamboxd.json` ao sair e carregado ao iniciar.

## Arquitetura e Padrões de Projeto

O ponto central do projeto é a **forma** como ele foi construído, focando em baixo acoplamento e alta coesão.

### Separação de Camadas (além do Model-View)

O projeto separa as responsabilidades em 6 pacotes principais:

1.  **`model`**: As entidades puras (POJOs), sem lógica de negócio ou de interface.
2.  **`repository`**: Abstrai o acesso aos dados (gerencia as `ArrayLists`).
3.  **`service`**: O "cérebro" do sistema. Contém **toda** a lógica de negócio (ex: validar nota 0-10, clonar mídias, verificar email duplicado).
4.  **`controller`**: A "ponte" (fachada) que a `View` utiliza. Apenas traduz chamadas da `View` para o `Service`.
5.  **`data`**: Gerencia a infraestrutura de persistência (o Singleton e o DAO).
6.  **`view`**: A camada de apresentação (Textual ou Gráfica), sem nenhuma lógica de negócio.

### Padrões de Projeto Utilizados

* **Padrão Abstract Factory (Fábrica Abstrata):** O sistema pode rodar de dois jeitos: com uma **interface gráfica (Swing)** ou com uma **interface textual (console)**.
    * Para fazer essa troca de forma limpa, as interfaces `IAppView` e `UIFactory` foram criadas.
    * A **classe estática** `FabricaDeInterfaces` usa uma **configuração estática** (`TIPO_ATUAL`) para decidir qual fábrica concreta (`TextualUIFactory` ou `GraficaUIFactory`) deve ser instanciada.
    * O `Main.java` (no pacote padrão) apenas pede a fábrica e executa, sem nunca saber qual interface está rodando.

* **Padrão Singleton:**
    * A classe `Sistema` é um Singleton. Isso garante que existe apenas **uma** instância dos repositórios (`JogoRepository`, `DLCRepository`, `UsuarioRepository`) e do DAO em toda a aplicação, servindo como uma fonte central de verdade para os dados.

* **Padrão DAO (Data Access Object):**
    * A lógica de salvar e carregar os dados foi abstraída pela interface `PersistenciaDAO`.
    * A implementação concreta `JsonDAO` cuida dos detalhes de usar a biblioteca Gson. Isso desacopla o `Sistema` de *como* os dados são salvos.

* **Herança e Polimorfismo:**
    * Este é um conceito central de POO aplicado no projeto. Existe uma classe abstrata `Midia` que serve de base para `Jogo` e `DLC`.
    * A classe `Usuario` armazena um `ArrayList<Midia>`. Graças ao polimorfismo, ela consegue lidar com os dois tipos de mídia de forma transparente (ex: ao listar a biblioteca ou ao salvar/carregar).

* **Polimorfismo Avançado (Gson TypeAdapter):**
    * Como o Gson não sabe como deserializar uma interface (`List<Midia>`), foi implementado um `RuntimeTypeAdapterFactory`.
    * Essa fábrica "ensina" o Gson a adicionar um campo `"type": "Jogo"` ou `"type": "DLC"` ao JSON, permitindo que o polimorfismo funcione corretamente durante o carregamento dos dados.

## Bibliotecas Externas

* **Gson (Google):** Usada pelo `JsonDAO` para serializar (salvar) e deserializar (carregar) os objetos do sistema no formato JSON.

## Como Executar

1.  Clone o repositório.
2.  Abra o projeto na sua IDE (ex: IntelliJ).
3.  **Adicione a biblioteca Gson:**
    * Baixe o arquivo `.jar` do Gson (ex: `gson-2.10.1.jar`).
    * No IntelliJ, vá em `File` > `Project Structure...` > `Modules` > `Dependencies`.
    * Clique no `+` > `JARs or directories...` e adicione o arquivo `.jar` baixado.
4.  **Para escolher a interface:**
    * Vá em `steamboxd/view/FabricaDeInterfaces.java`.
    * Mude a constante estática `TIPO_ATUAL` para:
        * `TipoInterface.GRAFICA` (para a interface Swing)
        * `TipoInterface.TEXTUAL` (para a interface de console)
5.  Execute o arquivo `Main.java` (localizado na pasta `src/`, fora de qualquer pacote).