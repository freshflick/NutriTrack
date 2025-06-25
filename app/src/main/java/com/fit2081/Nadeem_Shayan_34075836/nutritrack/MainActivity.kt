package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.MainActivityViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.RegisterAndLoginScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.SettingScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.MainActivityViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.RegisterAndLoginScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.SettingScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ui.theme.NutriTrackTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var registerAndLoginScreenViewModel: RegisterAndLoginScreenViewModel
    private lateinit var settingScreenViewModel: SettingScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PatientRepo(applicationContext)
        val dataStoreRepo = DataStoreRepo(applicationContext)

        val factory = MainActivityViewModelFactory(application, repository)
        val settingFactory = SettingScreenViewModelFactory(repository, dataStoreRepo)
        val registerFactory = RegisterAndLoginScreenViewModelFactory(repository, dataStoreRepo)

        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        settingScreenViewModel = ViewModelProvider(this, settingFactory)[SettingScreenViewModel::class.java]
        registerAndLoginScreenViewModel = ViewModelProvider(this, registerFactory)[RegisterAndLoginScreenViewModel::class.java]

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isInitialized = sharedPreferences.getBoolean("isInitialized", false)

        if (!isInitialized) {
            sharedPreferences.edit() { putBoolean("isInitialized", true) }
            viewModel.importCSVIfFirstLaunch()
        }
        setContent {
            val isDarkMode by settingScreenViewModel.isDarkMode.observeAsState(false)
            NutriTrackTheme(darkTheme = isDarkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomePage(modifier = Modifier.padding(innerPadding),
                        registerAndLoginScreenViewModel)
            }
        }
    }
}

@Composable
@Suppress("DEPRECATION")
fun WelcomePage(modifier: Modifier = Modifier, loginViewModel: RegisterAndLoginScreenViewModel) {
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )

    val isLoggedInState = loginViewModel.checkLoggedIn().collectAsState(initial = null)
    val isLoggedIn = isLoggedInState.value

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(65.dp))
            Image(
                painter = painterResource(id = R.drawable.health_logo),
                contentDescription = "HealthAppLogo",
                modifier = Modifier.size(170.dp)
                    .clip(RoundedCornerShape(14.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "VitaCoach",
                fontSize = 43.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = sfProFont
            )
            Spacer(modifier = Modifier.height(20.dp))
            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(5.dp).fillMaxWidth()
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        "Disclaimer: ",
                        fontSize = 20.sp,
                        fontFamily = sfProFont,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        thickness = 1.dp,
                        color = Color.White
                    )
                    Text(
                        "This app provides general health and nutrition information for " +
                                "educational purposes only. It is not intended as medical advice, diagnosis, or treatment. " +
                                "Always consult a qualified healthcare " +
                                "professional before making any changes to your diet, exercise, or health regimen. " +
                                "Use this app at your own risk. " +
                                "If you’d like to an Accredited Practicing Dietitian (APD), please " +
                                "visit the Monash Nutrition/Dietetics Clinic (discounted rates for " +
                                "students): ",
                        fontSize = 13.5.sp,
                        fontFamily = sfProFont,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) { // For inserting a hyperlink
                        Text(
                            text = "Click here for more info",
                            fontFamily = sfProFont,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                val url =
                                    "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition"
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        loginViewModel.persistLogin()
                    }
                    if (isLoggedIn == true) {
                        val intent = Intent(context, FoodQuestionnaire::class.java)
                        context.startActivity(intent)
                    } else {
                        val intent = Intent(context, LoginScreen::class.java)
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    "Login",
                    fontSize = 16.5.sp,
                    fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            MainBottomBar()
            }
        }
    }
}

@Composable
fun MainBottomBar(modifier: Modifier = Modifier) {
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    Surface(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = Color(0xFF1E88E5),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomAppBar(
                modifier = Modifier.height(80.dp),
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Designed by Shayan Nadeem Malik 🐉",
                            textAlign = TextAlign.Center,
                            fontFamily = sfProFont
                        )
                    }
                }
            )
        }
    }
}