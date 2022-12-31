package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.ApiHeader;
import com.axial.modules.openapi_manager.model.AppHeader;
import com.axial.modules.openapi_manager.model.config.HeaderConfig;
import com.axial.modules.openapi_manager.model.config.SecurityHeaderConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ApiData {

    private ApiData() { }

    private static Map<String, AppHeader> appHeaders = new ConcurrentHashMap<>();


    public static Map<String, AppHeader> getAppHeaders() {
        return appHeaders;
    }

    public static AppHeader getAppHeader(String headerName) {
        return appHeaders.get(headerName);
    }

    public static boolean isHeaderAllowed(String headerName) {
        return appHeaders.containsKey(headerName);
    }

    public static void addAppHeaderIfNotExist(List<AppHeader> headers) {
        if (OpenApiDataUtils.isEmpty(headers)) {
            return;
        }
        headers.forEach(hd -> addAppHeaderIfNotExist(hd));
    }

    public static void addAppHeaderIfNotExist(AppHeader header) {
        if (Objects.isNull(header)) {
            return;
        }
        appHeaders.putIfAbsent(header.getName(), header);
    }


    /**
     *  Adding Security Header Config
     */
    static void addSecurityHeaderConfigsToAppHeadersIfNotExist(List<SecurityHeaderConfig> securityHeaderConfigs) {
        if (OpenApiDataUtils.isEmpty(securityHeaderConfigs)) {
            return;
        }
        addAppHeaderIfNotExist(ApiData.convertSecurityHeaderConfigsToAppHeaders(securityHeaderConfigs));
    }

    static void addAppHeaderIfNotExist(SecurityHeaderConfig securityHeaderConfig) {
        addAppHeaderIfNotExist(ApiData.convertSecurityHeaderConfigToAppHeader(securityHeaderConfig));
    }

    private static List<AppHeader> convertSecurityHeaderConfigsToAppHeaders(List<SecurityHeaderConfig> securityHeaderConfigs) {
        if (OpenApiDataUtils.isEmpty(securityHeaderConfigs)) {
            return Collections.emptyList();
        }
        return securityHeaderConfigs.stream().map(ApiData::convertSecurityHeaderConfigToAppHeader).collect(Collectors.toList());
    }

    private static AppHeader convertSecurityHeaderConfigToAppHeader(SecurityHeaderConfig securityHeaderConfig) {
        if (Objects.isNull(securityHeaderConfig)) {
            return null;
        }
        return AppHeader
                .builder()
                .name(securityHeaderConfig.getName())
                .required(false)
                .description(securityHeaderConfig.getDescription())
                .defaultValue(null)
                .build();
    }


    /**
     *  Adding Header Config
     */
    static void addHeaderConfigsToAppHeadersIfNotExist(List<HeaderConfig> headerConfigs) {
        if (OpenApiDataUtils.isEmpty(headerConfigs)) {
            return;
        }
        addAppHeaderIfNotExist(ApiData.convertHeaderConfigsToAppHeaders(headerConfigs));
    }

    static void addAppHeaderIfNotExist(HeaderConfig headerConfig) {
        addAppHeaderIfNotExist(ApiData.convertHeaderConfigToAppHeader(headerConfig));
    }

    private static List<AppHeader> convertHeaderConfigsToAppHeaders(List<HeaderConfig> headerConfigs) {
        if (OpenApiDataUtils.isEmpty(headerConfigs)) {
            return Collections.emptyList();
        }
        return headerConfigs.stream().map(ApiData::convertHeaderConfigToAppHeader).collect(Collectors.toList());
    }

    private static AppHeader convertHeaderConfigToAppHeader(HeaderConfig headerConfig) {
        if (Objects.isNull(headerConfig)) {
            return null;
        }
        return AppHeader
                .builder()
                .name(headerConfig.getName())
                .required(headerConfig.getRequired())
                .description(headerConfig.getDescription())
                .defaultValue(headerConfig.getDefaultValue())
                .build();
    }


    /**
     *  Adding Api Header
     */
    static void addApiHeadersToAppHeadersIfNotExist(List<ApiHeader> apiHeaders) {
        if (OpenApiDataUtils.isEmpty(apiHeaders)) {
            return;
        }
        addAppHeaderIfNotExist(ApiData.convertApiHeadersToAppHeaders(apiHeaders));
    }

    static void addAppHeaderIfNotExist(ApiHeader apiHeader) {
        addAppHeaderIfNotExist(ApiData.convertApiHeaderToAppHeader(apiHeader));
    }

    private static List<AppHeader> convertApiHeadersToAppHeaders(List<ApiHeader> apiHeaders) {
        if (OpenApiDataUtils.isEmpty(apiHeaders)) {
            return Collections.emptyList();
        }
        return apiHeaders.stream().map(ApiData::convertApiHeaderToAppHeader).collect(Collectors.toList());
    }

    private static AppHeader convertApiHeaderToAppHeader(ApiHeader apiHeader) {
        if (Objects.isNull(apiHeader)) {
            return null;
        }
        return AppHeader
                .builder()
                .name(apiHeader.getName())
                .required(apiHeader.isRequired())
                .description(apiHeader.getDescription())
                .defaultValue(apiHeader.getDefaultValue())
                .build();
    }

}