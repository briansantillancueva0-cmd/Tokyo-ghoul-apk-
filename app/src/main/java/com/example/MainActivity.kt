package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Gamepad
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.LauncherViewModel
import com.example.ui.components.FloatingPerformanceHud
import com.example.ui.screens.CharacterGuideScreen
import com.example.ui.screens.ControlAdapterScreen
import com.example.ui.screens.DriverInstallerScreen
import com.example.ui.screens.GameExecutionScreen
import com.example.ui.screens.LauncherHomeScreen
import com.example.ui.theme.GhoulDarkBg
import com.example.ui.theme.KaguneCrimson
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.QuinqueAccent
import com.example.ui.theme.QuinqueSilver

class MainActivity : ComponentActivity() {
    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(uiState.notificationMessage) {
                    uiState.notificationMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.dismissNotification()
                    }
                }

                if (uiState.isGameRunning) {
                    // Back handler while game session is active
                    BackHandler {
                        viewModel.exitGame()
                    }

                    GameExecutionScreen(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                } else {
                    // Back handler for main tabs
                    BackHandler(enabled = uiState.selectedTab != 0) {
                        viewModel.selectTab(0)
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.statusBars),
                        containerColor = GhoulDarkBg,
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = Color(0xFF16253D),
                                    contentColor = Color.White
                                )
                            }
                        },
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("main_navigation_bar"),
                                containerColor = Color(0xFF091220),
                                contentColor = QuinqueSilver,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = uiState.selectedTab == 0,
                                    onClick = { viewModel.selectTab(0) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                                            contentDescription = "Lanzador"
                                        )
                                    },
                                    label = { Text("Lanzador", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = QuinqueAccent,
                                        indicatorColor = Color(0xFF1E3A63),
                                        unselectedIconColor = QuinqueSilver.copy(alpha = 0.6f),
                                        unselectedTextColor = QuinqueSilver.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.testTag("tab_home_launcher")
                                )

                                NavigationBarItem(
                                    selected = uiState.selectedTab == 1,
                                    onClick = { viewModel.selectTab(1) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.selectedTab == 1) Icons.Filled.Memory else Icons.Outlined.Memory,
                                            contentDescription = "Drivers"
                                        )
                                    },
                                    label = { Text("Drivers", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = QuinqueAccent,
                                        indicatorColor = Color(0xFF1E3A63),
                                        unselectedIconColor = QuinqueSilver.copy(alpha = 0.6f),
                                        unselectedTextColor = QuinqueSilver.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.testTag("tab_drivers")
                                )

                                NavigationBarItem(
                                    selected = uiState.selectedTab == 2,
                                    onClick = { viewModel.selectTab(2) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.selectedTab == 2) Icons.Filled.Gamepad else Icons.Outlined.Gamepad,
                                            contentDescription = "Controles"
                                        )
                                    },
                                    label = { Text("Controles", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = KaguneCrimson,
                                        indicatorColor = Color(0xFF3B151E),
                                        unselectedIconColor = QuinqueSilver.copy(alpha = 0.6f),
                                        unselectedTextColor = QuinqueSilver.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.testTag("tab_controls")
                                )

                                NavigationBarItem(
                                    selected = uiState.selectedTab == 3,
                                    onClick = { viewModel.selectTab(3) },
                                    icon = {
                                        Icon(
                                            imageVector = if (uiState.selectedTab == 3) Icons.Filled.FlashOn else Icons.Outlined.Book,
                                            contentDescription = "Combos"
                                        )
                                    },
                                    label = { Text("Combos", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = KaguneCrimson,
                                        indicatorColor = Color(0xFF3B151E),
                                        unselectedIconColor = QuinqueSilver.copy(alpha = 0.6f),
                                        unselectedTextColor = QuinqueSilver.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.testTag("tab_combos")
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            AnimatedContent(
                                targetState = uiState.selectedTab,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "tab_navigation_transition"
                            ) { tab ->
                                when (tab) {
                                    0 -> LauncherHomeScreen(
                                        uiState = uiState,
                                        viewModel = viewModel,
                                        onNavigateToDrivers = { viewModel.selectTab(1) },
                                        onNavigateToControls = { viewModel.selectTab(2) }
                                    )
                                    1 -> DriverInstallerScreen(
                                        uiState = uiState,
                                        viewModel = viewModel
                                    )
                                    2 -> ControlAdapterScreen(
                                        uiState = uiState,
                                        viewModel = viewModel,
                                        onLaunchGameTest = { viewModel.launchGame() }
                                    )
                                    3 -> CharacterGuideScreen(
                                        uiState = uiState,
                                        viewModel = viewModel,
                                        onLaunchGame = { viewModel.launchGame() }
                                    )
                                }
                            }

                            // FLOATING REAL-TIME FPS & GPU OVERLAY WITHIN LAUNCHER
                            if (uiState.gameConfig.showFpsPanel) {
                                val activeDriver = uiState.drivers.firstOrNull { it.isSelected }
                                FloatingPerformanceHud(
                                    combatState = uiState.combat,
                                    resolution = uiState.gameConfig.resolution,
                                    hudStyle = uiState.controls.hudStyle,
                                    driverName = activeDriver?.name ?: "Turnip v24.3.0",
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
