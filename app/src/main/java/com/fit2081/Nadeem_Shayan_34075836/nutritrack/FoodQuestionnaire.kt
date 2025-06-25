package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.FoodQuestionnaireViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.RegisterAndLoginScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.SettingScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.FoodQuestionnaireViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.RegisterAndLoginScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.SettingScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.FoodIntakeRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ui.theme.NutriTrackTheme
import java.util.Calendar


class FoodQuestionnaire : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val patientRepository = PatientRepo(applicationContext)
        val repository = FoodIntakeRepo(applicationContext)
        val dataStoreRepo = DataStoreRepo(applicationContext)

        setContent {
            val factory = FoodQuestionnaireViewModelFactory(repository)
            val registerAndLoginFactory = RegisterAndLoginScreenViewModelFactory(patientRepository, dataStoreRepo)
            val settingFactory = SettingScreenViewModelFactory(patientRepository, dataStoreRepo)

            val viewModel: FoodQuestionnaireViewModel = viewModel(factory = factory)
            val settingViewModel: SettingScreenViewModel = viewModel(factory = settingFactory)
            val registerAndLoginScreenViewModel: RegisterAndLoginScreenViewModel = viewModel(factory = registerAndLoginFactory)

            val isDarkMode by settingViewModel.isDarkMode.observeAsState(false)
            NutriTrackTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(registerAndLoginScreenViewModel) }
                ) { innerPadding ->
                    Questionnaire(modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        registerAndLoginScreenViewModel = registerAndLoginScreenViewModel)
                }
            }
        }
    }

    @Composable
    fun ShowFloatingCard(onDismiss: () -> Unit, label: String, detail: String, personaImage: Int) {
        val sfProFont = FontFamily(
            Font(R.font.sf_pro_display_regular, FontWeight.Normal),
            Font(R.font.sf_pro_display_bold, FontWeight.Bold)
        )
        AlertDialog(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(10.dp),
            properties = DialogProperties(dismissOnClickOutside = false),
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Image(
                        painter = painterResource(id = personaImage),
                        contentDescription = "HealthAppLogo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = detail,
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = sfProFont,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
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
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(
                            text = "Dismiss",
                            fontFamily = sfProFont,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun FloatingCard() {
        val personaLabels = listOf(
            "Health Devotee", "Mindful Eater", "Wellness Striver",
            "Balance Seeker", "Health Procrastinator", "Food Carefree"
        )

        val personaDescriptions = listOf(
            "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. " +
                    "I may even buy superfoods or follow a particular type of diet. " +
                    "I like to think I am super healthy.",

            "I’m health-conscious and being healthy and eating healthy is important to me. " +
                    "Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. " +
                    "I look for new recipes and healthy eating information on social media.",

            "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. " +
                    "Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.",

            "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. " +
                    "I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",

            "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. " +
                    "I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",

            "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat."
        )

        val personaImages = listOf(
            R.drawable.persona_1, R.drawable.persona_2,
            R.drawable.persona_3, R.drawable.persona_4,
            R.drawable.persona_5, R.drawable.persona_6
        )

        var selectedIndex by remember { mutableStateOf<Int?>(null) } //detects clicked button
        val sfProFont = FontFamily(
            Font(R.font.sf_pro_display_regular, FontWeight.Normal),
            Font(R.font.sf_pro_display_bold, FontWeight.Bold)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(135.dp)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(personaLabels.chunked(3)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { label ->
                            Button(
                                onClick = { selectedIndex = personaLabels.indexOf(label) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontFamily = sfProFont,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        //show the dialog dynamically when a button is clicked
        selectedIndex?.let { index ->
            ShowFloatingCard(
                onDismiss = { selectedIndex = null },
                label = personaLabels[index],
                detail = personaDescriptions[index],
                personaImage = personaImages[index]
            )
        }
    }

    @Composable
    fun CheckboxWithLabel(label: String, viewModel: FoodQuestionnaireViewModel) {
        val sfProFont = FontFamily(
            Font(R.font.sf_pro_display_regular, FontWeight.Normal),
            Font(R.font.sf_pro_display_bold, FontWeight.Bold)
        )

        val categoryState = viewModel.foodCategoryMap[label] ?: return
        val checked by categoryState.observeAsState(initial = false)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = { isChecked ->
                    categoryState.value = isChecked
                },
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = label,
                fontFamily = sfProFont,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                softWrap = false
            )
        }
    }

    @Composable
    fun CheckboxGrid(viewModel: FoodQuestionnaireViewModel) {
        val foodItems = listOf(
            "Fruits", "Vegetables", "Grains",
            "Red meat", "Seafood", "Poultry",
            "Fish", "Eggs", "Nuts/Seeds"
        )

        Box(modifier = Modifier.height(150.dp)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                items(foodItems.chunked(3)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { label ->
                            Box(
                                modifier = Modifier.weight(1f, fill = true)
                            ) {
                                CheckboxWithLabel(label, viewModel)
                            }
                        }
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBar(registerAndLoginScreenViewModel: RegisterAndLoginScreenViewModel) {
        val context = LocalContext.current

        val onBackPressedDispatcher =
            LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

        val sfProFont = FontFamily(
            Font(R.font.sf_pro_display_regular, FontWeight.Normal),
            Font(R.font.sf_pro_display_bold, FontWeight.Bold)
        )
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text(
                    "Food Intake Questionnaire",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = sfProFont
                )
            },
            //quick logout functionality
            navigationIcon = {
                Button(onClick = {
                    registerAndLoginScreenViewModel.logout()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, LoginScreen::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                },
                    modifier = Modifier.padding(horizontal = 10.dp)) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null)
                }
            },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PersonaDropdown(viewModel: FoodQuestionnaireViewModel) {
        val selectedPersona by viewModel.persona.observeAsState("Select option")
        Log.d("PersonaDebug before textfield", "selectedPersona: $selectedPersona")

        var expanded by remember { mutableStateOf(false) }

        val personaLabels = listOf(
            "Health Devotee", "Mindful Eater", "Wellness Striver",
            "Balance Seeker", "Health Procrastinator", "Food Carefree"
        )

        val sfProFont = FontFamily(
            Font(R.font.sf_pro_display_regular, FontWeight.Normal),
            Font(R.font.sf_pro_display_bold, FontWeight.Bold)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            Log.d("PersonaDebug textfield", "selectedPersona: $selectedPersona")
            OutlinedTextField(
                value = selectedPersona,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Select option", fontFamily = sfProFont, fontSize = 15.sp)
                },
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Icon")
                },
                modifier = Modifier
                    .menuAnchor()
                    .wrapContentHeight()
                    .width(365.dp),
                shape = RoundedCornerShape(15.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                personaLabels.forEach { personaLabel ->
                    DropdownMenuItem(
                        text = { Text(personaLabel) },
                        onClick = {
                            viewModel.setPersona(personaLabel)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun TimePicker(
        context: Context,
        label: String,
        timeLiveData: LiveData<String>,
        expectedMeal: String,
        onTimeSelected: (String) -> Unit
    ) {
        val showDialog = remember { mutableStateOf(false) }
        val mCalendar = Calendar.getInstance()

        val hour = mCalendar.get(Calendar.HOUR_OF_DAY)
        val minutes = mCalendar.get(Calendar.MINUTE)

        val sfProFont = FontFamily(Font(R.font.sf_pro_display_regular, FontWeight.Normal))

        val timePicked by timeLiveData.observeAsState("00:00")

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
            Text(text = label, fontFamily = sfProFont)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { showDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 70.dp, vertical = 5.dp),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = timePicked,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = sfProFont
                    )
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Time Picker",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            //ensure time validation is correct
            if (showDialog.value) {
                TimePickerDialog(
                    context,
                    { _, selectedHour: Int, selectedMinute: Int ->
                        val newTime = "%02d:%02d".format(selectedHour, selectedMinute)

                        val morningRange = 5..11
                        val sleepRange = 20..24
                        val dinnerRange = 19..22

                        val isValidTime = when (expectedMeal.lowercase()) {
                            "morning" -> selectedHour in morningRange
                            "night" -> selectedHour in sleepRange
                            "dinner" -> selectedHour in dinnerRange

                            else -> true
                        }
                        if (isValidTime) {
                            onTimeSelected(newTime)
                        } else {
                            Toast.makeText(
                                context,
                                "Please select a valid time for $expectedMeal",
                                Toast.LENGTH_LONG
                            ).show()
                            showDialog.value = true
                        }
                    },
                    hour,
                    minutes,
                    false
                ).show()
                showDialog.value = false
            }
        }
    }

    @Composable
    fun Questionnaire(
        modifier: Modifier = Modifier,
        viewModel: FoodQuestionnaireViewModel,
        registerAndLoginScreenViewModel: RegisterAndLoginScreenViewModel
    ) {
        val context = LocalContext.current
        val loggedInUser = AuthManager.getPatientId()
        viewModel.loadCheckboxSelections(loggedInUser)

        val intent = Intent(context, MainActivityScreen::class.java)

        //allow scrolling on the page
        val scrollState = rememberScrollState()

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
                    .padding(5.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp),
                    thickness = 1.dp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Tick all the food categories you can eat: ", fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 15.dp)
                )
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
                    CheckboxGrid(viewModel = viewModel)
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    thickness = 2.dp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Your Persona", fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
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
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "People can be broadly classified into 6 different types based on" +
                                "their eating preferences. Click on each button to find out " +
                                "the different types, and select the type that best fits you.",
                        fontSize = 14.sp,
                        fontFamily = sfProFont,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 15.dp)
                    )
                    FloatingCard()
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    thickness = 2.dp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Which persona best fits you?", fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))

                //selecting persona
                PersonaDropdown(viewModel = viewModel)

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                ) {
                    Spacer(modifier = Modifier.height(7.dp))
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        thickness = 2.dp,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Timings", fontFamily = sfProFont,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 15.dp)
                    )
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceDim
                        )
                    )
                    {
                        Spacer(modifier = Modifier.height(13.dp))
                        TimePicker(
                            context,
                            "What time of day approx. do you " +
                                    "normally eat your biggest meal?",
                            viewModel.bigMealTime, "dinner", viewModel::setBigMealTime
                        )
                        Spacer(modifier = Modifier.height(13.dp))
                        TimePicker(
                            context,
                            "What time of day approx. do you " +
                                    "go sleep at night?",
                            viewModel.sleepTime, "night", viewModel::setSleepTime
                        )
                        Spacer(modifier = Modifier.height(13.dp))
                        TimePicker(
                            context,
                            "What time of day approx. do you " +
                                    "wake up in the morning?",
                            viewModel.wakeUpTime, "morning", viewModel::setWakeUpTime
                        )
                        Spacer(modifier = Modifier.height(13.dp))
                    }
                    Spacer(modifier = Modifier.height(13.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            if (loggedInUser != null) {
                                viewModel.addIntake(loggedInUser)
                            }
                            intent.putExtra("startDestination", "home")
                            context.startActivity(intent)
                        }
                    ) {
                        Text(
                            "Save & Continue",
                            fontFamily = sfProFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(13.dp))
                }
            }
        }
    }
}