package ru.dponyashov.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_room", schema = "main_schema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries(
        @NamedQuery(name = "Room.findByNumber",
                query = "select r from Room r where r.number like :roomNumber")
)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_room")
    @NotNull(message="{reception.errors.room.name.is_null}")
    @Size(min=3, max=50, message="{reception.errors.room.name.invalid_size}")
    private String number;

    @Column(name = "note_room")
    @Size(max=250, message="{reception.errors.room.note.invalid_size}")
    private String note;
}
