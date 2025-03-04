package com.malsoryz.learnjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.malsoryz.learnjetpackcompose.ui.theme.LearnJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnJetpackComposeTheme {
                Layout()
            }
        }
    }
}

@Composable
fun Layout() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Text(
            "Hello Compose",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BottomNavigationItem(val route: String, val selectedIcon: ImageVector, val unSelectedIcon: ImageVector, val label: String)

@Composable
fun BottomNavigationBar() {
    val navItems = listOf(
        BottomNavigationItem(
            route = "home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            label = "Home"
        ),
        BottomNavigationItem(
            route = "search",
            selectedIcon = Icons.Filled.Search,
            unSelectedIcon = Icons.Outlined.Search,
            label = "Search"
        ),
        BottomNavigationItem(
            route = "add",
            selectedIcon = Icons.Filled.Add,
            unSelectedIcon = Icons.Outlined.Add,
            label = "Add"
        ),
    )
    var selectedItem by remember { mutableStateOf(navItems[0].route) }

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.route,
                onClick = { selectedItem = item.route },
                icon = { Icon(
                    imageVector = if (selectedItem == item.route) item.selectedIcon else item.unSelectedIcon,
                    contentDescription = item.label
                ) },
                label = { Text(item.label) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    LearnJetpackComposeTheme {
        Layout()
    }
}