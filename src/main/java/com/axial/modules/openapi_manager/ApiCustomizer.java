package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.ApiHeader;
import com.axial.modules.openapi_manager.model.config.SecurityHeaderConfig;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This interface can be using for defining or modifying default headers,
 * also can be using in your project modules such as security modules.
 */
public interface ApiCustomizer {

    default List<ApiHeader> getDefaultHeaders() {

        final List<ApiHeader> apiHeaders = Arrays.asList(
                ApiHeader
                        .builder()
                        .key("XForwardedFor")
                        .name("X-FORWARDED-FOR")
                        .defaultValue("0.0.0.0")
                        .required(false)
                        .description("Redirect IP address")
                        .defaultApiHeader(false)
                        .defaultSecurityHeader(false)
                        .build()
        );

        return Collections.unmodifiableList(apiHeaders);
    }

    default List<ApiHeader> getHeaders() {
        return new ArrayList<>(getDefaultHeaders());
    }

    default List<ApiHeader> getApiHeaders() {
        return OpenApiUtils.emptyIfNull(getHeaders())
                .stream().filter(ApiHeader::isDefaultApiHeader).collect(Collectors.toUnmodifiableList());
    }

    default List<SecurityHeaderConfig> getSecurityHeaders() {
        return OpenApiUtils.emptyIfNull(getHeaders()).stream()
                .filter(ApiHeader::isDefaultSecurityHeader)
                .map(OpenApiUtils::convertApiHeaderToSecurityHeaderConfig)
                .collect(Collectors.toUnmodifiableList());
    }

}
