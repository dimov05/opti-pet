ALTER TABLE "bill"
    ADD COLUMN "open_date" TIMESTAMP NOT NULL DEFAULT now();
ALTER TABLE "bill"
    ADD COLUMN "close_date" TIMESTAMP;
ALTER TABLE "bill"
    ADD COLUMN "updated_date" TIMESTAMP NOT NULL DEFAULT now();
ALTER TABLE "bill"
    ADD COLUMN "created_by_user_id" UUID NOT NULL;
ALTER TABLE "bill"
    ADD COLUMN "updated_by_user_id" UUID NOT NULL;

ALTER TABLE "bill"
    ADD CONSTRAINT "patient_fk_creator_user"
        FOREIGN KEY ("created_by_user_id") REFERENCES "user" ("id");

ALTER TABLE "bill"
    ADD CONSTRAINT "patient_fk_updater_user"
        FOREIGN KEY ("updated_by_user_id") REFERENCES "user" ("id");