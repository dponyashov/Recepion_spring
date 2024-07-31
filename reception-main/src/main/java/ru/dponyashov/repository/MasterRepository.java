package ru.dponyashov.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dponyashov.entity.Client;
import ru.dponyashov.entity.Master;

import java.util.Arrays;
import java.util.List;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {
    List<Master> findByName(String name);

    @Query(name = "Master.findByFilter", nativeQuery = true)
    List<Master> findWithFilter(
                                @Param("masterName") String masterName,
                                @Param("masterPhone") String masterPhone,
                                Pageable pageable);
}