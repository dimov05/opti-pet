CREATE TABLE "bill_template"
(
    "id"           UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "name"         VARCHAR(255)     NOT NULL,
    "description"  VARCHAR(255),
    "date_added"   DATE             NOT NULL,
    "date_updated" DATE             NOT NULL,
    "is_active"    BOOLEAN          NOT NULL DEFAULT TRUE,
    "clinic_id"    UUID             NOT NULL,
    "user_id"      UUID             NOT NULL
);

CREATE TABLE "consumable_template"
(
    "id"               UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "quantity"         BIGINT           NOT NULL DEFAULT 1,
    "consumable_id"    UUID             NOT NULL,
    "bill_template_id" UUID             NOT NULL
);
CREATE TABLE "medication_template"
(
    "id"               UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "quantity"         BIGINT           NOT NULL DEFAULT 1,
    "medication_id"    UUID             NOT NULL,
    "bill_template_id" UUID             NOT NULL
);
CREATE TABLE "procedure_template"
(
    "id"               UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "quantity"         BIGINT           NOT NULL DEFAULT 1,
    "procedure_id"     UUID             NOT NULL,
    "bill_template_id" UUID             NOT NULL
);

ALTER TABLE "bill_template"
    ADD CONSTRAINT "bill_template_fk_clinic"
        FOREIGN KEY ("clinic_id") REFERENCES "clinic" ("id");

ALTER TABLE "bill_template"
    ADD CONSTRAINT "bill_template_fk_user"
        FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "consumable_template"
    ADD CONSTRAINT "consumable_template_fk_consumable"
        FOREIGN KEY ("consumable_id") REFERENCES "consumable" ("id");

ALTER TABLE "consumable_template"
    ADD CONSTRAINT "consumable_template_fk_bill_template"
        FOREIGN KEY ("bill_template_id") REFERENCES "bill_template" ("id");

ALTER TABLE "medication_template"
    ADD CONSTRAINT "medication_template_fk_consumable"
        FOREIGN KEY ("medication_id") REFERENCES "medication" ("id");

ALTER TABLE "medication_template"
    ADD CONSTRAINT "medication_template_fk_bill_template"
        FOREIGN KEY ("bill_template_id") REFERENCES "bill_template" ("id");

ALTER TABLE "procedure_template"
    ADD CONSTRAINT "procedure_template_fk_consumable"
        FOREIGN KEY ("procedure_id") REFERENCES "procedure" ("id");

ALTER TABLE "procedure_template"
    ADD CONSTRAINT "procedure_template_fk_bill_template"
        FOREIGN KEY ("bill_template_id") REFERENCES "bill_template" ("id");