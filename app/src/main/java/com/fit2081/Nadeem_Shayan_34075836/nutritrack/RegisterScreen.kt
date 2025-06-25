package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.delay

class RegisterScreen : ComponentActivity() {
    private lateinit var viewModel: RegisterAndLoginScreenViewModel
    private lateinit var settingScreenViewModel: SettingScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PatientRepo(applicationContext)
        val dataStoreRepo = DataStoreRepo(applicationContext)

        val factory = RegisterAndLoginScreenViewModelFactory(repository, dataStoreRepo)
        val settingFactory = SettingScreenViewModelFactory(repository, dataStoreRepo)

        viewModel = ViewModelProvider(this, factory)[RegisterAndLoginScreenViewModel::class.java]
        settingScreenViewModel = ViewModelProvider(this, settingFactory)[SettingScreenViewModel::class.java]

        setContent {
            val isDarkMode by settingScreenViewModel.isDarkMode.observeAsState(false)
            NutriTrackTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBarExample("Register", LoginScreen::class.java) }
                ) { innerPadding ->
                    RegisterScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, viewModel: RegisterAndLoginScreenViewModel) {
    val userNameInput by viewModel.name.observeAsState("")
    val passwordInput by viewModel.password.observeAsState("")
    val confirmPasswordInput by viewModel.confirmPassword.observeAsState("")

    val context = LocalContext.current

    val scrollState = rememberScrollState()

    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )

    //load in user ids from database
    LaunchedEffect(Unit) {
        viewModel.fetchUserIds()
    }

    val registrationStatus by viewModel.registrationLiveStatus.observeAsState()
    val passwordStatus by viewModel.passwordStatus.observeAsState()

    //validate registration input to ensure details match in database
    LaunchedEffect(registrationStatus) {
        registrationStatus?.let {
            var registerSuccessStatusCheck = it
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearRegistrationStatus()
            if (registerSuccessStatusCheck == "Registration successful, please login with your registered details.") {
                val intent = Intent(context, LoginScreen::class.java)
                context.startActivity(intent)
            }
        }
    }

    val selectedUser by viewModel.userId.observeAsState("")
    val userIds by viewModel.allUserIds.collectAsState()

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
                painter = painterResource(id = R.drawable.register),
                contentDescription = "RegisterPic",
                modifier = Modifier.size(250.dp)
                    .clip(RoundedCornerShape(1.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            UserIdDropdown(
                userIds = userIds,
                selectedUserId = selectedUser.toString(),
                onUserIdSelected = { userId ->
                    viewModel.userId.value = userId.toIntOrNull()
                }
            )

            val phoneNumberInput = viewModel.phoneNumber.observeAsState().value?.toString() ?: ""

            //username and password
            TextField(userNameInput, { viewModel.name.value = it }, "Enter your username", KeyboardType.Text)
            TextField(phoneNumberInput, { viewModel.phoneNumber.value = it.filter { c -> c.isDigit() }.toLongOrNull() }, "Enter your phone number",
                KeyboardType.Number)
            PasswordField(passwordInput, { viewModel.password.value = it }, "Enter your password",
                KeyboardType.Password)

            //extra validation for password to ensure user uses a secure combination
            passwordStatus?.let{
                Text(
                    text = it,
                    color = if (it.contains("Valid password", ignoreCase = true)) Color.Green else Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sfProFont,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp
                )
                LaunchedEffect(it) {
                    delay(3000)
                    viewModel.clearPasswordStatus()
                }
            }
            PasswordField(confirmPasswordInput, { viewModel.confirmPassword.value = it }, "Enter your password again",
                KeyboardType.Password)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    viewModel.registeredUser()
                }
            ) {
                Text("Register", fontFamily = sfProFont, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "This app is only for pre-registered users. Please " +
                    "enter your ID, phone number and password to claim your account.", fontFamily = sfProFont,
                textAlign = TextAlign.Center, fontSize = 15.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(value: String, onValueChange: (String) -> Unit, label: String, keyboardType: KeyboardType) {
    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = sfProFont) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFB1D18A),
            unfocusedBorderColor = Color(0xFFC5C8BA)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//password field for masking and show/hide functionality
fun PasswordField(value: String, onValueChange: (String) -> Unit, label: String, keyboardType: KeyboardType) {
    var showPassword by remember { mutableStateOf(false) }

    val sfProFont = FontFamily(
        Font(R.font.sf_pro_display_regular, FontWeight.Normal),
        Font(R.font.sf_pro_display_bold, FontWeight.Bold)
    )

    val visualTransformation = if (showPassword) {
        VisualTransformation.None
    } else
    {
        PasswordVisualTransformation()
    }

    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = sfProFont) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(onClick = {showPassword = !showPassword}) {
                Icon(
                    painter = painterResource(id = if (showPassword) R.drawable.visible else R.drawable.visible_off),
                    modifier = Modifier.size(30.dp)
                        .padding(horizontal = 4.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = if (showPassword) "Hide password" else "Show password"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFB1D18A),
            unfocusedBorderColor = Color(0xFFC5C8BA)
        )
    )
}