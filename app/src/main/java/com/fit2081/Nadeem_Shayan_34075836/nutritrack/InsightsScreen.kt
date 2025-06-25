package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.InsightViewModel
import java.util.Locale


@Composable
fun InsightsScreen(modifier: Modifier = Modifier, insightViewModel: InsightViewModel, navController: NavController) {
    val loggedInUser = AuthManager.getPatientId()
    val isLoading by insightViewModel.isLoading.observeAsState(true)

    //check if user logged in and load in insight data from database
    if (loggedInUser != null) {
        LaunchedEffect(loggedInUser) {
            Log.d("InsightDebug", "Calling viewModel functions")
            insightViewModel.addInsight(loggedInUser)
        }
    }

    //if it takes time, show a loading effect
    LaunchedEffect(isLoading) {
        if (!isLoading && loggedInUser != null){
            insightViewModel.loadInsightScores(loggedInUser)
        }
    }
    InsightContent(modifier = modifier, insightViewModel = insightViewModel, navController = navController)
}


@Composable
fun InsightContent(modifier: Modifier, insightViewModel: InsightViewModel, navController: NavController) {
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
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Insights:", fontFamily = sfProFont,
                color = Color.Gray,
                fontSize = 21.sp,
                modifier = Modifier.align(Alignment.Start)
                    .padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Food Score", fontFamily = sfProFont,
                fontWeight = FontWeight.Bold,
                fontSize = 23.sp,
                modifier = Modifier.align(Alignment.Start)
                    .padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            foodSlider(viewModel = insightViewModel, navController = navController)
        }
    }
}


@SuppressLint("ComposableNaming") //ignore naming suggestion
@Composable
fun foodSlider(modifier: Modifier = Modifier, viewModel: InsightViewModel, navController: NavController) {
    val foodItems = listOf(
        "Vegetables", "Fruits", "Grains & Cereals", "Whole Grains",
        "Meat & Alternatives", "Dairy", "Water", "Unsaturated Fats", "Saturated Fats",
        "Sodium", "Sugar", "Alcohol", "Discretionary Foods", "Total Food Quality Score"
    )

    // Collect scores into a map of label -> score as Compose state
    val sliderValues = remember(foodItems) {
        mutableStateMapOf<String, Double>().apply {
            foodItems.forEach { put(it, 0.0) }
        }
    }

    // Observe LiveData values and update sliderValues accordingly
    foodItems.forEach { item ->
        val scoreLiveData = viewModel.insightScoreMap[item]
        val score by scoreLiveData?.observeAsState(0.0) ?: remember { mutableDoubleStateOf(0.0) }
        // Update sliderValues map on score change
        Log.d("Checking score", "$score")
        LaunchedEffect(score) {
            sliderValues[item] = score as Double
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            items(foodItems) { item ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    foodProgressionWithLabel(
                        label = item,
                        value = sliderValues[item] ?: 0.0,
                        navController = navController
                    )
                }
            }
        }
    }
}



@SuppressLint("ComposableNaming") //to ignore naming suggestion
@Composable
fun foodProgressionWithLabel(label: String, value: Double, navController: NavController){
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    val totalScoreMax = 100
    val categoryScoreMax = 10
    val lowerMaxScore = 5

    val context = LocalContext.current

    val roundedValue = "%.2f".format(value).toFloat()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        //to separate scoring and make it more noticeable
        if (value > categoryScoreMax) {
            Spacer(modifier = Modifier.height(275.dp))
            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 18.sp,
                        fontFamily = sfProFont,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { (value / totalScoreMax).toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$roundedValue/100",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp),
                        textAlign = TextAlign.End,
                        fontFamily = sfProFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        thickness = 1.5.dp,
                    )
                    //share score button
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 50.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            val shareText = "Hey! I just got a HEIFA score of ${roundToTwoDecimalPlaces(value)}"
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share via"))
                        }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Share with someone", fontFamily = sfProFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    //improve diet button
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 50.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { navController.navigate("nutriCoach") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        } }
                    ) {
                        Icon(
                            painterResource(R.drawable.ai_icon),
                            contentDescription = "Share",
                            modifier = Modifier.size(25.dp)
                                .padding(horizontal = 2.dp)
                        )
                        Text("Improve your diet!", fontFamily = sfProFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(150.dp))

            //specific categories which have a max score of 5.0
        } else if (label in listOf("Grains & Cereals", "Whole Grains", "Water", "Alcohol", "Saturated Fats", "Unsaturated Fats")){
            Text(
                text = label,
                fontSize = 15.sp,
                fontFamily = sfProFont,
                modifier = Modifier.weight(1.2f)
            )
            LinearProgressIndicator(
                progress = { (value / lowerMaxScore.toFloat()).toFloat() },
                modifier = Modifier.weight(1.6f).height(10.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Text(
                text = "$roundedValue/5",
                modifier = Modifier.padding(6.dp)
                    .width(70.dp),
                textAlign = TextAlign.End,
                fontFamily = sfProFont
            )

        //all other categories with max score of 10.0
        } else {
            Text(
                text = label,
                fontSize = 15.sp,
                fontFamily = sfProFont,
                modifier = Modifier.weight(1.2f)
            )
            LinearProgressIndicator(
                progress = { (value / categoryScoreMax.toFloat()).toFloat() },
                modifier = Modifier.weight(1.6f).height(10.dp)
                    .clip(RoundedCornerShape(3.dp))
            )
            Text(
                text = "$roundedValue/10",
                modifier = Modifier.padding(6.dp)
                    .width(70.dp),
                textAlign = TextAlign.End,
                fontFamily = sfProFont
            )
        }
    }
}

//custom function to round values to 2dp
fun roundToTwoDecimalPlaces(value: Double): Double {
    return String.format(Locale.US, "%.2f", value).toDouble()
}