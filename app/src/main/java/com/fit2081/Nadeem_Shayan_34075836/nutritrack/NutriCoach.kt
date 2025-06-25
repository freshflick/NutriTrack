package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.GenAIViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.NutriCoachViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.NutriCoachTip
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.NutriCoachTip.UiState

@Composable
fun NutriCoachScreen(modifier: Modifier = Modifier, nutriCoachViewModel: NutriCoachViewModel,
                     genAiViewModel: GenAIViewModel,
                     navController: NavController) {
    val loggedInUser = AuthManager.getPatientId()
    val scrollState = rememberScrollState()

    // FruityVice API variables ----------------------------------
    val name by nutriCoachViewModel.fruitName.observeAsState("")
    val family by nutriCoachViewModel.family.observeAsState("")
    val calories by nutriCoachViewModel.calories.observeAsState(0.0)
    val fat by nutriCoachViewModel.fat.observeAsState(0.0)
    val sugar by nutriCoachViewModel.sugar.observeAsState(0.0)
    val carbohydrate by nutriCoachViewModel.carbohydrate.observeAsState(0.0)
    val protein by nutriCoachViewModel.protein.observeAsState(0.0)

    var fruitSearch by rememberSaveable { mutableStateOf("") }

    // GenAi variables -------------------------------------------
    val tips by genAiViewModel.listOfTips.collectAsState()
    val uiState by genAiViewModel.uiState.observeAsState(UiState.Initial)

    val optimalFruitScoring by genAiViewModel.optimalFruitScore.observeAsState()

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
                text = "Your Personal AI Food Assistant", fontFamily = sfProFont,
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
            //condition to display fruit info or not for optimal scores
            if (loggedInUser != null) {
                LaunchedEffect(Unit) {
                    genAiViewModel.getOptimalFruitScore(loggedInUser)
                }
            }

            //fetch from viewmodel and display text according to result
            //if true then user doesn't need to access fruityvice for fruit info
            if (optimalFruitScoring == true){
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "You meet the optimal fruit score! Congrats!", fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                //display random image from url if fruit score optimal
                Image(
                    painter = rememberAsyncImagePainter("https://picsum.photos/200/300"),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.height(30.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    thickness = 1.2.dp,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Need a Boost?", fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A little boost of motivation and helpful tips to keep you going strong!",
                    fontFamily = sfProFont,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.padding(3.dp).width(375.dp)
                        .wrapContentHeight()
                        .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceDim
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (uiState) {
                            is UiState.Loading -> {
                                CircularProgressIndicator()
                            }
                            is UiState.Success -> {
                                val outputText = (uiState as UiState.Success).outputText
                                genAiViewModel.storeTip(outputText, loggedInUser)
                                Text(
                                    text = outputText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = sfProFont
                                )
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
                                    Text(text = "Press the button to generate a tip", fontFamily = sfProFont)
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Start)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            //send prompt to viewmodel to genAI
                            onClick = { genAiViewModel.sendPrompt() },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .width(275.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Motivational Message (AI)",
                                fontSize = 14.sp,
                                fontFamily = sfProFont,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.align(Alignment.Start)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    var showDialog by remember { mutableStateOf(false) }

                    if (showDialog) {
                        ShowTipsModal(
                            onDismiss = { showDialog = false },
                            label = "AI Tips",
                            // list of tips here
                            tips = tips,
                            viewModel = genAiViewModel
                        )
                    }
                    Button(
                        onClick = {
                            genAiViewModel.loadTips(loggedInUser)
                            showDialog = true
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(200.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Show All Tips",
                            fontSize = 14.sp,
                            fontFamily = sfProFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            //if score not optimal user needs to access fruityvice for fruit info
            else
            {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Fruit Name:", fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 14.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = fruitSearch,
                        onValueChange = { fruitSearch = it },
                        label = { Text("Enter a fruit", fontFamily = sfProFont) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .width(260.dp)
                            .height(70.dp)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFB1D18A),
                            unfocusedBorderColor = Color(0xFFC5C8BA)
                        )
                    )
                    Button(
                        onClick = {
                            if (loggedInUser != null && fruitSearch.isNotBlank()) {
                                nutriCoachViewModel.getFruitInfo(fruitSearch)
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(150.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            "Details",
                            fontSize = 14.sp,
                            fontFamily = sfProFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                        Spacer(modifier = Modifier.width(8.dp))
                        ElevatedCard(
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                            modifier = Modifier.padding(3.dp).fillMaxWidth()
                                .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceDim
                            )
                        ) {
                            NutriCoachTextField("Fruit: ", "$name")
                            NutriCoachTextField("Family: ", "$family")
                            NutriCoachTextField("Calories: ", "$calories")
                            NutriCoachTextField("Fat: ", "$fat")
                            NutriCoachTextField("Sugar: ", "$sugar")
                            NutriCoachTextField("Carbohydrates: ", "$carbohydrate")
                            NutriCoachTextField("Protein: ", "$protein")
                        }
                    }
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
                    text = "Need a Boost?", fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A little boost of motivation and helpful tips to keep you going strong!",
                    fontFamily = sfProFont,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.padding(3.dp).width(375.dp)
                        .wrapContentHeight()
                        .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceDim
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (uiState) {
                            is UiState.Loading -> {
                                CircularProgressIndicator()
                            }
                            is UiState.Success -> {
                                val outputText = (uiState as UiState.Success).outputText
                                genAiViewModel.storeTip(outputText, loggedInUser)
                                Text(
                                    text = outputText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = sfProFont
                                )
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
                                    Text(text = "Press the button to generate a tip", fontFamily = sfProFont)
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Start)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { genAiViewModel.sendPrompt() },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .width(275.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Motivational Message (AI)",
                                fontSize = 14.sp,
                                fontFamily = sfProFont,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.align(Alignment.Start)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    var showDialog by remember { mutableStateOf(false) }

                    if (showDialog) {
                        ShowTipsModal(
                            onDismiss = { showDialog = false },
                            label = "AI Tips",
                            // list of tips here
                            tips = tips,
                            viewModel = genAiViewModel
                        )
                    }
                    Button(
                        onClick = {
                            genAiViewModel.loadTips(loggedInUser)
                            showDialog = true
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(200.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Show All Tips",
                            fontSize = 14.sp,
                            fontFamily = sfProFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NutriCoachTextField(label: String, value: String){
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontFamily = sfProFont,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontFamily = sfProFont,
            fontWeight = FontWeight.Normal
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        thickness = 1.dp,
        color = Color.White
    )
}

//custom modal with clear all tips functionality and dismiss
@Composable
fun ShowTipsModal(onDismiss: () -> Unit, label: String, tips: List<NutriCoachTip>, viewModel: GenAIViewModel) {
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    val loggedInUser = AuthManager.getPatientId()
    val context = LocalContext.current
    val deleteStatus by viewModel.deleteTipStatus.observeAsState()

    LaunchedEffect(deleteStatus) {
        if (deleteStatus == "Tips have been deleted successfully"){
            Toast.makeText(context, deleteStatus, Toast.LENGTH_LONG).show()
            viewModel.resetDeleteStatus()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(10.dp),
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    label,
                    fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    thickness = 1.5.dp,
                    color = Color.White
                )
            }
        },
        text = {
            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(3.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 13.dp))
                {
                    Text(text = "Disclaimer: \n If this section is empty, try generating some tips which will then show up here. \n\n" +
                            "Use the clear tips button to clean up the section if it gets too cluttered.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        fontFamily = sfProFont,
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(7.dp))
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        thickness = 1.5.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(tips) { tip ->
                            Text(
                                text = tip.tipText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                fontFamily = sfProFont,
                                fontSize = 15.sp,
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Start
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                thickness = 1.5.dp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(240.dp)
                ) {
                    Text(
                        text = "Dismiss",
                        fontFamily = sfProFont,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { viewModel.deleteAllTipsByPatientId(loggedInUser)
                              viewModel.loadTips(loggedInUser) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        text = "Clear All Tips",
                        fontFamily = sfProFont,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}