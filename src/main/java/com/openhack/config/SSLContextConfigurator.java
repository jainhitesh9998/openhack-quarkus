package com.openhack.config;

import io.quarkus.elasticsearch.restclient.lowlevel.ElasticsearchClientConfig;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;

@ElasticsearchClientConfig
public class SSLContextConfigurator implements RestClientBuilder.HttpClientConfigCallback {
    @Override
    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
        try {
            SSLContextBuilder sslBuilder = SSLContexts.custom()
                    .loadTrustMaterial(null,(x509Certificates, s) -> true);
            SSLContext sslContext = sslBuilder.build();
            httpClientBuilder.setSSLContext(sslContext);
            httpClientBuilder.setHostnameVerifier( new AllowAllHostnameVerifier());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return httpClientBuilder;
    }
}
