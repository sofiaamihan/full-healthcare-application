package com.example.fullhealthcareapplication.ui.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fullhealthcareapplication.ui.graphs.authNavGraph
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.factory.DiscoverServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.ui.components.MainContent
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    userInfoViewModelFactory: UserInfoViewModelFactory,
    discoverServiceViewModelFactory: DiscoverServiceViewModelFactory,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    tokenDataStore: TokenDataStore
){
    val coroutineScope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HOME // Change this whenever you want to test screens
    ){
        authNavGraph(
            navController = navController,
            userInfoViewModelFactory = userInfoViewModelFactory,
            healthServiceViewModelFactory = healthServiceViewModelFactory
        )
        composable(route = Graph.HOME){
            MainContent(
                discoverServiceViewModelFactory = discoverServiceViewModelFactory,
                userInfoViewModelFactory = userInfoViewModelFactory,
                healthServiceViewModelFactory = healthServiceViewModelFactory,
                tokenDataStore = tokenDataStore,
                toSignOut = {
                    coroutineScope.launch {
                        tokenDataStore.clearSession()
                        navController.navigate(Graph.AUTHENTICATION) {
                            popUpTo(Graph.ROOT) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}



