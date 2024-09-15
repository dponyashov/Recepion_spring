package ru.dponyashov.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.dponyashov.dto.RecordDto;
import ru.dponyashov.enums.RecordStatus;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.exception.NotFoundDirectoryElementException;
import ru.dponyashov.service.OnlineRecordService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OnlineRecordRestClient implements OnlineRecordService {

    private static final ParameterizedTypeReference<List<RecordDto>> RECORD_TYPE_REFERENCE =
            new ParameterizedTypeReference<>(){};

    private final RestClient recordRestClient;

    @Override
    public List<RecordDto> findNewRecordWithFilter(String filterName, String filterPhone) {
        return recordRestClient
                .get()
                .uri("/onlineRecord/list?name={filterName}&phone={filterName}&status=NEW",
                        filterName, filterPhone)
                .retrieve()
                .body(RECORD_TYPE_REFERENCE);
    }

    @Override
    public Optional<RecordDto> findRecordById(Long recordId) {
        try {
            return Optional.ofNullable(recordRestClient
                    .get()
                    .uri("/onlineRecord/{recordId}", recordId)
                    .retrieve()
                    .body(RecordDto.class)
            );
        } catch(HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }

    @Override
    public void changeStatusForRecord(Long recordId, RecordStatus Status) {
        RecordDto record = findRecordById(recordId).orElseThrow(
                () -> new NotFoundDirectoryElementException("Запись не найдена")
        );
        try {
            recordRestClient
                    .put()
                    .uri("/onlineRecord/{recordId}", recordId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new RecordDto(record.id(), record.phone(), record.name(), record.note(), Status.name()))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }
}
