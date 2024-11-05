ALTER TABLE "billed_item"
    RENAME CONSTRAINT
        "billed_item_fk_bill" TO "billed_medication_fk_bill";
ALTER TABLE "billed_item"
    RENAME CONSTRAINT
        "billed_item_fk_user" TO "billed_medication_fk_user";
ALTER TABLE "billed_item"
    RENAME CONSTRAINT
        "billed_item_fk_discount" TO "billed_medication_fk_discount";
ALTER TABLE "billed_item"
    RENAME CONSTRAINT
        "billed_item_fk_clinic" TO "billed_medication_fk_clinic";
ALTER TABLE "item"
    RENAME CONSTRAINT
        "item_fk_clinic" TO "medication_fk_clinic";
ALTER TABLE IF EXISTS "item"
    RENAME TO "medication";
ALTER TABLE IF EXISTS "billed_item"
    RENAME TO "billed_medication";