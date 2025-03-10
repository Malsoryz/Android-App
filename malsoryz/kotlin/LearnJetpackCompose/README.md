# Learn Jetpack Compose
<img src="https://raw.githubusercontent.com/Malsoryz/Malsoryz/refs/heads/main/assets/png/kotlin.png" style="height: 40px; width: 40px; margin: 2px;" alt="kotlin"/> <img src="https://raw.githubusercontent.com/Malsoryz/Malsoryz/refs/heads/main/assets/png/android.png" style="height: 40px; width: 40px; margin: 2px;" alt="android"/> <img src="https://raw.githubusercontent.com/Malsoryz/Malsoryz/refs/heads/main/assets/png/android-studio.png" style="height: 40px; width: 40px; margin: 2px;" alt="android-studio"/> <img src="https://raw.githubusercontent.com/Malsoryz/Malsoryz/refs/heads/main/assets/png/jetpack-compose.png" style="height: 40px; width: 40px; margin: 2px;" alt="jetpack-compose"/>

## Navigation Bar
Ini adalah Navigation Bar yang saya gunakan di Jetpack Compose
```kotlin
// data class untuk menyimpan data dari item Navigation Bar
data class BottomNavigationItem(
    val route: String, 
    val selectedIcon: ImageVector, 
    val unSelectedIcon: ImageVector, 
    val label: String
)
// fungsi untuk membuat Navigation Bar
@Composable
fun BottomNavigationBar() {
    // list nya berupa isi dari kelas BottomNavigationItem yang dibuat dengan data class
    val navItems = listOf(
        BottomNavigationItem(
            route = "home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            label = "Home"
        ),
        // tambahkan lebih banyak sesuai keperluan
    )
    // state Navigation Bar
    var selectedItem by remember { mutableStateOf(navItems[0].route) }

    // membuat Navigation Bar berdasarkan kelas BottomNavigationItem
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
```
Saya menggunakan `data class` untuk membuat kelas untuk menyimpan beberapa tipe data dan mungkin mirip seperti konsep `key value`
(Jika ada cara yang lebih baik maka gunakan saja)

Sumber https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#navigationbar