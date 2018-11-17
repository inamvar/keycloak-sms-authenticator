package com.berkaybayraktar.keycloak.authenticator;

import com.berkaybayraktar.gateway.Gateways;
import com.berkaybayraktar.keycloak.KeycloakSmsConstants;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SMS validation Input Created by joris on 11/11/2016.
 */
public class KeycloakSmsAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "sms-authentication";

    private static Logger logger = Logger.getLogger(KeycloakSmsAuthenticatorFactory.class);
    private static final KeycloakSmsAuthenticator SINGLETON = new KeycloakSmsAuthenticator();

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.OPTIONAL,
            AuthenticationExecutionModel.Requirement.DISABLED };

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        ProviderConfigProperty property;

        // SMS Code
        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.CONF_PRP_SMS_CODE_TTL);
        property.setLabel("SMS code time to live");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("The validity of the sent code in seconds.");
        property.setDefaultValue(60 * 5);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.CONF_PRP_SMS_CODE_LENGTH);
        property.setLabel("Length of the SMS code");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Length of the SMS code.");
        property.setDefaultValue(6);
        configProperties.add(property);

        // SMS gateway
        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.CONF_PRP_SMS_GATEWAY);
        property.setLabel("SMS gateway");
        property.setHelpText("Select SMS gateway");
        property.setType(ProviderConfigProperty.LIST_TYPE);
        property.setDefaultValue(Gateways.NIK_SMS);
        property.setOptions(Stream.of(Gateways.values()).map(Enum::name).collect(Collectors.toList()));
        configProperties.add(property);

        // SMS Line Number
        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.CONF_PRP_SMS_LINENUMBER);
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setLabel("SMS Line Number");
        //property.setHelpText("Enable access and ask for mobile number if it isn't defined");
        configProperties.add(property);

        // Credential
        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.CONF_PRP_SMS_CLIENTTOKEN);
        property.setLabel("Username");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("SMS Gateway Username");
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.CONF_PRP_SMS_CLIENTSECRET);
        property.setLabel("Password");
        property.setHelpText("SMS Gateway Password");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);

        // First time verification
        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.MOBILE_VERIFICATION_ENABLED);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        property.setLabel("Verify mobilephone\nnumber ONLY");
        property.setHelpText("Send SMS code ONLY to verify mobile number (add or update)");
        configProperties.add(property);

        // Ask for mobile if not defined
        property = new ProviderConfigProperty();
        property.setName(KeycloakSmsConstants.MOBILE_ASKFOR_ENABLED);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        property.setLabel("Ask for mobile number");
        property.setHelpText("Enable access and ask for mobile number if it isn't defined");
        configProperties.add(property);



    }

    public String getId() {
        logger.debug("getId called ... returning " + PROVIDER_ID);
        return PROVIDER_ID;
    }

    public Authenticator create(KeycloakSession session) {
        logger.debug("create called ... returning " + SINGLETON);
        return SINGLETON;
    }

    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        logger.debug("getRequirementChoices called ... returning " + Arrays.toString(REQUIREMENT_CHOICES));
        return REQUIREMENT_CHOICES;
    }

    public boolean isUserSetupAllowed() {
        logger.debug("isUserSetupAllowed called ... returning true");
        return true;
    }

    public boolean isConfigurable() {
        logger.debug("isConfigurable called ... returning true");
        return true;
    }

    public String getHelpText() {
        logger.debug("getHelpText called ...");
        return "Validates an OTP sent by SMS.";
    }

    public String getDisplayType() {
        String result = "SMS Authentication";
        logger.debug("getDisplayType called ... returning " + result);
        return result;
    }

    public String getReferenceCategory() {
        logger.debug("getReferenceCategory called ... returning sms-auth-code");
        return "sms-auth-code";
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        logger.debug("getConfigProperties called ... returning " + configProperties);
        return configProperties;
    }

    public void init(Config.Scope config) {
        logger.debug("init called ... config.scope = " + config);
    }

    public void postInit(KeycloakSessionFactory factory) {
        logger.debug("postInit called ... factory = " + factory);
    }

    public void close() {
        logger.debug("close called ...");
    }
}
