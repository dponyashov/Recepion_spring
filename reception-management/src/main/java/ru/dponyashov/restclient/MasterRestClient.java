package ru.dponyashov.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.MasterService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class MasterRestClient implements MasterService {
    private static final ParameterizedTypeReference<List<MasterDto>> MASTER_TYPE_REFERENCE =
            new ParameterizedTypeReference<>(){};

    private final RestClient masterRestClient;

    @Override
    public List<MasterDto> findAllWithFilter(String filterName, String filterPhone) {
        return masterRestClient
                .get()
                .uri("/api/master?masterName={filterName}&masterPhone={filterPhone}", filterName, filterPhone)
                .retrieve()
                .body(MASTER_TYPE_REFERENCE);
    }

    @Override
    public MasterDto createMaster(String name, String phone) {
        try {
            return masterRestClient
                    .post()
                    .uri("/api/master")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MasterDto(null, name, phone))
                    .retrieve()
                    .body(MasterDto.class);
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateMaster(Long masterId, String name, String phone) {
        try {
            masterRestClient
                    .put()
                    .uri("/api/master/{masterId}", masterId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MasterDto(masterId, name, phone))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteMaster(Long masterId) {
        try {
            masterRestClient
                    .delete()
                    .uri("/api/master/{masterId}", masterId)
                    .retrieve()
                    .toBodilessEntity();
        } catch(HttpClientErrorException.NotFound exception){
            throw new NoSuchElementException(exception);
        }
    }

    @Override
    public Optional<MasterDto> findMasterById(Long masterId) {
        try {
            return Optional.ofNullable(masterRestClient
                    .get()
                    .uri("/api/master/{masterId}", masterId)
                    .retrieve()
                    .body(MasterDto.class)
            );
        } catch(HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }

    @Override
    public List<MasterDto> findAll() {
        return masterRestClient
                .get()
                .uri("/api/master")
                .retrieve()
                .body(MASTER_TYPE_REFERENCE);
    }
}
