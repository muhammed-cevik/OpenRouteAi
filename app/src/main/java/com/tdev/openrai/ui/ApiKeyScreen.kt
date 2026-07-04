package com.tdev.openrai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tdev.openrai.ui.theme.*

@Composable
fun ApiKeyScreen(onSave: (String) -> Unit) {
    var key by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().background(Bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("OpenRouteAi", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "OpenRouter API anahtarini gir\nopenrouter.ai/keys adresinden alabilirsin",
                color = TextDim,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = key,
                onValueChange = { key = it },
                placeholder = { Text("sk-or-...", color = TextDim, fontSize = 14.sp) },
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (key.isNotBlank()) onSave(key) }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TextPrimary,
                    unfocusedBorderColor = Divider,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = TextPrimary,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(onClick = { visible = !visible }) {
                Text(
                    if (visible) "Gizle" else "Goster",
                    color = TextDim,
                    fontSize = 13.sp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        if (key.isNotBlank()) Color.White else Surface,
                        RoundedCornerShape(8.dp)
                    )
                    .then(
                        if (key.isNotBlank()) Modifier
                        else Modifier.border(1.dp, Divider, RoundedCornerShape(8.dp))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { if (key.isNotBlank()) onSave(key) },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = if (key.isNotBlank()) Color.Black else TextDim
                    ),
                    elevation = null
                ) {
                    Text("Devam", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
