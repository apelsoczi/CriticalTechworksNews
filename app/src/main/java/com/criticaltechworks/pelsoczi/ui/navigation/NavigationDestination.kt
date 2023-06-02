package com.criticaltechworks.pelsoczi.ui.navigation

sealed interface NavigationDestination {
    val route: String
    val routeWithArgs: String

    object StoriesDestination : NavigationDestination {
        override val route: String = "stories_route"
        override val routeWithArgs: String = "$route/"
    }

    object DetailDestination : NavigationDestination {
        val ARG_URL = "url"
        override val route: String = "detail_route"
        override val routeWithArgs: String = "$route/{$ARG_URL}"
    }

}