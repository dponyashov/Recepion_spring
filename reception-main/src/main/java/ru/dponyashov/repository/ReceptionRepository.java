package ru.dponyashov.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dponyashov.entity.Reception;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    @Query(name = "Reception.findByFilter", nativeQuery = true)
    List<Reception> findWithFilter(
            @Param("clientName") String clientName,
            @Param("masterName") String masterName,
            @Param("roomNumber") String roomNumber,
            @Param("dateOfVisit") LocalDate dateOfVisit,
            @Param("idClient") Long idClient,
            @Param("idMaster") Long idMaster,
            @Param("idRoom") Long idRoom,
            Pageable pageable);
}
