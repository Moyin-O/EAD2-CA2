package com.example.fightbase

sealed class Screen(val route: String) {
    object List : Screen("fighter_list")
    object Detail : Screen("fighter_detail/{id}") {
        fun createRoute(id: Int) = "fighter_detail/$id"
    }
    object Add : Screen("add_fighter")
    object Update : Screen("update_fighter/{id}") {
        fun createRoute(id: Int) = "update_fighter/$id"
    }
}
