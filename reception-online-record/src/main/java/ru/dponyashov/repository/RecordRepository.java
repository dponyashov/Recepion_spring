package ru.dponyashov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dponyashov.entity.RecordEntity;
import ru.dponyashov.enums.RecordStatus;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    @Query(name = "Record.findByFilter", nativeQuery = true)
    List<RecordEntity> findByFilter(
            @Param("phone") String phone,
            @Param("name") String name,
            @Param("status") RecordStatus status
    );
}
