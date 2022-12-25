package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.ApiHeader;
import com.axial.modules.openapi_manager.model.config.SecurityHeaderConfig;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This interface can be using for defining or modifying default headers,
 * also can be using in your project modules such as security modules.
 */
public interface ApiCustomizer {

    default List<ApiHeader> getDefaultHeaders() {

        final List<ApiHeader> apiHeaders = new ArrayList<>();

        ApiHeader apiHeader = new ApiHeader();
        apiHeader.setKey("XForwardedFor");
        apiHeader.setName("X-FORWARDED-FOR");
        apiHeader.setDefaultValue("0.0.0.0");
        apiHeader.setRequired(false);
        apiHeader.setDescription("Redirect IP address");
        apiHeader.setDefaultApiHeader(false);
        apiHeader.setDefaultSecurityHeader(false);
        apiHeaders.add(apiHeader);

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
