package com.bednarskar.freeproxyplugin;

import com.bednarskar.proxycorn.api.model.Filter;
import com.neovisionaries.i18n.CountryCode;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import java.util.Optional;

public class FreeProxyRequestBuilder {

	public static final String ANONYMITY_ARG = "anonymity[]";
	public static final String LAST_TESTED_ARG = "lastTested";
	public static final String ALLOWS_POST_ARG = "allowsPost";
	public static final String MIN_UPTIME_ARG = "minUptime";
	public static final String HTTPS = "https";
	public static final String PROTOCOL_ARG = "protocol[]";
	public static final String ALLOWS_HTTPS_ARG = "allowsHttps";
	public static final String COUNTRY_ARG = "country[]";
	public static final String PORT_ARG = "port[]";
	private static final String FREE_PROXY_API_URL = "https://api.getproxylist.com/proxy";
	private static final String LAST_TESTED = "3600";
	private static final String HIGH_ANONYMITY = "high anonymity";
	private static final String ANONYMITY = "anonymity";
	private static final String ALLOW_POST = "1";
	// percentage
	private static final String MIN_UPTIME = "75";
	private RequestBuilder requestBuilder;
	private Filter filter;

	public FreeProxyRequestBuilder(Filter filter){
		this.requestBuilder = RequestBuilder.get();
		this.filter = filter;
	}

	public RequestBuilder buildRequest() {
		requestBuilder.setUri(FREE_PROXY_API_URL);
		addProtocols();
		addCountries();
		addPorts();
		requestBuilder.addParameter(ANONYMITY_ARG, HIGH_ANONYMITY);
		requestBuilder.addParameter(ANONYMITY_ARG, ANONYMITY);
		requestBuilder.addParameter(LAST_TESTED_ARG, LAST_TESTED);
		requestBuilder.addParameter(ALLOWS_POST_ARG, ALLOW_POST);
		requestBuilder.addParameter(MIN_UPTIME_ARG, MIN_UPTIME);
		return requestBuilder;
	}

	private void addProtocols() {
		for(String protocol : filter.getProtocols()) {
			if(!protocol.equalsIgnoreCase(HTTPS)) {
				requestBuilder.addParameter(PROTOCOL_ARG, protocol);
			} else {
				requestBuilder.addParameter(ALLOWS_HTTPS_ARG, "1");
			}
		}
	}
	private void addCountries() {
		for(String countryCode : filter.getCountryCodes()) {
			Optional<CountryCode> cc = Optional.ofNullable(CountryCode.getByCode(countryCode));
			cc.ifPresent(code -> requestBuilder.addParameter(COUNTRY_ARG, code.getAlpha2()));
		}
	}
	private void addPorts() {
		for(String port : filter.getPortNumbers()) {
			requestBuilder.addParameter(PORT_ARG, port);
		}
	}
}
