package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.ApiHeader;
import com.axial.modules.openapi_manager.model.config.*;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created on December 2022
 */
//@Configuration
@Component
public class SpringDocCustomizerActions {

    private static final String HDR_PREFIX = "hdr";

    private static final String COMPONENTS_PREFIX = "#/components/parameters/" + HDR_PREFIX;

    private final ApplicationApiConfig applicationApiConfig;

    private final ApiCustomizer apiCustomizer;

    // private static int apiIndex = 0;

    public SpringDocCustomizerActions(ApplicationApiConfig applicationApiConfig, ApiCustomizer apiCustomizer) {
        this.applicationApiConfig = applicationApiConfig;
        this.apiCustomizer = apiCustomizer;
    }

    @PostConstruct
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

    public void customizeOpenAPI(OpenAPI openAPI, ApiConfig apiConfig) {

        addAppGeneralDetails(openAPI);
        addApiSpecificDetails(openAPI, apiConfig);
        addHeaders(openAPI, apiConfig);
    }

    private void addAppGeneralDetails(OpenAPI openAPI) {

        final List<Server> servers = OpenApiUtils.emptyIfNull(applicationApiConfig.getDomains())
                .stream()
                .map(domain -> {
                    final Server server = new Server();
                    server.setUrl(domain);
                    return server;
                }).collect(Collectors.toList());

        openAPI.servers(servers);
    }

    private void addApiSpecificDetails(OpenAPI openAPI, ApiConfig apiConfig) {

        openAPI.info(new Info()
                .title(apiConfig.getGroupName() + " - " + applicationApiConfig.getName())
                .version(applicationApiConfig.getVersion())
                .description(apiConfig.getDescription()));
    }

    private void addHeaders(OpenAPI openAPI, ApiConfig apiConfig) {

        /**
         * Api Headers
         */
        final Map<String, HeaderConfig> apiHeaderMap = new HashMap<>();
        apiHeaderMap.putAll(mapDefaultApiHeadersToHeaderConfig());
        apiHeaderMap.putAll(OpenApiUtils.emptyIfNull(applicationApiConfig.getCommonHeaders()));
        apiHeaderMap.putAll(OpenApiUtils.emptyIfNull(apiConfig.getHeaders()));

        addComponentsToApiDefinition(openAPI, createHeaderComponents(apiHeaderMap.values().stream().toList()));
        final List<PathItem> pathItems = getAllApisOfDefinition(openAPI, apiConfig);
        pathItems.forEach(pathItem -> addHeaderToPathItem(apiHeaderMap.values().stream().toList(), pathItem));

        /**
         * Security Schema Headers
         */
        final Map<String, SecurityHeaderConfig> securityHeaderMap = new HashMap<>();
        securityHeaderMap.putAll(mapDefaultSecurityHeadersToSecurityHeaderConfig());
        securityHeaderMap.putAll(OpenApiUtils.emptyIfNull(applicationApiConfig.getCommonSecurityHeaders()));
        securityHeaderMap.putAll(OpenApiUtils.emptyIfNull(apiConfig.getSecurityHeaders()));

        addComponentsToApiDefinition(openAPI, createSecurityHeaderComponents(securityHeaderMap.values().stream().toList()));
        addSecurityHeadersToDefinition(openAPI, securityHeaderMap.values().stream().toList());

    }

    private void addComponentsToApiDefinition(OpenAPI openAPI, Components components) {

        if (OpenApiUtils.isNotEmpty(components.getParameters())) {
            components.getParameters().forEach((key, parameter) -> openAPI.getComponents().addParameters(key, parameter));
        }

        if (OpenApiUtils.isNotEmpty(components.getSecuritySchemes())) {
            components.getSecuritySchemes().forEach((key, schema) -> openAPI.getComponents().addSecuritySchemes(key, schema));
        }
    }

    private Components createHeaderComponents(List<HeaderConfig> headers) {

        final Components components = new Components();

        if (OpenApiUtils.isNotEmpty(headers)) {
            headers.forEach(header ->
                    components.addParameters(HDR_PREFIX + header.getName(),
                            new HeaderParameter()
                                    .required(header.getRequired())
                                    .name(header.getName())
                                    .example(header.getExample())
                                    .description(header.getDescription())
                                    .schema(new StringSchema())
                    )
            );
        }

        return components;
    }

    private Components createSecurityHeaderComponents(List<SecurityHeaderConfig> securityHeaders) {

        final Components components = new Components();

        if (OpenApiUtils.isNotEmpty(securityHeaders)) {
            securityHeaders.forEach(securityHeader ->
                    components.addSecuritySchemes(securityHeader.getKey(),
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.APIKEY)
                                    .in(SecurityScheme.In.HEADER)
                                    .name(securityHeader.getName())
                                    .description(securityHeader.getDescription())
                    )
            );
        }

        return components;
    }

    private void addSecurityHeadersToDefinition(OpenAPI openAPI, List<SecurityHeaderConfig> securityHeaders) {

        final SecurityRequirement securityRequirement = new SecurityRequirement();

        securityHeaders.forEach(securityHeader -> securityRequirement.addList(securityHeader.getKey()));

        if (Objects.isNull(openAPI.getSecurity())) {
            final List<SecurityRequirement> list = new ArrayList<>();
            list.add(securityRequirement);
            openAPI.security(list);
        } else {
            openAPI.getSecurity().add(securityRequirement);
        }
    }

    private void addHeaderToPathItem(List<HeaderConfig> headers, PathItem pathItem) {

        pathItem.readOperations().forEach(operation -> {
            if (OpenApiUtils.isNotEmpty(headers)) {
                headers.forEach(header -> {
                    final String componentRef = COMPONENTS_PREFIX + header.getName();
                    /*
                     This header will be ignored if it already exists. When an existing header is added again, it is duplicated.
                     We do this because the headers are multiplexed when we change the definition.
                     */
                    if (Optional.ofNullable(operation.getParameters())
                            .orElse(new ArrayList<>())
                            .stream()
                            .anyMatch(op ->
                                    StringUtils.equals(header.getName(), op.getName()))) {
                        return;
                    }

                    /**
                     * New headers will be added.
                     */
                    operation.addParametersItem(
                            new HeaderParameter()
                                    .$ref(componentRef)
                                    .name(header.getName())
                                    .description(header.getDescription())
                                    .example(header.getExample())
                                    .required(header.getRequired())
                    );
                });
            }
        });
    }

    private List<PathItem> getAllApisOfDefinition(OpenAPI openApi, ApiConfig apiConfig) {

        final String apiUrlPrefix = apiConfig.getPath().replace("*", "");
        final List<PathItem> pathItems = new ArrayList<>();

        /**
         * Prepare header - api map for API specific header assignment
         */
        openApi.getPaths().forEach((openApiPathItemUrl, pathItem) -> {
            if (openApiPathItemUrl.startsWith(apiUrlPrefix)) {
                pathItems.add(pathItem);
            }
        });

        return pathItems;
    }

    private Map<String, HeaderConfig> mapDefaultApiHeadersToHeaderConfig() {
        return OpenApiUtils.emptyIfNull(apiCustomizer.getApiHeaders())
                .stream().map(SpringDocCustomizerActions::convertApiHeaderToHeaderConfig)
                .collect(Collectors.toMap(HeaderConfig::getName, Function.identity()));
    }

    private Map<String, SecurityHeaderConfig> mapDefaultSecurityHeadersToSecurityHeaderConfig() {
        return OpenApiUtils.emptyIfNull(apiCustomizer.getSecurityHeaders())
                .stream().collect(Collectors.toMap(SecurityHeaderConfig::getName, Function.identity()));
    }

    private static HeaderConfig convertApiHeaderToHeaderConfig(ApiHeader apiHeader) {
        if (Objects.isNull(apiHeader)) {
            return null;
        }
        final HeaderConfig headerConfig = new HeaderConfig();
        headerConfig.setName(apiHeader.getName());
        headerConfig.setRequired(apiHeader.isRequired());
        headerConfig.setDescription(apiHeader.getDescription());
        headerConfig.setDefaultValue(apiHeader.getDefaultValue());
        headerConfig.setExample(apiHeader.getDefaultValue());
        return headerConfig;
    }

}
