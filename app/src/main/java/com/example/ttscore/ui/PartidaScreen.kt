package com.example.ttscore.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ttscore.ui.theme.TtscoreTheme
import com.example.ttscore.ui.viewmodel.MatchInProgressViewModel
import com.example.ttscore.util.Resource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaScreen(
    player1Name: String,
    player2Name: String,
    isCasual: Boolean,
    onFinish: () -> Unit,
    viewModel: MatchInProgressViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val darkNavy = Color(0xFF001F3F)
    val p1Blue = Color(0xFF0D47A1)
    val p2Red = Color(0xFFB71C1C)

    LaunchedEffect(player1Name, player2Name, isCasual) {
        viewModel.setupMatch(player1Name, player2Name, isCasual)
    }

    LaunchedEffect(uiState.saveResultStatus) {
        val status = uiState.saveResultStatus
        if (status is Resource.Success) {
            Toast.makeText(context, "Partida salva com sucesso!", Toast.LENGTH_SHORT).show()
            onFinish()
        } else if (status is Resource.Error) {
            Toast.makeText(context, "Erro ao salvar: ${status.message}", Toast.LENGTH_LONG).show()
        }
    }

    if (uiState.isMatchFinished) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = darkNavy,
            title = { Text("Fim de Partida", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { 
                Column {
                    Text("O vencedor é ${uiState.winnerName}!", color = Color.White, fontSize = 18.sp)
                    if (uiState.saveResultStatus is Resource.Loading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Salvando resultado...", color = Color.White.copy(alpha = 0.7f))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onFinish,
                    colors = ButtonDefaults.buttonColors(containerColor = p1Blue),
                    enabled = uiState.isCasual || uiState.saveResultStatus !is Resource.Loading
                ) {
                    Text("VOLTAR AO MENU", color = Color.White)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SportsTennis,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCasual) "TREINO CASUAL" else "PARTIDA RANKED",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = onFinish,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "SAIR",
                            color = darkNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = darkNavy,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkNavy
                )
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            PlayerScoreSide(
                playerName = uiState.player1Name,
                score = uiState.player1Score,
                sets = uiState.player1Sets,
                backgroundColor = p1Blue,
                onIncrement = { viewModel.incrementPlayer1Score() },
                onDecrement = { viewModel.decrementPlayer1Score() },
                modifier = Modifier.weight(1f)
            )

            PlayerScoreSide(
                playerName = uiState.player2Name,
                score = uiState.player2Score,
                sets = uiState.player2Sets,
                backgroundColor = p2Red,
                onIncrement = { viewModel.incrementPlayer2Score() },
                onDecrement = { viewModel.decrementPlayer2Score() },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PlayerScoreSide(
    playerName: String,
    score: Int,
    sets: Int,
    backgroundColor: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = playerName.uppercase(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ScoreButton(icon = Icons.Default.Add, onClick = onIncrement)
                
                Text(
                    text = score.toString(),
                    color = Color.White,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                ScoreButton(icon = Icons.Default.Remove, onClick = onDecrement)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SETS: $sets",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ScoreButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(56.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun PartidaScreenPreview() {
    TtscoreTheme {
        PartidaScreen(
            player1Name = "Pedro Lucas",
            player2Name = "Jonas Sarmento",
            isCasual = true,
            onFinish = {}
        )
    }
}
