package com.example.ttscore // Certifique-se de que este é o nome exato do seu pacote

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaDeTesteFirebase(
                        onTestarConexao = {
                            executarTesteFirebase()
                        }
                    )
                }
            }
        }
    }

    private fun executarTesteFirebase() {
        TODO("Not yet implemented")
    }

    // Função que faz o envio do dado para o Firestore
    private fun ejecutarTesteFirebase() {
        // Sintaxe moderna e padrão
        val db = com.google.firebase.Firebase.firestore


        // Criando os dados do usuário com a idade que conversamos
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
                Toast.makeText(this, "Conectado! Dado salvo no Firestore.", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { erro ->
                Log.e("FIREBASE_TESTE", "Falha na conexão", erro)
                Toast.makeText(this, "Erro: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }
}

@Composable
fun TelaDeTesteFirebase(onTestarConexao: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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

        Button(onClick = onTestarConexao) {
            Text(text = "Testar Conexão com Firebase")
        }
    }
}
