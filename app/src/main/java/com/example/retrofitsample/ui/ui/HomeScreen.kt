package com.example.retrofitsample.ui.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.retrofitsample.R
import com.example.retrofitsample.ui.network.model.NetworkResponse
import com.example.retrofitsample.ui.network.model.WeatherModel


@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel
){

    var city by remember { mutableStateOf("") }
    val weatherData by viewModel.weatherData.observeAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ){
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                },
                label = { Text(text = "Please Enter Your City ")},
                singleLine = true
            )

            IconButton(
                onClick = {
                    viewModel.getData(city)
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon"
                )
            }
        }
        when(weatherData){
            is NetworkResponse.Success ->{
                val data = weatherData as NetworkResponse.Success<WeatherModel>
                WeatherDetails(data = data.data)
            }
            is NetworkResponse.Error -> {
                val data = weatherData as NetworkResponse.Error
                Text(text = data.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            null -> {
                Text(text = "No Data")
            }
        }
    }
}

@Composable
fun WeatherDetails(
    data: WeatherModel
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country , fontSize = 18.sp , color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = data.current.temp_c + "°C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            model = "https:${data.current.condition.icon}".replace("64x64" , "128x128"),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    WeatherKeyVal(key = "Humidity" , value = data.current.humidity)
                    Spacer(modifier = Modifier.width(16.dp))
                    WeatherKeyVal(key = "Wind Speed" , value = data.current.wind_kph)

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    WeatherKeyVal(key = "Feels Like" , value = data.current.feelslike_c)
                    Spacer(modifier = Modifier.width(16.dp))
                    WeatherKeyVal(key = "UV Index" , value = data.current.uv)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    WeatherKeyVal(key = "Visibility" , value = data.current.vis_km)
                    Spacer(modifier = Modifier.width(16.dp))
                    WeatherKeyVal(key = "Pressure" , value = data.current.pressure_mb)
                }
            }
        }
    }
}

@Composable
fun WeatherKeyVal(
    key: String , value: String
){
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = key , fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(
        modifier = Modifier,
        viewModel = HomeViewModel()
    )
}