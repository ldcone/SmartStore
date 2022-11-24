package com.example.smartstore.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartstore.R
import com.example.smartstore.ui.theme.CaffeBrown
import com.example.smartstore.ui.theme.CaffeDarkBrown
import com.example.smartstore.ui.theme.SmartStoreTheme
import com.example.smartstore.viewmodel.HOME
import com.example.smartstore.viewmodel.MYPAGE
import com.example.smartstore.viewmodel.MainViewModel
import com.example.smartstore.viewmodel.ORDER
import com.ssafy.smartstore.dto.User

@Composable
fun MainApp(viewModel: MainViewModel, user: User){
    val shownBottomNav by viewModel.shownNavigationBar.observeAsState()
    val navController = rememberNavController()

    // bottom navigation bar shown
    viewModel.setVisibleBottomNav(true)

    Scaffold(
        bottomBar = {
            if(shownBottomNav!!){
                BottomNavigation(navController = navController)
            }
        }
    ) { innerPadding->
        Box(Modifier.padding(innerPadding)){
            NavigationGraph(navController = navController, viewModel, user)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController){
    val items = listOf<BottomNavItem>(
        BottomNavItem.Home,
        BottomNavItem.Order,
        BottomNavItem.MyPage,
    )
    BottomNavigation(
        backgroundColor = CaffeBrown,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(26.dp)
                        .height(26.dp)
                )},
                label = {Text(stringResource(id = item.title), fontSize = 9.sp)},
                selectedContentColor = Color.White,
                unselectedContentColor = CaffeDarkBrown,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute){
                        navController.graph.startDestinationRoute?.let{
                            // startDestinationRoute 만 스택에 쌓이도록
                            popUpTo(it) { saveState = true }
                        }
                        // 화면 인스턴스 하나만 만들어짐
                        launchSingleTop = true
                        // 버튼 재클릭시 이전 상태가 남아있도록
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val title:Int,
    val icon:Int,
    val screenRoute:String
){
    object Home:BottomNavItem(R.string.bottom_home, R.drawable.home, HOME)
    object Order:BottomNavItem(R.string.bottom_order, R.drawable.shopping_list, ORDER)
    object MyPage:BottomNavItem(R.string.bottom_home, R.drawable.user, MYPAGE)
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: MainViewModel, user:User){
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screenRoute
    ){
        composable(BottomNavItem.Home.screenRoute){
            HomeApp(user, viewModel)
        }
        composable(BottomNavItem.Order.screenRoute){
            OrderApp(viewModel = viewModel)
        }
        composable(BottomNavItem.MyPage.screenRoute){
            MyPageApp(user, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartStoreTheme {
        MainApp(viewModel(),User("test","","유저01",0))
    }
}