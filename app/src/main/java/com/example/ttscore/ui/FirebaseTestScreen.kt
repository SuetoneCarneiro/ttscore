package com.example.ttscore.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseTestScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teste Firebase") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Projeto TTScore",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Clique no botão abaixo para validar a conexão do seu app com o Firestore.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(onClick = { executarTesteFirebase(context) }) {
                Text(text = "Testar Conexão com Firebase")
            }
        }
    }
}

private fun executarTesteFirebase(context: android.content.Context) {
    val db = Firebase.firestore

    val usuarioTeste = hashMapOf(
        "nome" to "Pedro Academico",
        "projeto" to "TTScore",
        "status" to "Conectado com Sucesso",
        "idade" to 21
    )

    db.collection("usuarios")
        .add(usuarioTeste)
        .addOnSuccessListener { documentReference ->
            Log.d("FIREBASE_TESTE", "Sucesso total! ID: ${documentReference.id}")
            Toast.makeText(context, "Conectado! Dado salvo no Firestore.", Toast.LENGTH_LONG).show()
        }
        .addOnFailureListener { erro ->
            Log.e("FIREBASE_TESTE", "Falha na conexão", erro)
            Toast.makeText(context, "Erro: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
        }
}
