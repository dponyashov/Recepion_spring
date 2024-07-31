package ru.dponyashov.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dponyashov.entity.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByName(String name);

    @Query(name = "Client.findByFilter", nativeQuery = true)
    List<Client> findWithFilter(@Param("clientName") String clientName,
                                @Param("clientPhone") String clientPhone,
                                Pageable pageable);
}
