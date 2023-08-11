package ru.practicum.shareit.util;

import org.springframework.data.domain.Sort;

public class Constant {
    public static final String HEADER_USER_ID = "X-Sharer-User-Id";
    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
    public static final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "startBooking");
    public static final  String FIRST_ELEMENT_INDEX = "0";
    public static final String LIMIT_OF_PAGES = "20";
    public static final String DEFAULT_ELEMENT_INDEX = "0";
    public static final String LIMIT_OF_PAGES_DEFAULT = "20";
}
