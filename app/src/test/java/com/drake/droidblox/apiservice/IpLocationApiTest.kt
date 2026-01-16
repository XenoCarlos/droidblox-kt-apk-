package com.drake.droidblox.apiservice

import com.drake.droidblox.apiservice.httpclient.customHttpClient
import com.drake.droidblox.logger.TestLogger
import kotlinx.coroutines.runBlocking
import org.junit.Test

class IpLocationApiTest {
    companion object {
        private const val TAG = "IpLocationApiTest"
    }

    private val logger = TestLogger
    private val ipLocationApi = IpLocationApi(
        logger = logger,
        httpClient = customHttpClient(logger,"UNIT_TEST")
    )

    @Test fun testIpInfo() = runBlocking {
        logger.i(TAG, "Testing IpInfo.io")
        val ipLocation = ipLocationApi.fetchIplocationWithIPInfo("107.180.197.170")
        logger.i(TAG, "IP Location: $ipLocation")
    }

    @Test fun testRoValra() = runBlocking {
        logger.i(TAG, "Testing RoValra")
        val ipLocation = ipLocationApi.fetchIPLocationWithRoValra("107.180.197.170")
        logger.i(TAG, "IP Location: $ipLocation")
    }

    /* you can notice that the ip location of both are not the same
     * and rovalra's ip location tracker is more accurate
     * even though majority of the people who will use this doesn't
     * give a single fuck whether its in the same state or not
     */
}