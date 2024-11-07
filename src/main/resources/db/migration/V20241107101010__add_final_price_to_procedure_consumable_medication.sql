ALTER TABLE "procedure"
    ADD COLUMN "final_price" DECIMAL(9, 2) CHECK (final_price >= 0) DEFAULT 0;
ALTER TABLE "consumable"
    ADD COLUMN "final_price" DECIMAL(9, 2) CHECK (final_price >= 0) DEFAULT 0;
ALTER TABLE "medication"
    ADD COLUMN "final_price" DECIMAL(9, 2) CHECK (final_price >= 0) DEFAULT 0;