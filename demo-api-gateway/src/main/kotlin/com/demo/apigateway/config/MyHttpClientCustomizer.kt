package com.demo.apigateway.config

import io.netty.channel.ChannelOption
import org.springframework.cloud.gateway.config.HttpClientCustomizer
import reactor.netty.http.client.HttpClient

class MyHttpClientCustomizer: HttpClientCustomizer {

    override fun customize(httpClient: HttpClient?): HttpClient? {
        return httpClient?.tcpConfiguration { tcpClient ->
            tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
            tcpClient.option(ChannelOption.SO_KEEPALIVE, true)
        }
    }
}
