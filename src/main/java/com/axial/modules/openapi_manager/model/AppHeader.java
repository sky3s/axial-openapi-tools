package com.axial.modules.openapi_manager.model;

public class AppHeader {

    /**
     * Key pass via Header, actual key of header
     */
    private String name;

    private String defaultValue;

    private boolean required;

    private String description;

    private AppHeader(Builder builder) {
        name = builder.name;
        defaultValue = builder.defaultValue;
        required = builder.required;
        description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String name;
        private String defaultValue;
        private boolean required;
        private String description;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder defaultValue(String val) {
            defaultValue = val;
            return this;
        }

        public Builder required(boolean val) {
            required = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public AppHeader build() {
            return new AppHeader(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }
}
