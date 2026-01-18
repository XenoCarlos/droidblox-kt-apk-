package com.drake.droidblox.apiservice

import com.drake.droidblox.apiservice.models.ipinfo.IpLocation
import com.drake.droidblox.apiservice.models.rovalra.RawRoValraIpLocation
import com.drake.droidblox.apiservice.models.rovalra.RoValraIpLocation
import com.drake.droidblox.logger.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IpLocationApi @Inject constructor(
    private val logger: Logger,
    private val httpClient: HttpClient
) {
    companion object {
        private const val TAG = "IpLocationApi"
    }

    suspend fun fetchIplocationWithIPInfo(
        ip: String
    ): String? {
        logger.d(TAG, "Fetching ip location $ip from ip info")
        val ipInfoReq = httpClient.get(
            "https://ipinfo.io/$ip/json"
        )
        if (ipInfoReq.status != HttpStatusCode.OK) {
            logger.e(TAG, "Failed to fetch ip location $ip from ip info")
            return null
        } else {
            val location: IpLocation = ipInfoReq.body()
            return if (location.city == location.region) {
                "${location.city}, ${location.country}"
            } else {
                "${location.city}, ${location.region}, ${location.country}"
            }
        }
    }

    suspend fun fetchIPLocationWithRoValra(
        ip: String
    ): String? {
        // thank you rovalrat for giving me permission
        logger.d(TAG, "fetching ip location $ip from rovalra")
        val roValraIpLocationReq = httpClient.get(
            "https://apis.rovalra.com/v1/geolocation?ip=$ip"
        )
        if (roValraIpLocationReq.status != HttpStatusCode.OK) {
            logger.e(TAG, "failed to get ip location $ip from rovalra")
            return null
        }
        val location: RoValraIpLocation = roValraIpLocationReq.body<RawRoValraIpLocation>().location
        return if (location.city == location.region) {
            "${location.city}, ${location.country}"
        } else {
            "${location.city}, ${location.region}, ${location.country}"
        }
    }
    suspend fun fetchIPLocation(
        ip: String
    ): String? {
        var ipLocation: String? = fetchIPLocationWithRoValra(ip)
        if (ipLocation == null) {
            logger.w(TAG, "Falling back to IPInfo")
            ipLocation = fetchIplocationWithIPInfo(ip)
            if (ipLocation == null) {
                logger.e(TAG, "Couldn't find IP location for ip address $ip")
                return null
            }
        }
        logger.d(TAG, "$ip located at $ipLocation")
        return ipLocation
    }
}