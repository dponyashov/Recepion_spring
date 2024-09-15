package ru.dponyashov.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_notification", schema = "main_schema")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries(
        @NamedQuery(name="Notification.findAll",
                query = "select n from Notification n"
        )
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "notifications")
    @JsonIgnore
    private List<Client> clients;

    @Column(name = "notification_code")
    private String code;

    @Column(name = "notification_name")
    private String name;
}
