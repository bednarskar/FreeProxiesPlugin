package com.bednarskar.freeproxyplugin;

import com.bednarskar.proxycorn.api.model.Filter;
import com.bednarskar.proxycorn.api.model.Protocol;
import com.bednarskar.proxycorn.api.model.ProxyInstanceBasicInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Client {
    public Set<ProxyInstanceBasicInfo> getThemAll(Filter filter) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("https://api.getproxylist.com/proxy?protocol[]=http");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        int i =0;
        while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
        }
        reader.close();

        // print result
        System.out.println(response.toString());
        httpClient.close();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(response.toString());

        Set<ProxyInstanceBasicInfo> setOfProxies = new HashSet<>();
        setOfProxies.add(new ProxyInstanceBasicInfo(Protocol.http, node.get("ip").asText(), node.get("port").asInt()));
        return setOfProxies;
    }
}
