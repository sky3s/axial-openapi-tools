package com.axial.modules.openapi_tools;

import com.axial.modules.openapi_tools.model.config.ApiConfig;
import com.axial.modules.openapi_tools.model.config.ApplicationApiConfig;
import jakarta.annotation.PostConstruct;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@PropertySource("classpath:default-springdoc-config.properties")
public class SpringDocInitializer {

    private final ConfigurableListableBeanFactory beanFactory;

    private final ApiCustomizer apiCustomizer;

    private final ApplicationApiConfig applicationApiConfig;

    private final SpringDocCustomizerActions customizerActions;

    public SpringDocInitializer(ConfigurableListableBeanFactory beanFactory,
                                ApiCustomizer apiCustomizer,
                                ApplicationApiConfig applicationApiConfig,
                                SpringDocCustomizerActions customizerActions) {
        this.beanFactory = beanFactory;
        this.apiCustomizer = apiCustomizer;
        this.applicationApiConfig = applicationApiConfig;
        this.customizerActions = customizerActions;
    }


    @PostConstruct
    private void initOps() {
        createApiData();
        addCustomizers();
    }

    private void createApiData() {

        /**
         * Adding default headers to ApiData
         */
        ApiData.addApiHeadersToAppHeadersIfNotExist(apiCustomizer.getHeaders());
        ApiData.addApiHeadersToAppHeadersIfNotExist(apiCustomizer.getApiHeaders());
        ApiData.addSecurityHeaderConfigsToAppHeadersIfNotExist(apiCustomizer.getSecurityHeaders());

        /**
         * Adding default YML Headers to ApiData
         */
        ApiData.addHeaderConfigsToAppHeadersIfNotExist(
                OpenApiDataUtils.emptyIfNull(applicationApiConfig.getCommonHeaders()).values().stream().toList());
        ApiData.addSecurityHeaderConfigsToAppHeadersIfNotExist(
                OpenApiDataUtils.emptyIfNull(applicationApiConfig.getCommonSecurityHeaders()).values().stream().toList());

        /**
         * Adding API Specific YML Headers to ApiData
         */
        ApiData.addHeaderConfigsToAppHeadersIfNotExist(
                applicationApiConfig.getApis().values().stream()
                        .flatMap(api ->
                                OpenApiDataUtils.emptyIfNull(api.getHeaders()).values().stream())
                        .collect(Collectors.toList()));
        ApiData.addSecurityHeaderConfigsToAppHeadersIfNotExist(
                applicationApiConfig.getApis().values().stream()
                        .flatMap(api ->
                                OpenApiDataUtils.emptyIfNull(api.getSecurityHeaders()).values().stream())
                        .collect(Collectors.toList()));
    }

    private void addCustomizers() {

        final Map<String, ApiConfig> apiMap = applicationApiConfig
                .getApis()
                .values()
                .stream()
                .collect(Collectors.toMap(ApiConfig::getName, Function.identity()));

        OpenApiDataUtils
                .emptyIfNull(beanFactory
                        .getBeansOfType(GroupedOpenApi.class))
                .values().
                forEach(groupedOpenApi ->
                        groupedOpenApi
                                .getOpenApiCustomizers()
                                .add(openApi ->
                                        customizerActions.customizeOpenAPI(openApi, apiMap.get(groupedOpenApi.getGroup())))
                );

    }

}
