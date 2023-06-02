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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.criticaltechworks.pelsoczi.R
import com.criticaltechworks.pelsoczi.data.model.Headline
import com.criticaltechworks.pelsoczi.ui.detail.DetailScreen
import com.criticaltechworks.pelsoczi.ui.navigation.NavigationDestination.DetailDestination
import com.criticaltechworks.pelsoczi.ui.navigation.NavigationDestination.DetailDestination.ARG_URL
import com.criticaltechworks.pelsoczi.ui.navigation.NavigationDestination.StoriesDestination
import com.criticaltechworks.pelsoczi.ui.stories.StoriesScreen
import java.net.URLEncoder

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
        composable(route = StoriesDestination.route) {
            StoriesScreen(
                onReadStory = { headline: Headline ->
                    navController.navigate(
                        route = "${DetailDestination.route}/${URLEncoder.encode(headline.url)}",
                    )
                }
            )
        }
        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ARG_URL) { type = NavType.StringType }
            )
        ) {
            DetailScreen(
                navController = navController
            )
        }
    }
}