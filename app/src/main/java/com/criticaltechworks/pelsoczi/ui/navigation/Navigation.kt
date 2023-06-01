package com.criticaltechworks.pelsoczi.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.criticaltechworks.pelsoczi.R
import com.criticaltechworks.pelsoczi.ui.navigation.NavigationDestination.StoriesDestination
import com.criticaltechworks.pelsoczi.ui.stories.StoriesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.source_attribution),
                        contentDescription = null,
                    )
                },
            )
        }
    ) { paddingValues ->
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