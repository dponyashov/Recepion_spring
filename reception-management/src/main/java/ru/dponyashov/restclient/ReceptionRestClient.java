package ru.dponyashov.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.dponyashov.dto.ClientDto;
import ru.dponyashov.dto.MasterDto;
import ru.dponyashov.dto.ReceptionDto;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.ReceptionService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class ReceptionRestClient implements ReceptionService {

    private static final ParameterizedTypeReference<List<ReceptionDto>> RECEPTION_TYPE_REFERENCE =
            new ParameterizedTypeReference<>(){};

    private final RestClient receptionRestClient;

    @Override
    public List<ReceptionDto> findAllWithFilter(String masterName, String clientName,
                                                String roomNumber, LocalDate dateOfVisit) {
        return receptionRestClient
                .get()
                .uri("/api/reception?masterName={masterName}&clientName={clientName}&roomNumber={roomNumber}&dateOfVisit={dateOfVisit}",
                        masterName, clientName, roomNumber,
                        dateOfVisit != null
                                ? dateOfVisit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                : ""
                )
                .retrieve()
                .body(RECEPTION_TYPE_REFERENCE);
    }

    @Override
    public ReceptionDto createReception(MasterDto master, RoomDto room, ClientDto client,
                                        LocalDate dateOfVisit, LocalTime startTime, LocalTime finishTime,
                                        String details) {
        try {
            return receptionRestClient
                    .post()
                    .uri("/api/reception")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ReceptionDto(null, master, room, client,
                            dateOfVisit, startTime, finishTime,
                            details))
                    .retrieve()
                    .body(ReceptionDto.class);
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public List<ReceptionDto> findAll() {
        return receptionRestClient
                .get()
                .uri("/api/reception")
                .retrieve()
                .body(RECEPTION_TYPE_REFERENCE);
    }

    @Override
    public Optional<ReceptionDto> findReceptionById(Long receptionId) {
        try {
            return Optional.ofNullable(receptionRestClient
                    .get()
                    .uri("/api/reception/{receptionId}", receptionId)
                    .retrieve()
                    .body(ReceptionDto.class)
            );
        } catch(HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }

    @Override
    public void deleteReception(Long receptionId) {
        try {
            receptionRestClient
                    .delete()
                    .uri("/api/reception/{receptionId}", receptionId)
                    .retrieve()
                    .toBodilessEntity();
        } catch(HttpClientErrorException.NotFound exception){
            throw new NoSuchElementException(exception);
        }
    }

    @Override
    public void updateReception(Long receptionId, MasterDto master, RoomDto room, ClientDto client,
                                LocalDate dateOfVisit, LocalTime startTime, LocalTime finishTime, String details) {
        try {
            receptionRestClient
                    .put()
                    .uri("/api/reception/{receptionId}", receptionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ReceptionDto(receptionId, master, room, client, dateOfVisit, startTime, finishTime,
                            details))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }
}
