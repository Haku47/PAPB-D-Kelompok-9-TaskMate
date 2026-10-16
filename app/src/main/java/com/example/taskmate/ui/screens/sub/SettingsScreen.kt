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
import com.example.taskmate.ui.theme.AppLanguage
import com.example.taskmate.ui.theme.BackgroundDark
import com.example.taskmate.ui.theme.BlueNormal
import com.example.taskmate.ui.theme.SurfaceCardDark
import com.example.taskmate.ui.theme.FontSizeMode
import com.example.taskmate.ui.theme.SurfaceDark
import com.example.taskmate.ui.theme.TextPrimary
import com.example.taskmate.ui.theme.TextPrimaryDark
import com.example.taskmate.ui.theme.ThemeMode
import com.example.taskmate.ui.theme.ThemeState
import com.example.taskmate.ui.theme.White
import com.example.taskmate.ui.theme.strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val s = strings
    val currentDisplayMode = when (ThemeState.themeMode) {
        ThemeMode.SYSTEM -> s.modeSystem
        ThemeMode.LIGHT -> s.modeLight
        ThemeMode.DARK -> s.modeDark
    }
    val currentFontSize = when (ThemeState.fontSizeMode) {
        FontSizeMode.SMALL -> s.fontSizeSmall
        FontSizeMode.REGULAR -> s.fontSizeRegular
        FontSizeMode.LARGE -> s.fontSizeLarge
    }
    val currentLanguage = when (ThemeState.language) {
        AppLanguage.ENGLISH -> s.langEnglish
        AppLanguage.INDONESIAN -> s.langIndonesian
    }

    val isDark = ThemeState.isDarkMode
    val currentBg = if (isDark) BackgroundDark else White
    val currentText = if (isDark) TextPrimaryDark else TextPrimary

    Scaffold(
        containerColor = currentBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = s.settingsTitle,
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
                    label = s.displayModeLabel,
                    selectedValue = currentDisplayMode,
                    options = listOf(s.modeSystem, s.modeLight, s.modeDark),
                    onOptionSelected = { selected ->
                        ThemeState.themeMode = when (selected) {
                            s.modeDark -> ThemeMode.DARK
                            s.modeLight -> ThemeMode.LIGHT
                            else -> ThemeMode.SYSTEM
                        }
                    }
                )
                SettingRowDropdown(
                    label = s.fontSizeLabel,
                    selectedValue = currentFontSize,
                    options = listOf(s.fontSizeSmall, s.fontSizeRegular, s.fontSizeLarge),
                    onOptionSelected = { selected ->
                        ThemeState.fontSizeMode = when (selected) {
                            s.fontSizeSmall -> FontSizeMode.SMALL
                            s.fontSizeLarge -> FontSizeMode.LARGE
                            else -> FontSizeMode.REGULAR
                        }
                    }
                )
                SettingRowDropdown(
                    label = s.languageLabel,
                    selectedValue = currentLanguage,
                    options = listOf(s.langEnglish, s.langIndonesian),
                    onOptionSelected = { selected ->
                        ThemeState.language = when (selected) {
                            s.langIndonesian, "Indonesian", "Indonesia" -> AppLanguage.INDONESIAN
                            else -> AppLanguage.ENGLISH
                        }
                    }
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
                    text = s.logoutButton,
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