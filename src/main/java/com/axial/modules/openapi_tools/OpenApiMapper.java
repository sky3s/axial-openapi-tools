package com.axial.modules.openapi_tools;

import com.axial.modules.openapi_tools.model.ApiHeader;
import com.axial.modules.openapi_tools.model.config.SecurityHeaderConfig;

import java.util.*;

class OpenApiMapper {

    private OpenApiMapper() { }

    static SecurityHeaderConfig convertApiHeaderToSecurityHeaderConfig(ApiHeader apiHeader) {
        if (Objects.isNull(apiHeader)) {
            return null;
        }
        return SecurityHeaderConfig
                .builder()
                .key(apiHeader.getKey())
                .name(apiHeader.getName())
                .example(apiHeader.getDefaultValue())
                .description(apiHeader.getDescription())
                .build();
    }

}
