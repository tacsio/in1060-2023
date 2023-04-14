## Exercício 1

### Objetivo

API REST que consulta disciplinas disponíveis para realização de matrículas para os discentes. Na consulta de
disciplinas disponíveis considera-se que esta API pode enviar um campo extra com sugestões de matrículas enviadas por um
serviço externo de um modelo de IA "on-line" (que aumentaria o tempo de resposta total da
requisição).

Manter um throughput razoável no consumo de uma API REST é uma meta importante, visto que tanto o tempo de resposta
razoável quanto a possibilidade da sugestão otimizada são fatores interessantes para utilização dessa API.

### Condições Adaptação

Existem várias formas de adaptar o sistema e sua infraestrutura para tal. Desta forma, temos as seguintes regras:

- Quando a média do tempo de resposta das requisições começar a apresentar um aumento acima do limite estabelecido, a
  aplicação deve simplificar o seu algoritmo eliminando o passo extra de consulta ao modelo de IA "Fake" por uma sugestão 
  otimizada de disciplinas.
- Quando o throughput (requests/s) da API aumentar além do limite estabelecido ocorrerá um _scale-out_ da aplicação no 
  cluster o número de réplicas da API será aumentado.

### Info

- O modelo de IA no exemplo é ***FAKE*** (apenas para gerar um possível incremento no tempo de resposta da aplicação)
- O ambiente executará num cluster Kubernetes local (utilizando a ferramenta Kind, que executa um cluster Kubernetes
  sobre containers Docker), o que poderá enviesar o resultado das adaptações.

## Aplicação Adaptativa

|     **Dimensão**     | **Resposta**                                                                                                                                                                                  |
|:--------------------:|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Por que Adaptar?** | Manter um throughput razoável mesmo sob aumento do consumo em uma API REST.                                                                                                                   |
| **Quando Adaptar?**  | 1. Quando o tempo de resposta ultrapassar o limite aceitável;<br>2. Quando o throughput ultrapassar um limite pré-determinado;                                                                |
|  **Onde Adaptar?**   | 1. Infra-estrutura;<br>2. No comportamento da aplicação, desabilitando features do sistema.                                                                                                   |
|  **O que Adaptar?**  | 1. Número de instâncias da aplicação;<br>2. Features não essenciais que geram impacto no tempo de resposta da aplicação.                                                                      |
|  **Como Adaptar?**   | Utilizando feedback-loop centralizado externo a lógica a aplicação já instrumentalizada para exportar as principais métricas relacionadas ao objetivo de negócio e requisitos não funcionais. |

### Help

```bash 
http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/
```

### Diferenças Teóricas

Analisador já envia mudanças para o planejador. Na implementação o analisar identifica os sintomas com base nos objectos (goals).
O Planejador é que gera o plano, validando as ações conforme o estado atual do sistema. 