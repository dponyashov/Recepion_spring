package ru.dponyashov.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.dponyashov.enums.RecordStatus;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(schema = "online_schema", name = "t_record")
@NamedQueries(
        @NamedQuery(name = "Record.findByFilter",
                query = """
                        select r from RecordEntity r
                        where (:phone is null or r.phone like :phone)
                          and (:name is null or r.name like :name)
                          and (:status is null or r.status like :status)
                        """
        )
)
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_client")
    private String phone;

    @Column(name = "name_client")
    private String name;

    @Column(name = "note_client")
    private String note;

    @Column(name = "status_record")
    @Enumerated(EnumType.STRING)
    private RecordStatus status;
}
