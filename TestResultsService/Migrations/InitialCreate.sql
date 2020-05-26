CREATE TABLE IF NOT EXISTS "__EFMigrationsHistory" (
    "MigrationId" character varying(150) NOT NULL,
    "ProductVersion" character varying(32) NOT NULL,
    CONSTRAINT "PK___EFMigrationsHistory" PRIMARY KEY ("MigrationId")
);


DO $$
BEGIN
    IF NOT EXISTS(SELECT 1 FROM "__EFMigrationsHistory" WHERE "MigrationId" = '20200526101104_InitialCreate') THEN
    CREATE TABLE test_results (
        id uuid NOT NULL,
        result_id character varying(128) NOT NULL,
        result_date timestamp NOT NULL,
        result integer NOT NULL,
        CONSTRAINT "PK_test_results" PRIMARY KEY (id)
    );
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS(SELECT 1 FROM "__EFMigrationsHistory" WHERE "MigrationId" = '20200526101104_InitialCreate') THEN
    CREATE UNIQUE INDEX "IX_test_results" ON test_results (result_id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS(SELECT 1 FROM "__EFMigrationsHistory" WHERE "MigrationId" = '20200526101104_InitialCreate') THEN
    INSERT INTO "__EFMigrationsHistory" ("MigrationId", "ProductVersion")
    VALUES ('20200526101104_InitialCreate', '3.1.4');
    END IF;
END $$;
