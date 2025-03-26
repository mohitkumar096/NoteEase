package com.mohit.noteease.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohit.noteease.R
import androidx.navigation.NavController
import com.mohit.noteease.data.local.strings.Strings.Companion.appName
import com.mohit.noteease.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScreen(viewModel: NoteViewModel,navController: NavController, closeDrawer: () -> Unit) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //  app logo & Name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notes_icon1),
                    contentDescription = "Logo",
                    alignment = Alignment.CenterStart,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(13.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = appName,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 34.sp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            //  navigation Items
            Column(
                modifier = Modifier.weight(1f)
            ) {
                DrawerItem(
                    icon = Icons.Default.Note,
                    text = "All Notes",
                    selected = navController.currentDestination?.route == Screen.NoteList.route
                ) {
                    navController.navigate(Screen.NoteList.route)
                    closeDrawer()
                }

                DrawerItem(
                    icon = Icons.Default.PushPin,
                    text = "Pinned Notes",
                    selected = navController.currentDestination?.route == Screen.PinnedNotes.route
                ) {
                    navController.navigate(Screen.PinnedNotes.route)
                    closeDrawer()
                }

                DrawerItem(
                    icon = Icons.Default.Add,
                    text = "Create New",
                    selected = false
                ) {
                    viewModel.clearSelectedNote()
                    navController.navigate("add_edit_note/0")
                    closeDrawer()
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // botom selection
            Column(
                modifier = Modifier.padding(top = 2.dp).weight(2f)
            ) {
                DrawerItem(
                    icon = Icons.Default.Help,
                    text = "Help & Feedback",
                    selected = navController.currentDestination?.route == Screen.HelpFeedback.route
                ) {
                    navController.navigate(Screen.HelpFeedback.route)
                    closeDrawer()
                }

                DrawerItem(
                    icon = Icons.Default.Info,
                    text = "About",
                    selected = navController.currentDestination?.route == Screen.About.route
                ) {
                    navController.navigate(Screen.About.route)
                    closeDrawer()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItem(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(26.dp)
            )
        },
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unselectedContainerColor = MaterialTheme.colorScheme.background,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    )
}
