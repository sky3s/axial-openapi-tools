package com.axial.modules.openapi_tools.model.config;

public class HeaderConfig {

    private String name;

    private Boolean required;

    private String defaultValue;

    private String example;

    private String description;

    public HeaderConfig(String name, Boolean required, String defaultValue, String example, String description) {
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
        this.example = example;
        this.description = description;
    }

    private HeaderConfig(Builder builder) {
        name = builder.name;
        required = builder.required;
        defaultValue = builder.defaultValue;
        example = builder.example;
        description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String name;
        private Boolean required;
        private String defaultValue;
        private String example;
        private String description;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder required(Boolean val) {
            required = val;
            return this;
        }

        public Builder defaultValue(String val) {
            defaultValue = val;
            return this;
        }

        public Builder example(String val) {
            example = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public HeaderConfig build() {
            return new HeaderConfig(this);
        }
    }

    public String getName() {
        return name;
    }

    public Boolean getRequired() {
        return required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getExample() {
        return example;
    }

    public String getDescription() {
        return description;
    }
}
