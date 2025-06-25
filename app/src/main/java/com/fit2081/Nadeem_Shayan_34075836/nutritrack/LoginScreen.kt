package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.RegisterAndLoginScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.SettingScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.RegisterAndLoginScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.SettingScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ui.theme.NutriTrackTheme

class LoginScreen : ComponentActivity() {
    //initializing viewmodels
    private lateinit var viewModel: RegisterAndLoginScreenViewModel
    private lateinit var settingScreenViewModel: SettingScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PatientRepo(applicationContext)
        val dataStoreRepo = DataStoreRepo(applicationContext)

        val factory = RegisterAndLoginScreenViewModelFactory(repository, dataStoreRepo)
        val settingFactory = SettingScreenViewModelFactory(repository, dataStoreRepo)

        settingScreenViewModel = ViewModelProvider(this, settingFactory)[SettingScreenViewModel::class.java]
        viewModel = ViewModelProvider(this, factory)[RegisterAndLoginScreenViewModel::class.java]

        setContent {
            val isDarkMode by settingScreenViewModel.isDarkMode.observeAsState(false)
            NutriTrackTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBarExample("Log in", MainActivity::class.java) }
                ) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        loginViewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarExample(label: String, activityClass: Class<out Activity>) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
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
                label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = sfProFont
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, activityClass)
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, loginViewModel: RegisterAndLoginScreenViewModel) {
    val passwordInput by loginViewModel.password.observeAsState("")
    val context = LocalContext.current

    //loading in user ids from database
    LaunchedEffect(Unit) {
        loginViewModel.fetchUserIds()
    }

    val scrollState = rememberScrollState()

    val userId by loginViewModel.userId.observeAsState()
    val selectedUser = userId?.toString() ?: ""

    val userIds by loginViewModel.allUserIds.collectAsState()
    val isLoggedInState = loginViewModel.checkLoggedIn().collectAsState(initial = null)

    val isLoggedIn = isLoggedInState.value

    //using launched effect to run task in background for checking if user is logged in
    if (isLoggedIn == true) {
        LaunchedEffect(Unit) {
            context.startActivity(Intent(context, FoodQuestionnaire::class.java))
        }
    }

    if (isLoggedIn == false) {
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
                    .padding(17.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_page),
                    contentDescription = "HealthAppLogo",
                    modifier = Modifier.size(350.dp)
                        .clip(RoundedCornerShape(1.dp))
                )

                Spacer(modifier = Modifier.height(14.dp))
                UserIdDropdown(
                    userIds = userIds,
                    selectedUserId = selectedUser.toString(),
                    onUserIdSelected = { userId ->
                        loginViewModel.userId.value = userId.toIntOrNull()
                    }
                )

                //username and password
                PasswordField(passwordInput, { loginViewModel.password.value = it }, "Enter your password",
                    KeyboardType.Password)


                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        loginViewModel.validateLogin(selectedUser.toIntOrNull(), passwordInput)
                    }
                ) {
                    Text("Login", fontFamily = sfProFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                val loginStatus by loginViewModel.loginStatus.observeAsState()

                //validates input to make sure it matches in database for login
                LaunchedEffect(loginStatus) {
                    loginStatus?.let {
                        var loginSuccessStatusCheck = it
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        loginViewModel.clearLoginStatus()
                        if (loginSuccessStatusCheck == "Successfully logged in") {
                            val intent = Intent(context, FoodQuestionnaire::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        val intent = Intent(context, RegisterScreen::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Text("Register", fontFamily = sfProFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "This app is only for pre-registered users. Please " +
                        "have your ID and phone number ready before continuing.", fontFamily = sfProFont,
                    textAlign = TextAlign.Center)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserIdDropdown(userIds: List<Int>, selectedUserId: String, onUserIdSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedUserId,
            onValueChange = {},
            readOnly = true,
            label = { Text("My ID (Provided by your Clinician)", fontFamily = sfProFont) },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Icon")
            },
            modifier = Modifier
                .menuAnchor()
                .width(365.dp),
                shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFB1D18A),
            unfocusedBorderColor = Color(0xFFC5C8BA)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            userIds.forEach { userId ->
                DropdownMenuItem(
                    text = { Text(userId.toString()) },
                    onClick = {
                        expanded = false
                        onUserIdSelected(userId.toString())  //pass selected ID
                    }
                )
            }
        }
    }
}