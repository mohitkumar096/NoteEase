package com.mohit.noteease.ui.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mohit.noteease.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.mohit.noteease.data.local.strings.Strings
import com.mohit.noteease.data.local.strings.Strings.Companion.DeveloperEmail
import com.mohit.noteease.data.local.strings.Strings.Companion.DeveloperLinkedIn
import com.mohit.noteease.data.local.strings.Strings.Companion.DeveloperName
import com.mohit.noteease.data.local.strings.Strings.Companion.DeveloperWhatsApp
import com.mohit.noteease.data.local.strings.Strings.Companion.aboutApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current
    // colors
    val isDarkMode = isSystemInDarkTheme()
    val window = (context as? Activity)?.window
    WindowCompat.setDecorFitsSystemWindows(window!!, true)
    val solidColor = if (isDarkMode) Color.Black.toArgb() else Color.White.toArgb()
    window?.statusBarColor = solidColor
    window?.navigationBarColor = solidColor

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "About NoteEase",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Make content scrollable
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // app logo
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        color = Color(0x00FFFFFF),
                        shape = RectangleShape
                    ).clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = com.mohit.noteease.R.drawable.notes_icon),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(140.dp)
                )
            }

            // app name and version
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = Strings.appName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = Strings.appVersion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // feature
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Key Features",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                FeatureItem("🎨 Color-coded notes for better organization")
                FeatureItem("📌 Pin important notes for quick access")
                FeatureItem("🔍 Powerful search functionality")
                FeatureItem("📱 Clean, intuitive interface")
            }

            // description
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "About The App",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = aboutApp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // developer card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).align(alignment = Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Developed By",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            ).align(alignment = Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "MK",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = DeveloperName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Android Developer",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // social
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        // email
                        IconButton(
                            onClick = {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$DeveloperEmail")
                                    putExtra(Intent.EXTRA_SUBJECT, "NoteEase App Feedback")
                                }
                                try {
                                    context.startActivity(emailIntent)
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Developer",
                                modifier = Modifier.size(28.dp),
                                tint = Color(0xFFFF4433)
                            )
                        }

                        // LinkedIn
                        IconButton(
                            onClick = {
                                val linkedInIntent = try {
                                    Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://www.$DeveloperLinkedIn")
                                        `package` = "com.linkedin.android"
                                    }
                                } catch (e: Exception) {
                                    Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://www.$DeveloperLinkedIn")
                                    }
                                }
                                try {
                                    context.startActivity(linkedInIntent)
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(context, "Couldn't open LinkedIn", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_linkedin),
                                contentDescription = "LinkedIn Profile",
                                modifier = Modifier.size(28.dp),
                                tint = Color(0xFF1122FF)
                            )
                        }

                        // WhatsApp

                        // GitHub
                        IconButton(
                            onClick = {
                                val githubIntent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://github.com/mohitkumar096")
                                }
                                context.startActivity(githubIntent)
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_github),
                                contentDescription = "GitHub Profile",
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer
            Text(
                text = Strings.CopyRight,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}