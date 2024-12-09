package com.example.retrofitsample.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitsample.ui.network.RetrofitInstance
import com.example.retrofitsample.ui.network.model.Constant
import com.example.retrofitsample.ui.network.model.NetworkResponse
import com.example.retrofitsample.ui.network.model.WeatherModel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val weatherApiService = RetrofitInstance.weatherApiService
    private val _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherData: LiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun getData(
        city: String,
    ){
        _weatherData.value = NetworkResponse.Loading
        viewModelScope.launch {
           val response =  weatherApiService.getWeather(Constant.apiKey, city)
            try {
                if (response.isSuccessful){
                    response.body()?.let {
                        _weatherData.value = NetworkResponse.Success(it)
                    }
                }else{
                    _weatherData.value = NetworkResponse.Error(response.message())
                }
            }
            catch (e: Exception){
                _weatherData.value = NetworkResponse.Error(e.message.toString())
            }
        }
    }
}