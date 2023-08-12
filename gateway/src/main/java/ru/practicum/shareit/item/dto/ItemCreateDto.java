package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDto {

    private Long ownerId;

    @NotBlank(groups = {OnCreate.class}, message = "Поле не может быть пыстым.")
    private String name;

    @NotBlank(groups = {OnCreate.class}, message = "Поле не может быть пыстым.")
    private String description;

    @NotNull(groups = {OnCreate.class}, message = "Поле не может быть пыстым.")
    private Boolean available;

    private Long requestId;
}
