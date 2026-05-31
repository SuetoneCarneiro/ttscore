# Feature Buttons - TTScore

## Como utilizar

Basta importar o componente desejado na sua Screen e passar os parâmetros necessarios.

### Exemplo de uso: Registro

```kotlin
@Composable
fun MinhaTelaDeCadastro() {
    var user by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        TextField(value = user, onValueChange = { user = it }, label = { Text("Usuário") })
        // ... outros campos

        BotaoRegistrar(
            username = user,
            email = email,
            pass = password,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            onSuccess = {
                // Navegar para a Home ou mostrar mensagem de sucesso
            },
            onError = { erro ->
                // Mostrar um Snackbar ou Toast com a mensagem de erro
            }
        )
    }
}
```

## Componentes Disponíveis

| Componente | Função | Parâmetros Principais |
|---|---|---|
| `BotaoLogin` | Realiza o login na API | `username`, `pass` |
| `BotaoRegistrar` | Cria uma nova conta | `username`, `email`, `pass` |
| `BotaoCriarPartida` | Registra o placar de um jogo | `token`, `opponentId`, `player1Score`, `player2Score` |
| `BotaoEnviarPedidoAmizade` | Envia solicitação para outro user | `token`, `addresseeId` |
| `BotaoAceitarAmizade` | Aceita um convite pendente | `token`, `friendshipId` |
| `BotaoRemoverAmizade` | Exclui ou bloqueia amizade | `token`, `friendshipId` |
| `BotaoAtualizarPerfil` | Edita dados do usuário logado | `token`, `username?`, `avatarUrl?` |

## Comportamento Padrão

Todos os botões utilizam internamente o `LoadingButton`, o que garante que:
1.  **Estado de Loading**: Um `CircularProgressIndicator` aparece automaticamente enquanto a requisição está em curso.
2.  **Prevenção de Cliques Duplos**: O botão fica desabilitado automaticamente enquanto carrega.
3.  **Tratamento de Erros**: Você recebe a mensagem de erro da API diretamente no callback `onError`.

## Personalização

Para ajustar a aparência, use o parâmetro `modifier`. Todos os botões aceitam `Modifier` para que você possa definir largura, altura, margens e alinhamento.

```kotlin
BotaoLogin(
    username = "...",
    pass = "...",
    modifier = Modifier
        .height(56.dp)
        .clip(RoundedCornerShape(12.dp))
)
```

## Boa Prática
Estes componentes utilizam `hiltViewModel()` internamente. Isso significa que eles são independentes e podem ser colocados em qualquer lugar da hierarquia da UI, desde que a Activity/Fragment esteja anotada com `@AndroidEntryPoint`.
