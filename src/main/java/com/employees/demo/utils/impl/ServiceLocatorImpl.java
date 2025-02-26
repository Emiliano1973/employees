package com.employees.demo.utils.impl;

import com.employees.demo.utils.ServiceLocator;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServiceLocatorImpl implements ServiceLocator {

    private final DiscoveryClient discoveryClient;
    private final Map<String, String> mapLocator;

    public ServiceLocatorImpl(final DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.mapLocator=new ConcurrentHashMap<>();
    }

    @Override
    public String getUrlByServiceName(final String serviceName) {
        Objects.requireNonNull(serviceName, "Error!! Service name must not be null") ;
        if (this.mapLocator.containsKey(serviceName)) {
            return this.mapLocator.get(serviceName);
        }
        final String urlService=getServiceUrl(serviceName);
        this.mapLocator.put(serviceName, urlService);
        return urlService;
    }


    private String getServiceUrl(final String serviceName) {
        return discoveryClient.getInstances(serviceName)
                .stream()
                .findFirst()
                .map(instance -> {
                    // Se la porta è 443, usiamo HTTPS
                    String protocol = instance.getPort() == 443 ? "https" : "http";
                    return new StringBuilder().append(protocol).append("://")
                            .append(instance.getUri().getHost()).append(":")
                            .append(instance.getPort()).toString();
                })
                .orElseThrow(() -> new RuntimeException("Service not found!"));
    }
}
