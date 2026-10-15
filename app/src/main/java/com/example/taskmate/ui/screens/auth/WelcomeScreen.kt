package com.example.taskmate.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.R
import com.example.taskmate.ui.theme.AppThemeColors

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppThemeColors.background)
            .clickable { onContinue() }, // Tap di layar untuk melanjutkan ke Login Screen
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_taskmate_logo),
            contentDescription = "TaskMate Logo",
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome to TaskMate",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppThemeColors.textPrimary
        )
    }
}