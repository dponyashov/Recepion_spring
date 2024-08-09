package ru.dponyashov.configaration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import ru.dponyashov.restclient.ClientRestClient;
import ru.dponyashov.restclient.MasterRestClient;
import ru.dponyashov.restclient.RoomRestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RoomRestClient roomRestClient(
            @Value("${reception.services.main.uri:http://localhost:8080}") String registrationBaseUri,
            @Value("${reception.services.main.username}") String username,
            @Value("${reception.services.main.password}") String password){
        return new RoomRestClient(RestClient.builder()
                .baseUrl(registrationBaseUri)
                .requestInterceptor(new BasicAuthenticationInterceptor(username, password))
                .build());
    }

    @Bean
    public MasterRestClient masterRestClient(
            @Value("${reception.services.main.uri:http://localhost:8080}") String registrationBaseUri,
            @Value("${reception.services.main.username}") String username,
            @Value("${reception.services.main.password}") String password){
        return new MasterRestClient(RestClient.builder()
                .baseUrl(registrationBaseUri)
                .requestInterceptor(new BasicAuthenticationInterceptor(username, password))
                .build());
    }

    @Bean
    public ClientRestClient clientRestClient(
            @Value("${reception.services.main.uri:http://localhost:8080}") String registrationBaseUri,
            @Value("${reception.services.main.username}") String username,
            @Value("${reception.services.main.password}") String password){
        return new ClientRestClient(RestClient.builder()
                .baseUrl(registrationBaseUri)
                .requestInterceptor(new BasicAuthenticationInterceptor(username, password))
                .build());
    }
}
