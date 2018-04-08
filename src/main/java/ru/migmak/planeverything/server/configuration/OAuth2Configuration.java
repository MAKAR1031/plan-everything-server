package ru.migmak.planeverything.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableAuthorizationServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
    private static final String[] AUTHORIZED_GRANT_TYPES = {"password", "authorization_code", "refresh_token"};
    private static final String SCOPE = "all";

    @Value("${oauth.resourceId}")
    private String resourceId;
    @Value("${oauth.client.web.id}")
    private String clientId;
    @Value("${oauth.client.web.secret}")
    private String clientSecret;

    private final AuthenticationManagerBuilder authenticationManager;

    @Autowired
    public OAuth2Configuration(AuthenticationManagerBuilder authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authentication ->
                authenticationManager.getOrBuild().authenticate(authentication));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
               .withClient(clientId)
               .secret(clientSecret)
               .resourceIds(resourceId)
               .authorizedGrantTypes(AUTHORIZED_GRANT_TYPES)
               .scopes(SCOPE);
    }
}
