package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank(groups = OnCreate.class,
            message = "Имя не может быть пустым.")
    private String name;

    @NotBlank(groups = OnCreate.class,
            message = "Email не может быть пустым.")
    @Email(groups = {OnCreate.class, OnUpdate.class},
            regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Не корректный Email.")
    private String email;
}
