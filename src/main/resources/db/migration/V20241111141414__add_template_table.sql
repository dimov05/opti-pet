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

ALTER TABLE "bill_template"
    ADD CONSTRAINT "bill_template_fk_clinic"
        FOREIGN KEY ("clinic_id") REFERENCES "clinic" ("id");

ALTER TABLE "bill_template"
    ADD CONSTRAINT "bill_template_fk_user"
        FOREIGN KEY ("user_id") REFERENCES "user" ("id");

CREATE TABLE "bill_template_consumable"
(
    "bill_template_id" UUID NOT NULL,
    "consumable_id"    UUID NOT NULL
);
CREATE TABLE "bill_template_medication"
(
    "bill_template_id" UUID NOT NULL,
    "medication_id"    UUID NOT NULL
);
CREATE TABLE "bill_template_procedure"
(
    "bill_template_id" UUID NOT NULL,
    "procedure_id"     UUID NOT NULL
);
ALTER TABLE "bill_template_consumable"
    ADD PRIMARY KEY ("bill_template_id", "consumable_id");
ALTER TABLE "bill_template_consumable"
    ADD CONSTRAINT "bill_template_consumable_fk_bill_template"
        FOREIGN KEY ("bill_template_id") REFERENCES "bill_template" ("id");
ALTER TABLE "bill_template_consumable"
    ADD CONSTRAINT "bill_template_consumable_fk_consumable"
        FOREIGN KEY ("consumable_id") REFERENCES "consumable" ("id");

ALTER TABLE "bill_template_medication"
    ADD PRIMARY KEY ("bill_template_id", "medication_id");
ALTER TABLE "bill_template_medication"
    ADD CONSTRAINT "bill_template_medication_fk_bill_template"
        FOREIGN KEY ("bill_template_id") REFERENCES "bill_template" ("id");
ALTER TABLE "bill_template_medication"
    ADD CONSTRAINT "bill_template_medication_fk_medication"
        FOREIGN KEY ("medication_id") REFERENCES "medication" ("id");

ALTER TABLE "bill_template_procedure"
    ADD PRIMARY KEY ("bill_template_id", "procedure_id");
ALTER TABLE "bill_template_procedure"
    ADD CONSTRAINT "bill_template_procedure_fk_bill_template"
        FOREIGN KEY ("bill_template_id") REFERENCES "bill_template" ("id");
ALTER TABLE "bill_template_procedure"
    ADD CONSTRAINT "bill_template_procedure_fk_procedure"
        FOREIGN KEY ("procedure_id") REFERENCES "procedure" ("id");
