--------------------------------------------------------
--  DDL for Table PERSON
--------------------------------------------------------

  CREATE TABLE "PERSON" 
   (	"ID" NUMBER(38,0), 
	"FIRST_NAME" VARCHAR2(40 BYTE), 
	"SECOND_NAME" VARCHAR2(40 BYTE)
   );
--------------------------------------------------------
--  DDL for Index PERSON_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PERSON_PK" ON "PERSON" ("ID");
--------------------------------------------------------
--  Constraints for Table PERSON
--------------------------------------------------------

  ALTER TABLE "PERSON" ADD CONSTRAINT "PERSON_PK" PRIMARY KEY ("ID");

--------------------------------------------------------
--  DDL for Table POST
--------------------------------------------------------

  CREATE TABLE "POST" 
   (	"ID" NUMBER, 
    "PERSON_ID" NUMBER,
	"TITLE" VARCHAR2(40 BYTE)
   );
--------------------------------------------------------
--  DDL for Index POST_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "POST_PK" ON "POST" ("ID");
--------------------------------------------------------
--  Constraints for Table POST
--------------------------------------------------------

  ALTER TABLE "POST" ADD CONSTRAINT "POST_PK" PRIMARY KEY ("ID");
 
  ALTER TABLE "POST" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table POST
--------------------------------------------------------

  ALTER TABLE "POST" ADD CONSTRAINT "POST_FK" FOREIGN KEY ("PERSON_ID") REFERENCES "PERSON" ("ID") ON DELETE CASCADE ENABLE;
  
  CREATE INDEX POST_INDEX1 ON POST (PERSON_ID);
  
--------------------------------------------------------
--  DDL for Table COMMENT
--------------------------------------------------------

  CREATE TABLE "COMMENT" 
   (	"ID" NUMBER, 
    "POST_ID" NUMBER,
	"CONTENT" VARCHAR2(40 BYTE)
   );
--------------------------------------------------------
--  DDL for Index COMMENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "COMMENT_PK" ON "COMMENT" ("ID");
--------------------------------------------------------
--  Constraints for Table COMMENT
--------------------------------------------------------

  ALTER TABLE "COMMENT" ADD CONSTRAINT "COMMENT_PK" PRIMARY KEY ("ID");
 
  ALTER TABLE "COMMENT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table COMMENT
--------------------------------------------------------

  ALTER TABLE "COMMENT" ADD CONSTRAINT "COMMENT_FK" FOREIGN KEY ("POST_ID") REFERENCES "POST" ("ID") ON DELETE CASCADE ENABLE;
  
  CREATE INDEX COMMENT_INDEX1 ON COMMENT (POST_ID);
  
  CREATE SEQUENCE  "S_PERSON_INDEX";
  
  CREATE SEQUENCE  "S_POST_INDEX";
  
  CREATE SEQUENCE  "S_COMMENT_INDEX";

