package ru.dponyashov.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "t_notification", schema = "main_schema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
