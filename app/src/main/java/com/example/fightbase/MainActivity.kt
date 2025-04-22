package com.example.fightbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                            println("✅ Fetched fighters: ${response.size}")
                            response.forEach {
                                println("👉 Fighter: ${it.name} - ${it.weightClass}")
                            }
                            fighters = response
                        } catch (e: Exception) {
                            error = e.message
                            e.printStackTrace()
                        } finally {
                            isLoading = false
                        }
                    }
                }


                // Load on first launch AND reload trigger
                LaunchedEffect(isLoading) {
                    if (isLoading) {
                        loadFighters()
                    }
                }


                NavHost(navController, startDestination = "fighter_list") {
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
                        }
                    }

                    composable("add_fighter") {
                        AddFighterScreen(
                            navController = navController,
                            onFighterAdded = {
                                loadFighters()
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

