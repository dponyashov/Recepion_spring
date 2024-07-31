package ru.dponyashov.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dponyashov.safety.annotation.StringToEncode;

@Entity
@Table(name = "t_master", schema = "main_schema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedQueries(
        @NamedQuery(name = "Master.findByFilter",
                query = """
                    select m from Master m 
                    where (:masterName is null or m.name like :masterName)
                      and (:masterPhone is null or m.phone like :masterPhone)
                    """)
)
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_master")
    @StringToEncode
    @NotNull(message = "{reception.errors.master.name.is_null}")
    @Size(min=5, max=50, message = "{reception.errors.master.name.invalid_size}")
    private String name;

    @Column(name = "phone_master")
    @StringToEncode
    private String phone;
}
