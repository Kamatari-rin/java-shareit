package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i " +
            "from Item i " +
            "join fetch i.owner " +
            "where i.owner.id = :id ")
    List<Item> findAllByOwnerIdWithBookings(@Param("id") Long userId, Sort sortByIdAsc);

    @Query("select i " +
            "from Item i " +
            "join fetch i.owner " +
            "where i.id = :id ")
    @NonNull
    Optional<Item> findByIdWithOwner(@Param("id") @NonNull Long id);

    @Query("select i " +
           "from Item as i " +
           "where i.available = true " +
           "and (UPPER(i.name) like UPPER(CONCAT('%', :text, '%')) " +
           "or UPPER(i.description) like UPPER(CONCAT('%', :text, '%')))")
    List<Item> search(@Param("text") String text);
}
