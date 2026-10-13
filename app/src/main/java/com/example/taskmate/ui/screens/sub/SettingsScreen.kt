package com.example.taskmate.ui.screens.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmate.ui.theme.BackgroundDark
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.SurfaceCardDark
import com.example.taskmate.ui.theme.SurfaceDark
import com.example.taskmate.ui.theme.TextPrimary
import com.example.taskmate.ui.theme.TextPrimaryDark
import com.example.taskmate.ui.theme.ThemeState
import com.example.taskmate.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    var displayMode by remember {
        mutableStateOf(if (ThemeState.isDarkMode) "Dark" else "Light")
    }
    var fontSize by remember { mutableStateOf("Regular") }
    var language by remember { mutableStateOf("English") }

    val isDark = ThemeState.isDarkMode
    val currentBg = if (isDark) BackgroundDark else White
    val currentText = if (isDark) TextPrimaryDark else TextPrimary

    Scaffold(
        containerColor = currentBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = currentText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = currentText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = currentBg)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SettingRowDropdown(
                    label = "Display Mode",
                    selectedValue = displayMode,
                    options = listOf("Light", "Dark", "System"),
                    onOptionSelected = { selected ->
                        displayMode = selected
                        ThemeState.isDarkMode = when (selected) {
                            "Dark" -> true
                            "Light" -> false
                            else -> systemInDark
                        }
                    }
                )
                SettingRowDropdown(
                    label = "Font Size",
                    selectedValue = fontSize,
                    options = listOf("Small", "Regular", "Large"),
                    onOptionSelected = { fontSize = it }
                )
                SettingRowDropdown(
                    label = "Language",
                    selectedValue = language,
                    options = listOf("English", "Indonesian"),
                    onOptionSelected = { language = it }
                )
            }

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueNormal)
            ) {
                Text(
                    text = "Log Out",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = White
                )
            }
        }
    }
}

@Composable
fun SettingRowDropdown(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isDark = ThemeState.isDarkMode

    val cardBackground = if (isDark) SurfaceCardDark else Color(0xFFDCEBFE)
    val pillBackground = if (isDark) SurfaceDark else White
    val currentText = if (isDark) TextPrimaryDark else TextPrimary

    Surface(
        color = cardBackground,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Label di sisi kiri
            Text(
                text = label,
                fontSize = 14.sp,
                color = currentText,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Dropdown pill di sisi kanan
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(pillBackground)
                        .clickable { expanded = true }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = selectedValue,
                        fontSize = 13.sp,
                        color = currentText,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = currentText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(pillBackground)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    fontSize = 13.sp,
                                    color = currentText
                                )
                            },
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}