package com.criticaltechworks.pelsoczi.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.criticaltechworks.pelsoczi.ui.navigation.NavigationDestination.StoriesDestination
import com.criticaltechworks.pelsoczi.ui.stories.StoriesScreen

@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()
    Scaffold { paddingValues ->
        NewsApiNavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

@Composable
fun NewsApiNavHost(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = StoriesDestination.route,
        modifier = modifier,
    ) {
        composable(StoriesDestination.route) {
            StoriesScreen()
        }
    }
}