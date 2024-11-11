ALTER TABLE "discount"
    RENAME COLUMN "percent_items" TO "percent_consumables";
ALTER TABLE "discount"
    ADD COLUMN "percent_medications" DECIMAL(5, 2) CHECK (percent_medications >= 0);
ALTER TABLE "discount"
    ADD COLUMN "percent_hospitals" DECIMAL(5, 2) CHECK (percent_hospitals >= 0);