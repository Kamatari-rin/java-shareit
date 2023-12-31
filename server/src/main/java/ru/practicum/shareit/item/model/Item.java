package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "items", schema = "public")
@EqualsAndHashCode(exclude = {"name", "description", "available", "owner", "request"})
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Request request;

    @OneToMany
    @JoinColumn(name = "item_id")
    private Set<Booking> bookings;

    @OneToMany
    @JoinColumn(name = "item_id")
    private Set<Comment> comments;
}
