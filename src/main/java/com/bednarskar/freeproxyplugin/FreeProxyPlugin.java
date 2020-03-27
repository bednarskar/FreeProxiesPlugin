package com.bednarskar.freeproxyplugin;

import com.bednarskar.proxycorn.api.ProxyCornPlugin;
import com.bednarskar.proxycorn.api.model.Filter;

import com.bednarskar.proxycorn.api.model.ProxyInstanceBasicInfo;

import java.util.Set;

public class FreeProxyPlugin implements ProxyCornPlugin {
    public String getName () {
        return "free proxy plugin";
    }

    @Override
    public Set<ProxyInstanceBasicInfo> getProxyInstanceBasicInfo (Filter filter) throws Exception {
        Client client = new Client();
        return client.getThemAll(filter);
        //TODO prepare request based on filter
    }

}
