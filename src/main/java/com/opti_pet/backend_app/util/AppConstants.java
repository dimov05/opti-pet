package com.opti_pet.backend_app.util;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AppConstants {
    public static final UUID DEFAULT_CLINIC_UUID = UUID.fromString("3837ce44-ff3f-4b63-8833-7193be3aa4c3");
    public static final String EMAIL_FIELD_NAME = "email";
    public static final String NAME_FIELD_NAME = "name";
    public static final String CITY_FIELD_NAME = "city";
    public static final String ADDRESS_FIELD_NAME = "address";
    public static final String PHONE_NUMBER_FIELD_NAME = "phoneNumber";
    public static final String ID_FIELD_NAME = "id";
    public static final String UUID_FIELD_NAME = "UUID";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATE_TIME_PATTERN = "HH:mm dd.MM.yyyy";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer DEFAULT_PAGE_NUMBER = 0;
    public static final String USER_ENTITY = "User";
    public static final String CLINIC_ENTITY = "Clinic";
    public static final String ROLE_ENTITY = "Role";
    public static final String PATIENT_ENTITY = "Patient";
    public static final String PRICE_FIELD_NAME = "price";
    public static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String CLINIC_FIELD_NAME = "clinic";
    public static final String PATIENT_FIELD_NAME = "patient";
    public static final String OWNER_FIELD_NAME = "owner";
    public static final String CLIENT_FIELD_NAME = "client";
    public static final String PASSPORT_FIELD_NAME = "passport";
    public static final String CONSUMABLE_ENTITY = "Consumable";
    public static final String PROCEDURE_ENTITY = "Procedure";
    public static final String MEDICATION_ENTITY = "Medication";
    public static final String BILL_TEMPLATE_ENTITY = "Bill Template";
    public static final String TAX_RATE_PERCENT_FIELD_NAME = "taxRatePercent";
    public static final String DATE_ADDED_FIELD_NAME = "dateAdded";
    public static final String DATE_UPDATED_FIELD_NAME = "dateUpdated";
    public static final String AVAILABLE_QUANTITY_FIELD_NAME = "availableQuantity";
    public static final String ID_EXPORT_NAME = "Id";
    public static final String PRICE_EXPORT_NAME = "Price";
    public static final String DESCRIPTION_EXPORT_NAME = "Description";
    public static final String NAME_EXPORT_NAME = "Name";
    public static final String TAX_RATE_PERCENT_EXPORT_NAME = "Tax Rate %";
    public static final String DATE_ADDED_EXPORT_NAME = "Date Added";
    public static final String DATE_UPDATED_EXPORT_NAME = "Date Updated";
    public static final String FINAL_PRICE_EXPORT_NAME = "Price after Tax";
    public static final String AVAILABLE_QUANTITY_EXPORT_NAME = "Available Quantity";
    public static final String PROCEDURE_EXPORT_TYPE = "procedure";
    public static final String MEDICATION_EXPORT_TYPE = "medication";
    public static final String CONSUMABLE_EXPORT_TYPE = "consumable";
    public static final String EXPORT_EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String CONTENT_DISPOSITION = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String CONTENT_DISPOSITION_FORMAT_FILE_NAME = "attachment; filename=\"%s%s.xlsx\"";
    public static final String TEMPLATE = "Template";
    public static final String FILE_NAME_TEMPLATE_IMPORT = "%ss_template_import_";
    public static final String CLINIC_ID_FIELD_NAME = "clinicId";
    public static final String USER_FIELD_NAME = "user";
    public static final String AMOUNT_AFTER_TAX_FIELD_NAME = "amountAfterTax";
    public static final String REMAINING_AMOUNT_FIELD_NAME = "remainingAmount";
    public static final String OPEN_DATE_FIELD_NAME = "openDate";
}
