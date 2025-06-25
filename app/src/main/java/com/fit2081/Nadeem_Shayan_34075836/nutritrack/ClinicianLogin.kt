package com.fit2081.Nadeem_Shayan_34075836.nutritrack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.SettingScreenViewModel
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ViewModel.ViewModelFactory.SettingScreenViewModelFactory
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.DataStoreRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.Repositories.PatientRepo
import com.fit2081.Nadeem_Shayan_34075836.nutritrack.ui.theme.NutriTrackTheme

class ClinicianLogin: ComponentActivity() {
    private lateinit var viewModel: SettingScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PatientRepo(applicationContext) // can reuse or share instance
        val dataStoreRepo = DataStoreRepo(applicationContext)

        val factory = SettingScreenViewModelFactory(repository, dataStoreRepo)

        viewModel = ViewModelProvider(this, factory)[SettingScreenViewModel::class.java]

        setContent {
            val isDarkMode by viewModel.isDarkMode.observeAsState(false)
            NutriTrackTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    ClinicianLogin(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ClinicianLogin(modifier: Modifier = Modifier) {
    var clinicianKeyEntered by remember { mutableStateOf("") }
    val clinicianKey by remember { mutableStateOf("dollar-entry-apples") }

    val context = LocalContext.current
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
                .verticalScroll(scrollState)
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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
                text = "Clinician Login", fontFamily = sfProFont,
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
            Image(
                painter = painterResource(id = R.drawable.clinician_login),
                contentDescription = "HealthAppLogo",
                modifier = Modifier.size(350.dp)
                    .clip(RoundedCornerShape(1.dp))
            )

            //username and password
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = clinicianKeyEntered,

                onValueChange = { clinicianKeyEntered = it },
                label = { Text("Enter your clinician key", fontFamily = sfProFont) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 5.dp),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB1D18A),
                    unfocusedBorderColor = Color(0xFFC5C8BA)
                )
            )
            Spacer(modifier = Modifier.height(22.dp))
            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    //check if username and password are correct
                    if (clinicianKey == clinicianKeyEntered) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show()

                        val intent = Intent(context, ClinicianDashboard::class.java)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_LONG).show()
                    }
                }
            ) {
                Text(
                    "Clinician Login",
                    fontFamily = sfProFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
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
                        "Only registered nutritionists should access the Dashboard. \n" +
                                "To obtain the key, please contact the NutriTrack HR department.",
                        fontFamily = sfProFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}