package com.criticaltechworks.pelsoczi.ui.navigation

sealed interface NavigationDestination {
    val route: String

    object StoriesDestination : NavigationDestination {
        override val route: String = "stories_route"
    }
}