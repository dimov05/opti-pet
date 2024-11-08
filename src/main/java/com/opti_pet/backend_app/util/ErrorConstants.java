package com.opti_pet.backend_app.util;

public class ErrorConstants {
    public static final String ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE = "%s with field '%s' and value '%s' was not found!";
    public static final String EMAIL_MUST_BE_VALID_EXCEPTION = "Field 'email' must be a well-formed email address.";
    public static final String EMAIL_NOT_PRESENT = "random@email.com";
    public static final String RANDOM_NOT_VALID_UUID = "6ad019c3-ae89-48c1-be81-dbb6dac4933a";
    public static final String FIELD_NOT_NULL_EXCEPTION = "Field '%s' must not be null.";
    public static final String FIELD_NOT_BLANK_EXCEPTION = "Field '%s' must not be blank.";
    public static final String UNEXPECTED_VALUE_EXCEPTION = "Unexpected value: ";
    public static final String INVALID_EXCEL_FORMAT_COLUMN_AND_ROW_TEMPLATE_EXCEPTION = "Invalid Excel format: Cell(%d) on Row (%d) does not match the expected format.";
    public static final String ERROR_PROCESSING_EXCEL_FILE_TEMPLATE_EXCEPTION = "Error processing the Excel file";
    public static final String INVALID_EXCEL_FORMAT_HEADERS_DO_NOT_MATCH_TEMPLATE_EXCEPTION = "Invalid Excel format: headers do not match expected format.";
}
