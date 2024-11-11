ALTER TABLE "billed_consumable"
    ALTER COLUMN "billed_date" TYPE TIMESTAMP;
ALTER TABLE "billed_medication"
    ALTER COLUMN "billed_date" TYPE TIMESTAMP;
ALTER TABLE "billed_procedure"
    ALTER COLUMN "billed_date" TYPE TIMESTAMP;

CREATE TABLE "hospital"
(
    "id"               UUID PRIMARY KEY NOT NULL                   DEFAULT gen_random_uuid(),
    "name"             VARCHAR(255)     NOT NULL,
    "description"      VARCHAR(255),
    "price"            DECIMAL(9, 2) CHECK (price >= 0),
    "final_price"      DECIMAL(9, 2) CHECK (final_price >= 0),
    "tax_rate_percent" DECIMAL(5, 2) CHECK (tax_rate_percent >= 0) DEFAULT 20.00,
    "date_added"       DATE             NOT NULL,
    "date_updated"     DATE             NOT NULL,
    "is_active"        BOOLEAN          NOT NULL                   DEFAULT TRUE,
    "clinic_id"        UUID             NOT NULL
);
CREATE TABLE "booked_hospital"
(
    "id"               UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "name"             VARCHAR(255)     NOT NULL,
    "description"      VARCHAR(255),
    "billed_price"     DECIMAL(9, 2)    NOT NULL,
    "tax_rate_percent" DECIMAL(5, 2)    NOT NULL,
    "booked_hours"     BIGINT           NOT NULL DEFAULT 0,
    "start_date"       TIMESTAMP        NOT NULL,
    "end_date"         TIMESTAMP,
    "billed_date"      TIMESTAMP        NOT NULL,
    "bill_id"          UUID             NOT NULL,
    "user_id"          UUID             NOT NULL,
    "clinic_id"        UUID             NOT NULL,
    "patient_id"       UUID             NOT NULL,
    "discount_id"      BIGINT
);
CREATE TABLE "hospital_note"
(
    "id"                 UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "message"            TEXT,
    "date_added"         TIMESTAMP        NOT NULL,
    "is_public"          BOOLEAN          NOT NULL DEFAULT TRUE,
    "user_id"            UUID             NOT NULL,
    "booked_hospital_id" UUID             NOT NULL
);

ALTER TABLE "hospital"
    ADD CONSTRAINT "hospital_fk_clinic"
        FOREIGN KEY ("clinic_id") REFERENCES "clinic" ("id");

ALTER TABLE "booked_hospital"
    ADD CONSTRAINT "booked_hospital_fk_bill"
        FOREIGN KEY ("bill_id") REFERENCES "bill" ("id");

ALTER TABLE "booked_hospital"
    ADD CONSTRAINT "booked_hospital_fk_user"
        FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "booked_hospital"
    ADD CONSTRAINT "booked_hospital_fk_discount"
        FOREIGN KEY ("discount_id") REFERENCES "discount" ("id");

ALTER TABLE "booked_hospital"
    ADD CONSTRAINT "booked_hospital_fk_clinic"
        FOREIGN KEY ("clinic_id") REFERENCES "clinic" ("id");

ALTER TABLE "booked_hospital"
    ADD CONSTRAINT "booked_hospital_fk_patient"
        FOREIGN KEY ("patient_id") REFERENCES "patient" ("id");

ALTER TABLE "hospital_note"
    ADD CONSTRAINT "hospital_note_fk_booked_hospital"
        FOREIGN KEY ("booked_hospital_id") REFERENCES "booked_hospital" ("id");

ALTER TABLE "hospital_note"
    ADD CONSTRAINT "hospital_note_fk_user"
        FOREIGN KEY ("user_id") REFERENCES "user" ("id");