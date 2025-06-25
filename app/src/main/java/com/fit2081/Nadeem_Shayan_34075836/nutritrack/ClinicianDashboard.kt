package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.GenAIViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.SettingScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.GenAIViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.SettingScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.UiState
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.FoodIntakeRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.NutriCoachRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ui.theme.NutriTrackTheme

class ClinicianDashboard: ComponentActivity() {
    private lateinit var genAIViewModel: GenAIViewModel
    private lateinit var settingsViewModel: SettingScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = NutriCoachRepo(applicationContext)
        val foodIntakeRepo = FoodIntakeRepo(applicationContext)
        val patientRepo = PatientRepo(applicationContext)

        val dataStoreRepo = DataStoreRepo(applicationContext)

        val genAIFactory = GenAIViewModelFactory(repository, foodIntakeRepo, patientRepo)
        val settingFactory = SettingScreenViewModelFactory(patientRepo, dataStoreRepo)

        genAIViewModel = ViewModelProvider(this, genAIFactory)[GenAIViewModel::class.java]
        settingsViewModel = ViewModelProvider(this, settingFactory)[SettingScreenViewModel::class.java]

        setContent {
            val isDarkMode by settingsViewModel.isDarkMode.observeAsState(false)
            NutriTrackTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    ClinicianDashboard(modifier = Modifier.padding(innerPadding),
                        genAiViewModel = genAIViewModel)
                }
            }
        }
    }
}

@Composable
fun ClinicianDashboard(modifier: Modifier = Modifier, genAiViewModel: GenAIViewModel) {
    val loggedInUser = AuthManager.getPatientId()
    val scrollState = rememberScrollState()

    val averageHEIFAMale by genAiViewModel.maleAverageScore.observeAsState(0f)
    val averageHEIFAFemale by genAiViewModel.femaleAverageScore.observeAsState(0f)

    val context = LocalContext.current

    //send instruction to viewmodel for analysis only when clicked, to improve efficiency of app and avoid clutter
    val dataPatternAnalysis = rememberSaveable { mutableStateOf(false) }
    val deeperDataPatternAnalysis = rememberSaveable { mutableStateOf(false) }

    val intent = Intent(context, MainActivityScreen::class.java)

    //fetch the average heifa scores
    LaunchedEffect(Unit) {
        genAiViewModel.getAverageHEIFAScore()
    }

    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    Surface(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState),
        color = MaterialTheme.colorScheme.background
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "NutriCoach:", fontFamily = sfProFont,
                color = Color.Gray,
                fontSize = 21.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Clinician Dashboard", fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
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
                text = "Average HEIFA Scores:", fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(8.dp))
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.padding(3.dp).fillMaxWidth()
                            .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceDim
                        )
                    ) {
                        NutriCoachTextField("Average HEIFA (Male) ", "$averageHEIFAMale")
                        NutriCoachTextField("Average HEIFA (Female): ", "$averageHEIFAFemale")
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                thickness = 1.2.dp,
            )
            Spacer(modifier = Modifier.height(18.dp))

            //logic for sending prompt through viewmodel for data analysis
            Button(
                onClick = { genAiViewModel.sendPromptForPattern()
                          dataPatternAnalysis.value = true
                          deeperDataPatternAnalysis.value = false},
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(350.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    "Data Pattern Analysis",
                    fontSize = 14.sp,
                    fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold
                )
            }

            //logic for sending prompt through viewmodel for further data analysis
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { genAiViewModel.sendPromptForExtraPattern()
                    deeperDataPatternAnalysis.value = true
                    dataPatternAnalysis.value = false},
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(350.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    "Deeper Data Pattern Analysis",
                    fontSize = 14.sp,
                    fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.padding(horizontal = 13.dp))
            {
                Text(
                    text = "Disclaimer: \n Use the button to generate data patterns for patients",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    fontFamily = sfProFont,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(7.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    thickness = 1.5.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                //switches boolean so only one analysis is displayed at a time, avoiding clutter
                if (dataPatternAnalysis.value == true) {
                    DataPattern(genAiViewModel)
                }
                if (deeperDataPatternAnalysis.value == true) {
                    DeeperDataPattern(genAiViewModel)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    intent.putExtra("startDestination", "settings")
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    "Back to Settings",
                    fontSize = 14.sp,
                    fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


//logic for handling AI response
@Composable
fun DataPattern(genAiViewModel: GenAIViewModel) {
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    val uiState by genAiViewModel.uiStateForData.observeAsState(UiState.Initial)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.Success -> {
                val outputText = (uiState as UiState.Success).outputText
                val points = outputText.lines().filter { it.isNotBlank() }
                points.forEach { point ->
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.padding(3.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceDim
                        )
                    ) {
                        Text(
                            text = point,
                            fontSize = 15.sp,
                            fontFamily = sfProFont,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            is UiState.Error -> {
                Text(
                    text = "Error: ${(uiState as UiState.Error).errorMessage}",
                    color = Color.Red,
                    fontFamily = sfProFont
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Data patterns will appear here", fontFamily = sfProFont)
                }
            }
        }
    }
}

//logic for handling further analysis AI response
@Composable
fun DeeperDataPattern(genAiViewModel: GenAIViewModel) {
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    val uiState by genAiViewModel.uiStateExtraData.observeAsState(UiState.Initial)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.Success -> {
                val outputText = (uiState as UiState.Success).outputText
                val points = outputText.lines().filter { it.isNotBlank() }
                points.forEach { point ->
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.padding(3.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceDim
                        )
                    ) {
                        Text(
                            text = point,
                            fontSize = 15.sp,
                            fontFamily = sfProFont,
                            modifier = Modifier.padding(10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
            is UiState.Error -> {
                Text(
                    text = "Error: ${(uiState as UiState.Error).errorMessage}",
                    color = Color.Red,
                    fontFamily = sfProFont
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Data patterns will appear here", fontFamily = sfProFont)
                }
            }
        }
    }
}