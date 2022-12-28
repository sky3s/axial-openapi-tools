package com.axial.modules.openapi_manager;

import com.axial.modules.openapi_manager.model.ApiHeader;
import com.axial.modules.openapi_manager.model.config.SecurityHeaderConfig;

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
