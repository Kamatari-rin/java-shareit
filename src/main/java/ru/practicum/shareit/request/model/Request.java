package ru.practicum.shareit.request.model;


import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "requests", schema = "public")
@EqualsAndHashCode(exclude = {"description", "requestMaker"})
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requestMaker;

    @OneToMany
    @JoinColumn(name = "request_id")
    private Set<Item> items;

    public void addItem(Item item) {
        items.add(item);
        item.setRequest(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setRequest(null);
    }
}
