module com.bednarskar.freeproxyplugin {
//    requires com.bednarskar.proxycorn.api;
    provides com.bednarskar.proxycorn.api.ProxyCornPlugin
            with com.bednarskar.freeproxyplugin.FreeProxyPlugin;
}