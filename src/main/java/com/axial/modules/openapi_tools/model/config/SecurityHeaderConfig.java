package com.axial.modules.openapi_tools.model.config;

public class SecurityHeaderConfig {

    private String key;

    private String name;

    private String example;

    private String description;

    public SecurityHeaderConfig(String key, String name, String example, String description) {
        this.key = key;
        this.name = name;
        this.example = example;
        this.description = description;
    }

    private SecurityHeaderConfig(Builder builder) {
        key = builder.key;
        name = builder.name;
        example = builder.example;
        description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String key;
        private String name;
        private String example;
        private String description;

        private Builder() {
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
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

        public SecurityHeaderConfig build() {
            return new SecurityHeaderConfig(this);
        }
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getExample() {
        return example;
    }

    public String getDescription() {
        return description;
    }
}