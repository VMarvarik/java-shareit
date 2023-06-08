package ru.practicum.shareit.item.dto;

import lombok.Getter;

@Getter
public class ItemForUpdateDto {
    private String name;
    private String description;
    private Boolean available;
}
