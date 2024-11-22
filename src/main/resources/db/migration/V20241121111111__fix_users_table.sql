ALTER TABLE "user"
    ALTER COLUMN "email" DROP NOT NULL;

ALTER TABLE "user"
    DROP CONSTRAINT IF EXISTS user_email_key;

ALTER TABLE "user"
    ALTER COLUMN "phone_number" SET NOT NULL;

ALTER TABLE "user"
    ADD CONSTRAINT user_phone_number_key UNIQUE ("phone_number");