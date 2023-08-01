package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class RequestCreateDto {

    Long userId;

    @NotNull
    String description;
}
