package ru.dponyashov.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.NotificationDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.ClientService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class ClientRestClient implements ClientService {

    private static final ParameterizedTypeReference<List<ClientDto>> CLIENT_TYPE_REFERENCE =
            new ParameterizedTypeReference<>(){};
    private static final ParameterizedTypeReference<List<NotificationDto>> NOTIFY_TYPE_REFERENCE =
            new ParameterizedTypeReference<>(){};

    private final RestClient clientRestClient;

    @Override
    public List<ClientDto> findAllWithFilter(String filterName, String filterPhone) {
        return clientRestClient
                .get()
                .uri("/api/client?clientName={filterName}&clientPhone={filterPhone}",
                        filterName, filterPhone)
                .retrieve()
                .body(CLIENT_TYPE_REFERENCE);
    }

    @Override
    public ClientDto createClient(String name, String phone, String mail, List<NotificationDto> notifications) {
        try {
            return clientRestClient
                    .post()
                    .uri("/api/client")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ClientDto(null, name, phone, mail, notifications))
                    .retrieve()
                    .body(ClientDto.class);
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<ClientDto> findClientById(Long clientId) {
        try {
            return Optional.ofNullable(clientRestClient
                    .get()
                    .uri("/api/client/{clientId}", clientId)
                    .retrieve()
                    .body(ClientDto.class)
            );
        } catch(HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }

    @Override
    public void updateClient(Long clientId, String name, String phone, String mail,
                             List<NotificationDto> notifications) {
        try {
            clientRestClient
                    .put()
                    .uri("/api/client/{clientId}", clientId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ClientDto(clientId, name, phone, mail, notifications))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteClient(Long clientId) {
        try {
            clientRestClient
                    .delete()
                    .uri("/api/client/{clientId}", clientId)
                    .retrieve()
                    .toBodilessEntity();
        } catch(HttpClientErrorException.NotFound exception){
            throw new NoSuchElementException(exception);
        }
    }

    @Override
    public List<NotificationDto> notificationList() {
        return clientRestClient
                .get()
                .uri("/api/notification")
                .retrieve()
                .body(NOTIFY_TYPE_REFERENCE);
    }

    @Override
    public Optional<NotificationDto> findNotifyById(Long notifyId) {
        try {
            return Optional.ofNullable(clientRestClient
                    .get()
                    .uri("/api/notification/{notifyId}", notifyId)
                    .retrieve()
                    .body(NotificationDto.class)
            );
        } catch(HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRestClient
                .get()
                .uri("/api/client")
                .retrieve()
                .body(CLIENT_TYPE_REFERENCE);
    }
}
