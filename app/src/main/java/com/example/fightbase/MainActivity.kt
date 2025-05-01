package com.example.fightbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.fightbase.ui.theme.FightBaseTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentLocale by remember { mutableStateOf("en") }
            val context = remember(currentLocale) {
                LocaleHelper.setLocale(this, currentLocale)
            }
            CompositionLocalProvider(LocalContext provides context) {
                MainApp(onLanguageChanged = { lang -> currentLocale = lang })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(onLanguageChanged: (String) -> Unit) {
    var isDark by remember { mutableStateOf(true) }
    var deleteMode by remember { mutableStateOf(false) }
    var sortAsc by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    var fighters by remember { mutableStateOf(emptyList<FighterResponse>()) }
    var fights by remember { mutableStateOf(emptyList<FightRecordResponse>()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    fun loadFighters() {
        isLoading = true
        error = null
        scope.launch {
            fighters = try {
                ApiClient.fighterService.getFighters()
            } catch (e: Exception) {
                error = e.message
                emptyList()
            }
            isLoading = false
        }
    }

    fun loadFights() {
        isLoading = true
        error = null
        scope.launch {
            fights = try {
                ApiClient.fighterService.getFights()
            } catch (e: Exception) {
                error = e.message
                emptyList()
            }
            isLoading = false
        }
    }

    val displayed = if (sortAsc) fighters.sortedBy { it.id } else fighters.sortedByDescending { it.id }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val canNavigateBack = navController.previousBackStackEntry != null

    FightBaseTheme(darkTheme = isDark) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    navigationIcon = {
                        if (canNavigateBack && currentRoute != "home") {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    },
                    actions = {
                        if (currentRoute == "fighters") {
                            IconButton(onClick = { sortAsc = !sortAsc }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = stringResource(R.string.sort),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        IconButton(onClick = { isDark = !isDark }) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = stringResource(R.string.theme_toggle),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            floatingActionButton = {
                if (!deleteMode) {
                    when (currentRoute) {
                        "fighters" -> {
                            FloatingActionButton(onClick = { navController.navigate("add_fighter") }) {
                                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_fighter))
                            }
                        }
                        "fights" -> {
                            FloatingActionButton(onClick = { navController.navigate("add_fight") }) {
                                Icon(Icons.Default.SportsMma, contentDescription = stringResource(R.string.add_fight))
                            }
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Box(Modifier.fillMaxSize().padding(innerPadding)) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        deleteMode = false
                        HomeScreen(
                            navController = navController,
                            onLanguageSelected = { onLanguageChanged(it) },
                            onViewFighters = {
                                loadFighters()
                                navController.navigate("fighters")
                            },
                            onViewFights = {
                                loadFights()
                                navController.navigate("fights")
                            }
                        )
                    }

                    composable("fighters") {
                        FighterListScreen(
                            fighters = displayed,
                            isLoading = isLoading,
                            error = error,
                            showDelete = deleteMode,
                            onRefresh = { loadFighters() },
                            onFighterClick = { f -> navController.navigate("fighter_detail/${f.id}") },
                            onDeleteFighter = { id ->
                                scope.launch {
                                    ApiClient.fighterService.deleteFighter(id)
                                    loadFighters()
                                }
                            }
                        )
                    }

                    composable("fights") {
                        FightListScreen(
                            fights = fights,
                            isLoading = isLoading,
                            error = error,
                            showDelete = deleteMode,
                            onRefresh = { loadFights() },
                            onDeleteFight = { id ->
                                scope.launch {
                                    ApiClient.fighterService.deleteFight(id)
                                    loadFights()
                                }
                            }
                        )
                    }

                    composable("add_fighter") {
                        AddFighterScreen(navController) {
                            loadFighters()
                            navController.popBackStack()
                        }
                    }

                    composable("add_fight") {
                        AddFightScreen(navController) {
                            loadFights()
                            navController.popBackStack()
                        }
                    }

                    composable(
                        "update_fighter/{fighterId}",
                        arguments = listOf(navArgument("fighterId") { type = NavType.IntType })
                    ) { back ->
                        val id = back.arguments!!.getInt("fighterId")
                        fighters.find { it.id == id }?.let { fighter ->
                            UpdateFighterScreen(navController, fighter) {
                                loadFighters()
                                navController.popBackStack()
                            }
                        }
                    }

                    composable(
                        "fighter_detail/{fighterId}",
                        arguments = listOf(navArgument("fighterId") { type = NavType.IntType })
                    ) { back ->
                        val id = back.arguments!!.getInt("fighterId")
                        fighters.find { it.id == id }?.let {
                            FighterDetailScreen(
                                fighter = it,
                                onEdit = { navController.navigate("update_fighter/$id") }
                            )
                        } ?: Text("Fighter not found", Modifier.padding(16.dp))
                    }
                }

                if (currentRoute != "home") {
                    FloatingActionButton(
                        onClick = { deleteMode = !deleteMode },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.toggle_delete_mode))
                    }
                }
            }
        }
    }
}
