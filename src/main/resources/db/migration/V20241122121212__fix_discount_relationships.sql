ALTER TABLE "bill"
    ADD COLUMN "discount_id" BIGINT;
ALTER TABLE "bill"
    ADD COLUMN "amount_before_tax_after_discount" DECIMAL(9, 2) CHECK (amount_before_tax_after_discount >= 0);

ALTER TABLE "bill"
    ADD CONSTRAINT "bill_fk_discount"
        FOREIGN KEY ("discount_id") REFERENCES "discount" ("id");

ALTER TABLE "billed_procedure"
    DROP CONSTRAINT "billed_procedure_fk_discount";
ALTER TABLE "billed_procedure"
    DROP COLUMN "discount_id";

ALTER TABLE "billed_medication"
    DROP CONSTRAINT "billed_medication_fk_discount";
ALTER TABLE "billed_medication"
    DROP COLUMN "discount_id";

ALTER TABLE "billed_consumable"
    DROP CONSTRAINT "billed_consumable_fk_discount";
ALTER TABLE "billed_consumable"
    DROP COLUMN "discount_id";

ALTER TABLE "booked_hospital"
    DROP CONSTRAINT "booked_hospital_fk_discount";
ALTER TABLE "booked_hospital"
    DROP COLUMN "discount_id";