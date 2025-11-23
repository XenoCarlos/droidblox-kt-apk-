package com.drake.droidblox.backend.activitywatcher

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

private const val TAG = "DBIpLocation"

class IpLocationApi {
    companion object {
        private var cachedRoSealListOfIP = listOf<Map<String, Any>>()

        private suspend fun fetchIPLocationWithIPInfo(
            httpClient: HttpClient,
            ip: String
        ): String? {
            val requestTo = "https://ipinfo.io/$ip/json"

            try {
                Log.d(TAG, "Requesting GET to $requestTo")
                val ipinfoReq = httpClient.get(requestTo)

                if (ipinfoReq.status.value != 200) {
                    Log.e(
                        TAG,
                        "Failed to get the IP location with ip info. Got ${ipinfoReq.status.value}.\nText:\n${ipinfoReq.bodyAsText()}"
                    )
                } else {
                    val location = ipinfoReq.body<Map<String, Any>>()
                    return if (location["city"] == location["region"]) {
                        "${location["region"]}, ${location["country"]}"
                    } else {
                        "${location["city"]}, ${location["region"]}, ${location["country"]}"
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Something went wrong while fetching IP location with ip info!;${e.message}"
                )
            }
            return null
        }

        private suspend fun getIPLocationWithRoSeal(
            httpClient: HttpClient,
            ip: String
        ): String? {
            if (cachedRoSealListOfIP.isEmpty()) {
                val requestTo =
                    "https://raw.githubusercontent.com/RoSeal-Extension/Top-Secret-Thing/refs/heads/main/data/datacenters.json"
                try {
                    Log.d(TAG, "Requesting GET to $requestTo")
                    val rosealReq = httpClient.get(requestTo)
                    if (rosealReq.status.value != 200) {
                        Log.e(
                            TAG,
                            "Failed to get the list of ip locations. Got ${rosealReq.status.value}\nText:\n${rosealReq.bodyAsText()}"
                        )
                    } else {
                        cachedRoSealListOfIP = rosealReq.body()
                    }
                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "Something went wrong while trying to cache the list of ip locations!; ${e.message}"
                    )
                    return null
                }
            }

            for (locationOfIp in cachedRoSealListOfIP) {
                val listOfIps = locationOfIp["ips"] as List<*>
                if (listOfIps.contains(ip)) {
                    val location = locationOfIp["location"] as Map<*, *>
                    return if (location["city"] == location["region"]) {
                        "${location["city"]}, ${location["country"]}"
                    } else {
                        "${location["city"]}, ${location["region"]}, ${location["country"]}"
                    }
                }
            }
            return null
        }

        suspend fun fetchIPLocation(
            httpClient: HttpClient,
            ip: String
        ): String? {
            Log.d(TAG, "Attempting to get IP location with RoSeal's list of ips")
            var location = getIPLocationWithRoSeal(httpClient, ip)
            if (location == null) {
                Log.e(TAG, "Didn't find the IP Location. Falling back to IpInfo.io")
                location = fetchIPLocationWithIPInfo(httpClient, ip)
                if (location != null) {
                    Log.e(TAG, "Couldn't find the IP location too, returning null!")
                }
            }
            Log.d(TAG, "IP Location: $location")
            return location
        }
    }

}