package ru.dponyashov.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="t_reception", schema = "main_schema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedQueries(
        @NamedQuery(name = "Reception.findByFilter",
            query = """
                select r from Reception r
                    join r.client client
                    join r.master master
                    join r.room room
                where (:clientName is null or client.name like :clientName)
                  and (:masterName is null or master.name like :masterName)
                  and (:roomNumber is null or room.number like :roomNumber)
                  and (:idClient is null or client.id = :idClient)
                  and (:idMaster is null or master.id = :idMaster)
                  and (:idRoom is null or room.id = :idRoom)
                  and (cast(:dateOfVisit as date) is null or r.dateOfVisit = :dateOfVisit)
                """
        )
)
public class Reception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_master")
    private Master master;

    @ManyToOne
    @JoinColumn(name = "id_room")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @Column(name = "date_of_visit")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message="{reception.errors.reception.date.is_null}")
    private LocalDate dateOfVisit;

    @Column(name = "start_time")
    @JsonFormat(pattern = "HH:mm")
    @NotNull(message="{reception.errors.reception.time.is_null}")
    private LocalTime startTime;

    @Column(name ="finish_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime finishTime;

    @Column(name ="details")
    @Size(max=250, message="{reception.errors.reception.details.invalid_size}")
    private String details;
}
