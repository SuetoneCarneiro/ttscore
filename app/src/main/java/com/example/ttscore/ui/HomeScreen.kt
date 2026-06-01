package com.example.ttscore.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ttscore.ui.components.buttons.*
import com.example.ttscore.ui.viewmodel.FriendshipViewModel
import com.example.ttscore.ui.viewmodel.MatchViewModel
import com.example.ttscore.ui.viewmodel.UserViewModel
import com.example.ttscore.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToNovaPartida: () -> Unit,
    onNavigateToCadastrarJogadores: () -> Unit,
    onNavigateToListarJogadores: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToFirebaseTest: () -> Unit

) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val userViewModel: UserViewModel = hiltViewModel()
    val matchViewModel: MatchViewModel = hiltViewModel()
    val friendshipViewModel: FriendshipViewModel = hiltViewModel()

    val rankingState by userViewModel.rankingState.collectAsState()
    val matchListState by matchViewModel.matchListState.collectAsState()
    val pendingState by friendshipViewModel.pendingState.collectAsState()

    var username by remember { mutableStateOf("usuario_teste") }
    var email by remember { mutableStateOf("teste@email.com") }
    var password by remember { mutableStateOf("123456") }

    var userToken by remember { mutableStateOf("") }
    var loggedUserId by remember { mutableStateOf("") }
    var loggedUsername by remember { mutableStateOf("") }

    var opponentUsername by remember { mutableStateOf("") }
    var searchUsername by remember { mutableStateOf("") }
    var friendshipId by remember { mutableStateOf("") }
    var addresseeId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TTScore - Painel de Testes API") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Autenticação
            TestSection(title = "1. Autenticacao (Registro/Login)") {
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Senha") }, modifier = Modifier.fillMaxWidth())

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotaoRegistrar(
                        username = username,
                        email = email,
                        pass = password,
                        modifier = Modifier.weight(1f),
                        onSuccess = { auth ->
                            userToken = auth.token
                            loggedUserId = auth.user.id
                            loggedUsername = auth.user.username
                            Toast.makeText(context, "Registrado!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show() }
                    )
                    BotaoLogin(
                        username = username,
                        pass = password,
                        modifier = Modifier.weight(1f),
                        onSuccess = { auth ->
                            userToken = auth.token
                            loggedUserId = auth.user.id
                            loggedUsername = auth.user.username
                            Toast.makeText(context, "Logado!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show() }
                    )
                }
            }

            // Status da Sessão
            if (userToken.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Sessão Ativa", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        Text("ID: $loggedUserId", fontSize = 12.sp)
                        Text("Username: $loggedUsername", fontSize = 12.sp)
                        Text("Token: ${userToken.take(20)}...", fontSize = 12.sp)
                    }
                }
            }

            // Perfil
            TestSection(title = "2. Perfil (PUT /api/users/me)") {
                BotaoAtualizarPerfil(
                    token = userToken,
                    username = "${loggedUsername}_edit",
                    avatarUrl = null,
                    modifier = Modifier.fillMaxWidth(),
                    onSuccess = { Toast.makeText(context, "Perfil atualizado!", Toast.LENGTH_SHORT).show() },
                    onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                )
            }

            // Ranking
            TestSection(title = "3. Ranking (GET /api/users/ranking)") {
                Button(
                    onClick = { if (userToken.isNotEmpty()) userViewModel.buscarRanking(userToken) else Toast.makeText(context, "Faça login primeiro", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Buscar Ranking") }

                when (val state = rankingState) {
                    is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    is Resource.Success -> {
                        val list = state.data ?: emptyList()
                        Text("${list.size} usuário(s) no ranking:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        list.take(5).forEach { u ->
                            Text("  ${u.username}  —  V:${u.wins}  D:${u.losses}", fontSize = 12.sp)
                        }
                        if (list.size > 5) Text("  ... e mais ${list.size - 5}", fontSize = 12.sp, color = Color.Gray)
                    }
                    is Resource.Error -> Text("Erro: ${state.message}", color = Color.Red, fontSize = 12.sp)
                    null -> {}
                }
            }

            // Registrar Partida
            TestSection(title = "4. Registrar Partida (POST /api/matches)") {
                OutlinedTextField(
                    value = opponentUsername,
                    onValueChange = { opponentUsername = it },
                    label = { Text("Username do Oponente") },
                    modifier = Modifier.fillMaxWidth()
                )
                BotaoCriarPartida(
                    token = userToken,
                    opponentUsername = opponentUsername,
                    player1Score = 11,
                    player2Score = 9,
                    modifier = Modifier.fillMaxWidth(),
                    onSuccess = { Toast.makeText(context, "Partida registrada!", Toast.LENGTH_SHORT).show() },
                    onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                )
            }

            // Partidas do Usuário logado
            TestSection(title = "5. Minhas Partidas (GET /api/matches/me)") {
                Button(
                    onClick = { if (userToken.isNotEmpty()) matchViewModel.buscarMinhasPartidas(userToken) else Toast.makeText(context, "Faça login primeiro", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Buscar Minhas Partidas") }

                MatchListResult(matchListState)
            }

            // Partidas por Username
            TestSection(title = "6. Partidas por Username (GET /api/matches/user/{username})") {
                OutlinedTextField(
                    value = searchUsername,
                    onValueChange = { searchUsername = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (userToken.isNotEmpty() && searchUsername.isNotBlank())
                            matchViewModel.buscarPartidasPorUsername(userToken, searchUsername)
                        else Toast.makeText(context, "Preencha o username e faça login", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Buscar Partidas do Usuário") }

                MatchListResult(matchListState)
            }

            // Head-to-Head
            TestSection(title = "7. Head-to-Head (GET /api/matches/versus/{username})") {
                OutlinedTextField(
                    value = opponentUsername,
                    onValueChange = { opponentUsername = it },
                    label = { Text("Username do Oponente") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (userToken.isNotEmpty() && opponentUsername.isNotBlank())
                            matchViewModel.buscarHeadToHead(userToken, opponentUsername)
                        else Toast.makeText(context, "Preencha o oponente e faça login", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Ver Histórico Frente a Frente") }

                MatchListResult(matchListState)
            }

            // Amizades
            TestSection(title = "8. Enviar Pedido de Amizade (POST /api/friendships/request/{id})") {
                OutlinedTextField(
                    value = addresseeId,
                    onValueChange = { addresseeId = it },
                    label = { Text("ID do usuário (addresseeId)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotaoEnviarPedidoAmizade(
                        token = userToken,
                        addresseeId = addresseeId,
                        forceAccept = false,
                        modifier = Modifier.weight(1f),
                        onSuccess = { Toast.makeText(context, "Pedido enviado! Status: ${it.status}", Toast.LENGTH_SHORT).show() },
                        onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                    )
                    BotaoEnviarPedidoAmizade(
                        token = userToken,
                        addresseeId = addresseeId,
                        forceAccept = true,
                        modifier = Modifier.weight(1f),
                        label = "Aceitar Direto",
                        onSuccess = { Toast.makeText(context, "Amizade criada! Status: ${it.status}", Toast.LENGTH_SHORT).show() },
                        onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                    )
                }
            }

            // Pedidos Pendentes
            TestSection(title = "9. Pedidos Pendentes (GET /api/friendships/pending)") {
                Button(
                    onClick = { if (userToken.isNotEmpty()) friendshipViewModel.buscarPendentes(userToken) else Toast.makeText(context, "Faça login primeiro", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Buscar Pedidos Pendentes") }

                when (val state = pendingState) {
                    is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    is Resource.Success -> {
                        val list = state.data ?: emptyList()
                        Text("${list.size} pedido(s) pendente(s):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        list.forEach { f ->
                            Text("  De: ${f.requester.username} → Para: ${f.addressee.username}  [${f.status}]", fontSize = 12.sp)
                        }
                    }
                    is Resource.Error -> Text("Erro: ${state.message}", color = Color.Red, fontSize = 12.sp)
                    null -> {}
                }
            }

            //  Aceitar ou Remover Amizade
            TestSection(title = "10. Aceitar / Remover Amizade") {
                OutlinedTextField(
                    value = friendshipId,
                    onValueChange = { friendshipId = it },
                    label = { Text("ID da Amizade") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotaoAceitarAmizade(
                        token = userToken,
                        friendshipId = friendshipId,
                        modifier = Modifier.weight(1f),
                        onSuccess = { Toast.makeText(context, "Amizade aceita!", Toast.LENGTH_SHORT).show() },
                        onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                    )
                    BotaoRemoverAmizade(
                        token = userToken,
                        friendshipId = friendshipId,
                        modifier = Modifier.weight(1f),
                        onSuccess = { Toast.makeText(context, "Amizade removida!", Toast.LENGTH_SHORT).show() },
                        onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                    )
                }
            }

            // Navegação
            TestSection(title = "Navegação Principal") {
                Button(onClick = onNavigateToNovaPartida, modifier = Modifier.fillMaxWidth()) { Text("Nova Partida") }
                Button(onClick = onNavigateToCadastrarJogadores, modifier = Modifier.fillMaxWidth()) { Text("Cadastrar Jogadores") }
                Button(onClick = onNavigateToListarJogadores, modifier = Modifier.fillMaxWidth()) { Text("Listar Jogadores") }
                Button(onClick = onNavigateToRanking, modifier = Modifier.fillMaxWidth()) { Text("Tela Ranking (API)") }
                Button(onClick = onNavigateToFirebaseTest, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) { Text("Firebase Original") }
            }
        }
    }
}

@Composable
private fun MatchListResult(state: Resource<List<com.example.ttscore.domain.model.Match>>?) {
    when (state) {
        is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        is Resource.Success -> {
            val list = state.data ?: emptyList()
            Text("${list.size} partida(s):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            list.take(5).forEach { m ->
                Text("  ${m.player1.username} ${m.player1Score}x${m.player2Score} ${m.player2.username}", fontSize = 12.sp)
            }
            if (list.size > 5) Text("  ... e mais ${list.size - 5}", fontSize = 12.sp, color = Color.Gray)
        }
        is Resource.Error -> Text("Erro: ${state.message}", color = Color.Red, fontSize = 12.sp)
        null -> {}
    }
}

@Composable
fun TestSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            HorizontalDivider()
            content()
        }
    }
}
