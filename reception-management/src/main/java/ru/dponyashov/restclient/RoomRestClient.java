package ru.dponyashov.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.dponyashov.dto.RoomDto;
import ru.dponyashov.exception.BadRequestException;
import ru.dponyashov.service.RoomService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RoomRestClient implements RoomService {

    private static final ParameterizedTypeReference<List<RoomDto>> ROOM_TYPE_REFERENCE =
            new ParameterizedTypeReference<>(){};

    private final RestClient roomRestClient;

    @Override
    public List<RoomDto> findAllWithFilter(String filterNumber) {
        return roomRestClient
                .get()
                .uri("/api/room?number={filterNumber}", filterNumber)
                .retrieve()
                .body(ROOM_TYPE_REFERENCE);
    }

    @Override
    public RoomDto createRoom(String number, String note) {
        try {
            return roomRestClient
                    .post()
                    .uri("/api/room")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new RoomDto(null, number, note))
                    .retrieve()
                    .body(RoomDto.class);
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateRoom(Long roomId, String number, String note) {
        try {
            roomRestClient
                    .put()
                    .uri("/api/room/{roomId}", roomId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new RoomDto(roomId, number, note))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteMaster(Long roomId) {
        try {
            roomRestClient
                    .delete()
                    .uri("/api/room/{roomId}", roomId)
                    .retrieve()
                    .toBodilessEntity();
        } catch(HttpClientErrorException.NotFound exception){
            throw new NoSuchElementException(exception);
        }
    }

    @Override
    public Optional<RoomDto> findRoomById(Long roomId) {
        try {
            return Optional.ofNullable(roomRestClient
                    .get()
                    .uri("/api/room//{roomId}", roomId)
                    .retrieve()
                    .body(RoomDto.class)
            );
        } catch(HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }
}
