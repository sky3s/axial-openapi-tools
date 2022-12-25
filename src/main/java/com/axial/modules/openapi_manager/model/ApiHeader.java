package com.axial.modules.openapi_manager.model;

/**
 * Created on December 2022
 */
public class ApiHeader {

    /**
     * Unique definition of header
     */
    private String key;

    /**
     * Key pass via Header, actual key of header
     */
    private String name;

    private String defaultValue;

    private boolean required;

    private String description;

    /**
     * Add this header as service header, default action
     */
    private boolean defaultApiHeader;

    /**
     * Add this header as security header, default action
     */
    private boolean defaultSecurityHeader;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDefaultApiHeader() {
        return defaultApiHeader;
    }

    public void setDefaultApiHeader(boolean defaultApiHeader) {
        this.defaultApiHeader = defaultApiHeader;
    }

    public boolean isDefaultSecurityHeader() {
        return defaultSecurityHeader;
    }

    public void setDefaultSecurityHeader(boolean defaultSecurityHeader) {
        this.defaultSecurityHeader = defaultSecurityHeader;
    }
}
