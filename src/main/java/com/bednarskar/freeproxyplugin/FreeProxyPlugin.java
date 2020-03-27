package com.bednarskar.freeproxyplugin;

import com.bednarskar.proxycorn.api.ProxyCornPlugin;
import com.bednarskar.proxycorn.api.model.Filter;

import com.bednarskar.proxycorn.api.model.ProxyInstanceBasicInfo;
import org.apache.log4j.Logger;

import java.util.Set;

public class FreeProxyPlugin implements ProxyCornPlugin {
    final static Logger LOGGER = Logger.getLogger(FreeProxyPlugin.class);

    public String getName () {
        return "free proxy plugin";
    }

    @Override
    public Set<ProxyInstanceBasicInfo> getProxyInstanceBasicInfo (Filter filter) throws Exception {
        LOGGER.debug("freeproxyplugin started...");
        LOGGER.debug("Filter: " + filter.toString());
        Client client = new Client();
        return client.getThemAll(filter);
        //TODO prepare request based on filter
    }

}
