package ru.dponyashov.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.dponyashov.safety.annotation.StringToEncode;

import java.util.List;

@Entity
@Table(name = "t_client", schema = "main_schema")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries(
        @NamedQuery(name = "Client.findByFilter",
                query = """
                    select c from Client c 
                    where (:clientName is null or c.name like :clientName)
                      and (:clientPhone is null or c.phone like :clientPhone)
                    """)
)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_client")
    @StringToEncode
    @NotNull(message = "{reception.errors.client.name.is_null}")
    @Size(min=5, max=50, message = "{reception.errors.client.name.invalid_size}")
    private String name;

    @Column(name = "phone_client")
    @StringToEncode
    @NotNull(message="{reception.errors.client.phone.is_null}")
    private String phone;

    @Column(name = "mail_client")
    @StringToEncode
    private String mail;

    @ManyToMany
    @JoinTable(
            name = "t_client_notify", schema = "main_schema",
            joinColumns = @JoinColumn(name = "id_client"),
            inverseJoinColumns = @JoinColumn(name = "id_notification"))
    private List<Notification> notifications;
}
