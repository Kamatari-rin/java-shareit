package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long ownerId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull(message = "Поле не может быть пыстым.")
    private Boolean available;

    private Long requestId;
}
