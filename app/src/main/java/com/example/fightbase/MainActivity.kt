package com.example.fightbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.fightbase.ui.theme.FightBaseTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FightBaseTheme {
                val navController = rememberNavController()

                var fighters by remember { mutableStateOf<List<FighterResponse>>(emptyList()) }
                var isLoading by remember { mutableStateOf(true) }
                var error by remember { mutableStateOf<String?>(null) }

                val scope = rememberCoroutineScope()

                fun loadFighters() {
                    isLoading = true
                    error = null
                    scope.launch {
                        try {
                            val response = ApiClient.fighterService.getFighters()
                            fighters = response
                        } catch (e: Exception) {
                            error = e.message ?: "Unknown error"
                            e.printStackTrace()
                        } finally {
                            isLoading = false
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    loadFighters()
                }

                AppScaffold(
                    navController = navController,
                    fighters = fighters,
                    isLoading = isLoading,
                    error = error,
                    onReload = { loadFighters() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    fighters: List<FighterResponse>,
    isLoading: Boolean,
    error: String?,
    onReload: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FightBase") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "fighter_list") {
                composable("fighter_list") {
                    FighterListScreen(
                        fighters = fighters,
                        isLoading = isLoading,
                        error = error,
                        onFighterClick = { fighter ->
                            navController.navigate("fighter_detail/${fighter.name}")
                        },
                        onAddClick = {
                            navController.navigate("add_fighter")
                        }
                    )
                }

                composable("fighter_detail/{fighterName}") { backStackEntry ->
                    val fighterName = backStackEntry.arguments?.getString("fighterName")
                    val fighter = fighters.find { it.name == fighterName }
                    if (fighter != null) {
                        FighterDetailScreen(fighter)
                    } else {
                        Text(
                            "Fighter not found",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        )
                    }
                }

                composable("add_fighter") {
                    AddFighterScreen(
                        navController = navController,
                        onFighterAdded = {
                            onReload()
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
