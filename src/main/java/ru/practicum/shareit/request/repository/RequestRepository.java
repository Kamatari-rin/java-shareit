package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;


import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestsByRequestMakerIdOrderByCreatedDesc(Long userId);

    List<Request> findAllByRequestMakerIsNot(User user, Pageable pageable);
}
