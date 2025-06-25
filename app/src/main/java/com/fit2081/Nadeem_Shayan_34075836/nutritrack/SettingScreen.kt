package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.RegisterAndLoginScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.SettingScreenViewModel

@Composable
@Suppress("DEPRECATION")
fun SettingScreen(modifier: Modifier = Modifier, viewModel: SettingScreenViewModel,
    registerAndLoginScreenViewModel: RegisterAndLoginScreenViewModel,
                  navController: NavController) {
    val context = LocalContext.current
    val loggedInUser = AuthManager.getPatientId()

    val scrollState = rememberScrollState()

    //fetch current user from shared preference, and mark it as key
    val name by viewModel.name.observeAsState("")
    val phoneNumber by viewModel.phoneNumber.observeAsState(0)
    val userId by viewModel.userid.observeAsState(0)

    //handle dark mode switch from viewmodel to save across app
    val isDarkMode by viewModel.isDarkMode.observeAsState(false)

    if (userId != null) {
        viewModel.loadSettingsInfo(loggedInUser)
    }

    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Settings:", fontFamily = sfProFont,
                color = Color.Gray,
                fontSize = 21.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Account and other settings", fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 23.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                thickness = 1.2.dp,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Account:", fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Username",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Username",
                        fontSize = 17.sp,
                        fontFamily = sfProFont,
                    )
                }
                Text(
                    text = name.camelCaseToWords(),
                    fontSize = 17.sp,
                    fontFamily = sfProFont,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Call,
                        contentDescription = "Phone Number",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Phone number",
                        fontSize = 17.sp,
                        fontFamily = sfProFont
                    )
                }

                Text(
                    text = "$phoneNumber",
                    fontSize = 17.sp,
                    fontFamily = sfProFont,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "User ID",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "User ID",
                        fontSize = 17.sp,
                        fontFamily = sfProFont
                    )
                }
                Text(
                    text = "$userId",
                    fontSize = 17.sp,
                    fontFamily = sfProFont,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                thickness = 1.2.dp,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Other settings:", fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.align(Alignment.Start)
            ) {
                //logic for logging out of app (session remove, data remains saved)
                TextButton(onClick = {registerAndLoginScreenViewModel.logout()
                    val intent = Intent(context, LoginScreen::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_LONG).show()
                    context.startActivity(intent)
                    (context as Activity).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                })
                {
                    Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null)
                    Spacer(Modifier.padding(10.dp))
                    Text("Logout", fontSize = 17.sp, fontFamily = sfProFont, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.align(Alignment.Start)
            ) {
                TextButton(onClick = {val intent = Intent(context, ClinicianLogin::class.java)
                context.startActivity(intent)}) {
                    Icon(Icons.Outlined.Face, contentDescription = null)
                    Spacer(Modifier.padding(10.dp))
                    Text("Clinician Login", fontSize = 17.sp, fontFamily = sfProFont, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                thickness = 1.2.dp,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Theme settings:", fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            //logic to handle dark mode/light mode switch
            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDarkMode) {
                    Spacer(Modifier.padding(6.dp))
                    Icon(Icons.Default.DarkMode, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                else
                {
                    Spacer(Modifier.padding(6.dp))
                    Icon(Icons.Default.LightMode, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.padding(10.dp))
                Text(text = if (isDarkMode) "Dark Mode" else "Light Mode", fontFamily = sfProFont, fontSize = 17.sp)
                Spacer(modifier = Modifier.weight(0.5f))
                Switch(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .scale(0.8f),
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) }
                )
            }
        }
    }
}