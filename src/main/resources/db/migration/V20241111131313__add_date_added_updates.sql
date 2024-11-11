ALTER TABLE "discount"
    ADD COLUMN "date_added" DATE NOT NULL DEFAULT now();
ALTER TABLE "discount"
    ADD COLUMN "date_updated" DATE NOT NULL DEFAULT now();