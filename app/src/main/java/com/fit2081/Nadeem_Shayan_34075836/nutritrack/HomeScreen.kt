package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.GenAIViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.HomeScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.InsightViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.NutriCoachViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.RegisterAndLoginScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.SettingScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.GenAIViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.HomeScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.InsightScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.NutriCoachViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.RegisterAndLoginScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.SettingScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.FoodIntakeRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.InsightsRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.NutriCoachRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ui.theme.NutriTrackTheme
import java.util.Locale

class MainActivityScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //initialize all viewmodels, factories and repos to use for navhost
        val startDestination = intent.getStringExtra("startDestination") ?: "home"

        val insightsRepo = InsightsRepo(applicationContext)
        val patientRepo = PatientRepo(applicationContext)
        val dataStoreRepo = DataStoreRepo(applicationContext)
        val nutriCoachRepository = NutriCoachRepo(applicationContext)
        val foodIntakeRepo = FoodIntakeRepo(applicationContext)

        val homeFactory = HomeScreenViewModelFactory(patientRepo)
        val insightsFactory = InsightScreenViewModelFactory(insightsRepo, patientRepo)
        val settingFactory = SettingScreenViewModelFactory(patientRepo, dataStoreRepo)
        val nutriCoachFactory = NutriCoachViewModelFactory(nutriCoachRepository)
        val genAIFactory = GenAIViewModelFactory(nutriCoachRepository, foodIntakeRepo, patientRepo)
        val loginFactory = RegisterAndLoginScreenViewModelFactory(patientRepo, dataStoreRepo)

        val insightsViewModel = ViewModelProvider(this, insightsFactory)[InsightViewModel::class.java]
        val settingViewModel = ViewModelProvider(this, settingFactory)[SettingScreenViewModel::class.java]
        val homeScreenViewModel = ViewModelProvider(this, homeFactory)[HomeScreenViewModel::class.java]
        val nutriCoachViewModel = ViewModelProvider(this, nutriCoachFactory)[NutriCoachViewModel::class.java]
        val genAIViewModel = ViewModelProvider(this, genAIFactory)[GenAIViewModel::class.java]
        val registerAndLoginScreenViewModel = ViewModelProvider(this, loginFactory)[RegisterAndLoginScreenViewModel::class.java]

        setContent {
            val isDarkMode by settingViewModel.isDarkMode.observeAsState(false)

            //use navhost for smoother navigation
            NutriTrackTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                val navBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStack?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { MyBottomBar(navController, currentRoute) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(viewModel = homeScreenViewModel, navController = navController)
                        }
                        composable("insights") {
                            InsightsScreen(insightViewModel = insightsViewModel, navController = navController)
                        }
                        composable("nutriCoach") {
                            NutriCoachScreen(nutriCoachViewModel = nutriCoachViewModel,
                                genAiViewModel = genAIViewModel,
                                navController = navController)
                        }
                        composable("settings") {
                            SettingScreen(
                                viewModel = settingViewModel,
                                registerAndLoginScreenViewModel = registerAndLoginScreenViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MyBottomBar(
    navController: NavController,
    currentRoute: String?,
    modifier: Modifier = Modifier) {
    val context = LocalContext.current
    BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 35.dp)
            .clip(RoundedCornerShape(15.dp))
            .shadow(8.dp, shape = RoundedCornerShape(15.dp))
            .height(85.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp,
        content = {
            IconGrid(navController, currentRoute)
        }
    )
}

//bottom bar creation
@Composable
fun IconGrid(navController: NavController, currentRoute: String?) {
    val context = LocalContext.current

    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )

    //store icon, text to display, destination address and intent to start the destination address activity
    val iconActions = listOf(
        Triple(R.drawable.home_icon, "Home", "home"),
        Triple(R.drawable.insights_icon, "Insights", "insights"),
        Triple(R.drawable.ai_icon, "NutriCoach", "nutriCoach"),
        Triple(R.drawable.settings_icon, "Settings", "settings")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 23.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        iconActions.forEach{ (icon, label, route) ->
            val isSelected = currentRoute == route
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    if(!isSelected) navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(38.dp)
                        .padding(top = 15.dp),
                    tint = if (isSelected) Color(0xFF1A3208) else Color.Gray
                )
                Text(
                    text = label,
                    fontSize = 13.5.sp,
                    fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFF1A3208) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeScreenViewModel,  navController: NavController) {
    val context = LocalContext.current

    //fetch logged in userid
    val loggedInUser = AuthManager.getPatientId()

    val scrollState = rememberScrollState()
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )

    val name by viewModel.name.observeAsState("Null")
    val heifaScore by viewModel.heifaTotalScore.observeAsState(0.0)

    if (loggedInUser != null) {
        viewModel.loadHomeScreenInfo(loggedInUser)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome,", fontFamily = sfProFont,
                color = Color.Gray,
                fontSize = 21.sp,
                modifier = Modifier.align(Alignment.Start)
                    .padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "$name".camelCaseToWords(), fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 23.sp,
                modifier = Modifier.align(Alignment.Start)
                    .padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(13.dp))
            Row (
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = "You've already filled in your Food Intake \n" +
                            "Questionnaire but you can change details here: ",
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(horizontal = 15.dp),
                    fontFamily = sfProFont
                )
                Button(
                    onClick = { val intent = Intent(context, FoodQuestionnaire::class.java)
                        context.startActivity(intent) },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Edit", fontSize = 14.sp, fontFamily = sfProFont, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            Image(
                painter = painterResource(id = R.drawable.food_image),
                contentDescription = "HealthAppLogo",
                modifier = Modifier.size(250.dp)
                    .clip(RoundedCornerShape(14.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = "Your Score: ",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 13.dp)
                        .weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = sfProFont
                )
                //make sure that it navigates to insight by putting it on top of stack
                TextButton(onClick = { navController.navigate("insights") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                } }) {
                    Text(
                        text = "See all scores  >",
                        fontSize = 14.sp,
                        fontFamily = sfProFont,
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.align(Alignment.Start)
            ) {
                Spacer(modifier = Modifier.width(20.dp))

                Icon(painterResource(R.drawable.up_icon), contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.height(20.dp))

                Text("Your Food Quality score",
                    fontSize = 16.sp, fontFamily = sfProFont,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 6.dp))

                Spacer(modifier = Modifier.width(30.dp))

                Text(
                    text = "${roundToTwoDecimalPlacesHome(heifaScore)} / 100",
                    fontSize = 15.5.sp, fontFamily = sfProFont, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 7.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp),
                thickness = 1.dp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(10.dp).fillMaxWidth()
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                )
            ) {
                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "What is the Food Quality Score?",
                    fontSize = 16.sp, fontFamily = sfProFont,
                    modifier = Modifier.align(Alignment.Start)
                        .padding(horizontal = 15.dp),
                    fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Your Food Quality Score provides a snapshot of how well your " +
                        "eating patterns align with established food guidelines, helping " +
                        "you identify both strengths and opportunities for improvement " +
                        "in your diet. \n" +
                        "\n" +
                        "This personalized measurement considers various food groups " +
                        "including vegetables, fruits, whole grains, and proteins to give " +
                        "you practical insights for making healthier food choices.",
                    fontSize = 14.sp, fontFamily = sfProFont,
                    modifier = Modifier.align(Alignment.Start)
                        .padding(horizontal = 15.dp))

                Spacer(modifier = Modifier.height(14.dp))
            }
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

//camel casing for names
fun String.camelCaseToWords(): String {
    return replace(Regex("([a-z])([A-Z])"), "$1 $2")
        .replaceFirstChar { it.uppercase() }
}

//custom function to round up values to 2dp
fun roundToTwoDecimalPlacesHome(value: Double?): Double {
    return String.format(Locale.US, "%.2f", value).toDouble()
}