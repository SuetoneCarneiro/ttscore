package com.example.ttscore.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ttscore.ui.viewmodel.MatchInProgressViewModel

@Composable
fun PartidaScreen(
    player1Name: String,
    player2Name: String,
    onFinish: () -> Unit,
    viewModel: MatchInProgressViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(player1Name, player2Name) {
        viewModel.setupMatch(player1Name, player2Name)
    }

    if (uiState.isMatchFinished) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Fim de Partida") },
            text = { Text("O vencedor é ${uiState.winnerName}!") },
            confirmButton = {
                Button(onClick = onFinish) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001F3F)) // Dark blue background for the top bar area
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SportsTennis,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TTScore",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "FINALIZAR PARTIDA",
                    color = Color(0xFF001F3F),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color(0xFF001F3F),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Main Content (Split Screen)
        Row(modifier = Modifier.fillMaxSize()) {
            // Player 1 Side (Blue)
            PlayerScoreSide(
                playerName = uiState.player1Name,
                score = uiState.player1Score,
                sets = uiState.player1Sets,
                backgroundColor = Color(0xFF0D47A1),
                onIncrement = { viewModel.incrementPlayer1Score() },
                onDecrement = { viewModel.decrementPlayer1Score() },
                modifier = Modifier.weight(1f)
            )

            // Player 2 Side (Red)
            PlayerScoreSide(
                playerName = uiState.player2Name,
                score = uiState.player2Score,
                sets = uiState.player2Sets,
                backgroundColor = Color(0xFFB71C1C),
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
                fontSize = 24.sp,
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
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                ScoreButton(icon = Icons.Default.Remove, onClick = onDecrement)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SETS: $sets",
                color = Color.White,
                fontSize = 20.sp,
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
        modifier = Modifier.size(64.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
