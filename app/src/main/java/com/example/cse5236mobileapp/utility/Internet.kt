package com.example.cse5236mobileapp.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService

class Internet(private val context: Context) {
    companion object{
        private const val TAG = "NETWORK STATE"
    }

    private val connectivityManager = getSystemService(context, ConnectivityManager::class.java)
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    // callback listener interface
    interface NetworkStateListener {
        fun onNetworkAvailable()
        fun onNetworkLost()
    }

    fun startMonitoring(listener: NetworkStateListener){
        networkCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                listener.onNetworkAvailable()
                Log.d(TAG, "Network available")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                listener.onNetworkLost()
                Log.d(TAG, "Network lost")
            }
        }
        connectivityManager?.registerDefaultNetworkCallback(networkCallback!!)
    }

    fun stopMonitoring() {
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
        }
    }

}