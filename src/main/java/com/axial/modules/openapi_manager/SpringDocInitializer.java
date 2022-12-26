package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.config.ApiConfig;
import com.axial.modules.openapi_manager.model.config.ApplicationApiConfig;
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
                OpenApiUtils.emptyIfNull(applicationApiConfig.getCommonHeaders()).values().stream().toList());
        ApiData.addSecurityHeaderConfigsToAppHeadersIfNotExist(
                OpenApiUtils.emptyIfNull(applicationApiConfig.getCommonSecurityHeaders()).values().stream().toList());

        /**
         * Adding API Specific YML Headers to ApiData
         */
        ApiData.addHeaderConfigsToAppHeadersIfNotExist(
                applicationApiConfig.getApis().values().stream()
                        .flatMap(api ->
                                OpenApiUtils.emptyIfNull(api.getHeaders()).values().stream())
                        .collect(Collectors.toList()));
        ApiData.addSecurityHeaderConfigsToAppHeadersIfNotExist(
                applicationApiConfig.getApis().values().stream()
                        .flatMap(api ->
                                OpenApiUtils.emptyIfNull(api.getSecurityHeaders()).values().stream())
                        .collect(Collectors.toList()));
    }

    private void addCustomizers() {

        //GroupedOpenApi groupedOpenApi = (GroupedOpenApi) beanFactory.getBeanDefinition("BasicAPI");

        final Map<String, ApiConfig> apiMap = applicationApiConfig
                .getApis()
                .values()
                .stream()
                .collect(Collectors.toMap(ApiConfig::getName, Function.identity()));

        beanFactory
                .getBeansOfType(GroupedOpenApi.class)
                .values().
                forEach(groupedOpenApi ->
                        groupedOpenApi
                                .getOpenApiCustomizers()
                                .add(openApi ->
                                        customizerActions.customizeOpenAPI(openApi, apiMap.get(groupedOpenApi.getGroup())))
                );

    }

}
