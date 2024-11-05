CREATE TABLE "consumable"
(
    "id"                 UUID PRIMARY KEY NOT NULL                   DEFAULT gen_random_uuid(),
    "name"               VARCHAR(255)     NOT NULL,
    "description"        VARCHAR(255),
    "available_quantity" BIGINT           NOT NULL                   DEFAULT 0,
    "price"              DECIMAL(9, 2) CHECK (price >= 0),
    "tax_rate_percent"   DECIMAL(5, 2) CHECK (tax_rate_percent >= 0) DEFAULT 20.00,
    "date_added"         DATE             NOT NULL,
    "date_updated"       DATE             NOT NULL,
    "is_active"          BOOLEAN          NOT NULL                   DEFAULT TRUE,
    "clinic_id"        UUID             NOT NULL
);
CREATE TABLE "billed_consumable"
(
    "id"               UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    "name"             VARCHAR(255)     NOT NULL,
    "description"      VARCHAR(255),
    "billed_price"     DECIMAL(9, 2)    NOT NULL,
    "tax_rate_percent" DECIMAL(5, 2)    NOT NULL,
    "quantity"         BIGINT           NOT NULL DEFAULT 1,
    "billed_date"      DATE             NOT NULL,
    "bill_id"          UUID             NOT NULL,
    "user_id"          UUID             NOT NULL,
    "clinic_id"      UUID             NOT NULL,
    "discount_id"      BIGINT
);
ALTER TABLE "consumable"
    ADD CONSTRAINT "consumable_fk_clinic"
        FOREIGN KEY ("clinic_id") REFERENCES "clinic" ("id");

ALTER TABLE "billed_consumable"
    ADD CONSTRAINT "billed_consumable_fk_bill"
        FOREIGN KEY ("bill_id") REFERENCES "bill" ("id");

ALTER TABLE "billed_consumable"
    ADD CONSTRAINT "billed_consumable_fk_user"
        FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "billed_consumable"
    ADD CONSTRAINT "billed_consumable_fk_discount"
        FOREIGN KEY ("discount_id") REFERENCES "discount" ("id");

ALTER TABLE "billed_consumable"
    ADD CONSTRAINT "billed_consumable_fk_clinic"
        FOREIGN KEY ("clinic_id") REFERENCES "clinic" ("id");