## Projeto

### Objetivo

Manter um throughput razoável no consumo de uma API REST em um deployment Kubernetes.

### Condições Adaptação

- Quando o throughput (requests/s) da API aumentar além do limite estabelecido ocorrerá um _scale-out_ da aplicação no 
  cluster o número de réplicas da API será aumentado.