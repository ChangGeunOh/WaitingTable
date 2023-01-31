package kr.pe.paran.waiting.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.os.Build
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import kr.pe.paran.waiting.domain.model.NetworkInfo
import kr.pe.paran.waiting.domain.model.NetworkType
import java.net.NetworkInterface
import java.util.*


object NetworkUtils {

    const val PUBLIC_IP_SERVER = "http://api.ipify.org/"

    private fun provideHttpClient() = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    fun getDeviceAddress(): String {
        var deviceIPAddress = ""
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        interfaces.forEach { networkInterface ->
            networkInterface.inetAddresses.toList().forEach { inetAddress ->
                if (!inetAddress.isLoopbackAddress && inetAddress.isSiteLocalAddress) {
                    deviceIPAddress = inetAddress.hostAddress ?: ""
                }
            }
        }
        return deviceIPAddress
    }

    suspend fun getPublicAddress(): String {
        var address = ""
        provideHttpClient().use {
            address = it.get(PUBLIC_IP_SERVER).body()
        }
        return address
    }

    private fun getCurrentConnectivityState(
        connectivityManager: ConnectivityManager
    ): NetworkInfo {
        val connected = connectivityManager.allNetworks.any { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        }

        return if (connected) NetworkInfo(isAvailable = true) else NetworkInfo()
    }

    fun Context.flowNetworkInfo() = callbackFlow {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                var networkInfo = NetworkInfo()
                connectivityManager.getNetworkCapabilities(network)?.let {
                    networkInfo = getNetworkInfo(it)
                }
                trySend(networkInfo)
            }

            override fun onLost(network: Network) {
                trySend(NetworkInfo())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(getNetworkInfo(networkCapabilities))
            }
        }


        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        val currentState = getCurrentConnectivityState(connectivityManager)
        trySend(currentState)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    fun getNetworkInfo(networkCapabilities: NetworkCapabilities) : NetworkInfo {

        val networkInfo = NetworkInfo()

        networkInfo.type = when  {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // android.permission.ACCESS_FINE_LOCATION
                    // android.permission.ACCESS_WIFI_STATE
                    val transportInfo = networkCapabilities.transportInfo as WifiInfo
                    networkInfo.ssid = transportInfo.ssid
                }
                NetworkType.WIFI
            }
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
            else -> NetworkType.NONE
        }

        if (networkInfo.type != NetworkType.NONE) {
            networkInfo.isAvailable = true
            networkInfo.deviceAddress = getDeviceAddress()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                networkInfo.strength = networkCapabilities.signalStrength
            }
        }

        return networkInfo
    }
}





