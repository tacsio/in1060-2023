## Exercício 1

### Objetivo

API REST que consulta disciplinas disponíveis para realização de matrículas para os discentes. Na consulta de
disciplinas disponíveis considera-se que esta API pode enviar um campo extra com sugestões de matrículas enviadas por um processamento externo de um modelo de IA on-line (que aumentaria o tempo de resposta total da
requisição).

Manter um throughput razoável no consumo de uma API REST é uma meta importante, visto que tanto o tempo de resposta
razoável quanto a possibilidade da sugestão otimizada são fatores interessantes para utilização dessa API.

### Condições Adaptação

Existem várias formas de adaptar o sistema e sua infraestrutura para tal. Desta forma, temos as seguintes regras:

- Quando a execução do Garbage Collector se tornar muito constante a infraestrutura onde a aplicação executa deverá mudar. Nesse
  caso, específico do aumento de execução do GC ocorrerá um _scale-up_ da JVM.
- Quando a média do tempo de resposta das requisições começar a apresentar um aumento acima do limite estabelecido, a
  infraestrutura realizada uma _scale-out_ aumentando o número de instâncias da API até um número limite.
- Se o número limite de instâncias for atingido e ainda assim a aplicação estiver respondendo além do limite definido,
  está deve simplificar o seu algoritmo eliminando o passo extra de consulta ao modelo de IA por uma sugestão otimizada de
  disciplinas.

### Info

- O modelo de IA no exemplo é ***FAKE*** (apenas para gerar um possível incremento no tempo de resposta da aplicação)
- O ambiente executará num cluster Kubernetes local (utilizando a ferramenta Kind, que executa um cluster Kubernetes
  sobre containers Docker), o que poderá enviesar o resultado das adaptações.

## Aplicação Adaptativa

|     **Dimensão**     | **Resposta**                                                                                                                                                                                                                                                |
|:--------------------:|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Por que Adaptar?** | Manter um throughput razoável mesmo sob aumento do consumo em uma API REST.                                                                                                                                                                                 |
| **Quando Adaptar?**  | 1. Quando o tempo de resposta ultrapassar o limite aceitável;<br>2. Quando o número de execuções do garbage collector aumentar além do “normal”;<br>3. Quando mesmo com várias instâncias em execução ao aplicação ainda apresentar alto tempo de resposta. |
|  **Onde Adaptar?**   | 1. Infra-estrutura;<br>2. Na arquitetura da aplicação, desabilitando features não essenciais ao negócio.                                                                                                                                                    |
|  **O que Adaptar?**  | 1. Recursos da JVM;<br>2. Número de instâncias da aplicação;<br>3. Features não essenciais que degradam o tempo de resposta.                                                                                                                                |
|  **Como Adaptar?**   | Utilizando feedback-loop centralizado externo a lógica a aplicação já instrumentalizada para exportar as principais métricas relacionadas ao objetivo de negócio e requisitos não funcionais.                                                               |

### Help

```bash 
http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/
```