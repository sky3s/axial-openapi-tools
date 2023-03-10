package com.axial.modules.openapi_tools;

import com.axial.modules.openapi_tools.model.ApiHeader;
import com.axial.modules.openapi_tools.model.config.SecurityHeaderConfig;

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
        return OpenApiDataUtils.emptyIfNull(getHeaders())
                .stream().filter(ApiHeader::isDefaultApiHeader).collect(Collectors.toUnmodifiableList());
    }

    default List<SecurityHeaderConfig> getSecurityHeaders() {
        return OpenApiDataUtils.emptyIfNull(getHeaders()).stream()
                .filter(ApiHeader::isDefaultSecurityHeader)
                .map(OpenApiMapper::convertApiHeaderToSecurityHeaderConfig)
                .collect(Collectors.toUnmodifiableList());
    }

}
