package com.bednarskar.freeproxyplugin;

import com.bednarskar.proxycorn.api.model.Filter;
import com.bednarskar.proxycorn.api.model.Protocol;
import com.bednarskar.proxycorn.api.model.ProxyInstanceBasicInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Client {

	static final Logger LOGGER = Logger.getLogger(Client.class);
	public static final String PROTOCOL = "protocol";
	public static final String ALLOWS_HTTPS = "allowsHttps";
	public static final String HTTP = "http";
	public static final String TRUE = "true";
	public static final String IP = "ip";
	public static final String PORT = "port";
	// TODO: allow configure it in main application in some way (maybe each plugin should be configured separately).
	// default value:
	private int TRY_NUMBER = 100;
	private ObjectMapper objectMapper;
	private BufferedReader reader;

	public Client() {
		this.objectMapper = new ObjectMapper();
	}

	public Set<ProxyInstanceBasicInfo> getThemAll(Filter filter) throws Exception {
		Set<ProxyInstanceBasicInfo> pluginResponse = new HashSet<>();
		FreeProxyRequestBuilder requestBuilder = new FreeProxyRequestBuilder(filter);

		HttpUriRequest request = requestBuilder.buildRequest();
		LOGGER.info("Request: " + request.toString());

		for (int i = 0; i <= TRY_NUMBER; i++) {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse httpResponse = httpClient.execute(request);
			String response = null;
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				response = parseResponse(httpResponse);
				LOGGER.debug("RESPONSE: \n" + response);

				if (!response.isEmpty()) {
					Set<ProxyInstanceBasicInfo> setOfProxies = responseToProxyInstanceBasicInfo(response);
					pluginResponse.addAll(setOfProxies);
				} else {
					httpClient.close();
					return pluginResponse;
				}
			}
			httpClient.close();
		}
		return pluginResponse;
	}

	private Set<ProxyInstanceBasicInfo> responseToProxyInstanceBasicInfo(String response) throws JsonProcessingException {
		JsonNode node = objectMapper.readTree(response);
		Set<ProxyInstanceBasicInfo> setOfProxies = new HashSet<>();

		node.forEach(n -> {
			try {
				String protocol = node.get(PROTOCOL).asText();
				if (node.get(ALLOWS_HTTPS).asText().equals(TRUE) && protocol.equalsIgnoreCase(HTTP)) {
					setOfProxies.add(new ProxyInstanceBasicInfo(Protocol.https, node.get(IP).asText(), node.get(PORT).asInt()));
				} else {
					setOfProxies.add(new ProxyInstanceBasicInfo(Protocol.valueOf(protocol), node.get(IP).asText(), node.get(PORT).asInt()));
				}
			} catch (Exception e) {
				LOGGER.error("Error parsing response: ", e);
			}
		});
		return setOfProxies;
	}

	private String parseResponse(CloseableHttpResponse httpResponse) throws IOException {
		reader = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent()));
		String inputLine;
		StringBuffer stringBuffer = new StringBuffer();
		while ((inputLine = reader.readLine()) != null) {
			stringBuffer.append(inputLine);
		}
		reader.close();
		return stringBuffer.toString();
	}
}
