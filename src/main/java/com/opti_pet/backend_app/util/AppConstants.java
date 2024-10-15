package com.opti_pet.backend_app.util;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AppConstants {
    public static final UUID DEFAULT_CLINIC_UUID = UUID.fromString("3837ce44-ff3f-4b63-8833-7193be3aa4c3");
    public static final String EMAIL_FIELD_NAME = "email";
    public static final String ID_FIELD_NAME = "ID";
    public static final String UUID_FIELD_NAME = "UUID";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer DEFAULT_PAGE_NUMBER = 0;
    public static final String USER_ENTITY = "User";
    public static final String CLINIC_ENTITY = "Clinic";
    public static final String ROLE_ENTITY = "Role";
    public static final String PATIENT_ENTITY = "Patient";
}
