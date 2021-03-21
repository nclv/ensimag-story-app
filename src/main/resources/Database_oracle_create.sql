CREATE TABLE "User" (
	"user_id" NUMBER(10) NOT NULL,
	"username" VARCHAR2(50) UNIQUE NOT NULL,
	"password" VARCHAR2(50) NOT NULL,
	constraint USER_PK PRIMARY KEY ("user_id"));

CREATE sequence "USER_USER_ID_SEQ";

CREATE trigger "BI_USER_USER_ID"
  before insert on "User"
  for each row
begin
  select "USER_USER_ID_SEQ".nextval into :NEW."user_id" from dual;
end;

/
CREATE TABLE "Story" (
	"story_id" NUMBER(10) NOT NULL,
	"open" NUMBER(1) DEFAULT "0" NOT NULL ,
	"published" NUMBER(1) DEFAULT "0" NOT NULL,
	"user_id" NUMBER(10) NOT NULL,
	constraint STORY_PK PRIMARY KEY ("story_id"));

CREATE sequence "STORY_STORY_ID_SEQ";

CREATE trigger "BI_STORY_STORY_ID"
  before insert on "Story"
  for each row
begin
  select "STORY_STORY_ID_SEQ".nextval into :NEW."story_id" from dual;
end;

/
CREATE TABLE "Paragraphe" (
	"story_id" NUMBER(10) NOT NULL,
	"para_id" NUMBER(10) NOT NULL,
	"content" CLOB NOT NULL,
	"is_final" NUMBER(1) DEFAULT "0" NOT NULL,
	constraint PARAGRAPHE_PK PRIMARY KEY ("story_id","para_id"));

CREATE sequence "PARAGRAPHE_PARA_ID_SEQ";

CREATE trigger "BI_PARAGRAPHE_PARA_ID"
  before insert on "Paragraphe"
  for each row
begin
  select "PARAGRAPHE_PARA_ID_SEQ".nextval into :NEW."para_id" from dual;
end;

/
CREATE TABLE "Parent Section" (
	"story_id" NUMBER(10) NOT NULL,
	"para_id" NUMBER(10) NOT NULL,
	"parent_story_id" NUMBER(10) NOT NULL,
	"parent_para_id" NUMBER(10) NOT NULL,
	"parag_condition_story_id" NUMBER(10) NOT NULL,
	"parag_condition_para_id" NUMBER(10) NOT NULL,
	"choice_text" CLOB NOT NULL,
	"choice_num" NUMBER(10) NOT NULL,
	constraint PARENT_SECTION_PK PRIMARY KEY ("story_id","para_id","parent_story_id","parent_para_id"));


/
CREATE TABLE "Historique" (
	"user_id" NUMBER(10) NOT NULL,
	"story_id" NUMBER(10) NOT NULL,
	"para_id" NUMBER(10) NOT NULL,
	"historic_id" NUMBER(10) NOT NULL,
	constraint HISTORIQUE_PK PRIMARY KEY ("user_id","story_id","para_id","historic_id"));

CREATE sequence "HISTORIQUE_HISTORIC_ID_SEQ";

CREATE trigger "BI_HISTORIQUE_HISTORIC_ID"
  before insert on "Historique"
  for each row
begin
  select "HISTORIQUE_HISTORIC_ID_SEQ".nextval into :NEW."historic_id" from dual;
end;

/
CREATE TABLE "Invited" (
	"user_id" NUMBER(10) NOT NULL,
	"story_id" NUMBER(10) NOT NULL,
	"date" DATE NOT NULL,
	constraint INVITED_PK PRIMARY KEY ("user_id","story_id"));


/
CREATE TABLE "Redaction" (
	"user_id" NUMBER(10) NOT NULL,
	"story_id" NUMBER(10) NOT NULL,
	"para_id" NUMBER(10) NOT NULL,
	"is_validated" NUMBER(1) NOT NULL,
	constraint REDACTION_PK PRIMARY KEY ("user_id","story_id","para_id"));


/

ALTER TABLE "Story" ADD CONSTRAINT "Story_fk0" FOREIGN KEY ("user_id") REFERENCES "User"("user_id");

ALTER TABLE "Paragraphe" ADD CONSTRAINT "Paragraphe_fk0" FOREIGN KEY ("story_id") REFERENCES "Story"("story_id");

ALTER TABLE "Parent Section" ADD CONSTRAINT "Parent Section_fk0" FOREIGN KEY ("story_id") REFERENCES "Paragraphe"("story_id");
ALTER TABLE "Parent Section" ADD CONSTRAINT "Parent Section_fk1" FOREIGN KEY ("para_id") REFERENCES "Paragraphe"("para_id");
ALTER TABLE "Parent Section" ADD CONSTRAINT "Parent Section_fk2" FOREIGN KEY ("parent_story_id") REFERENCES "Paragraphe"("story_id");
ALTER TABLE "Parent Section" ADD CONSTRAINT "Parent Section_fk3" FOREIGN KEY ("parent_para_id") REFERENCES "Paragraphe"("para_id");
ALTER TABLE "Parent Section" ADD CONSTRAINT "Parent Section_fk4" FOREIGN KEY ("parag_condition_story_id") REFERENCES "Paragraphe"("story_id");
ALTER TABLE "Parent Section" ADD CONSTRAINT "Parent Section_fk5" FOREIGN KEY ("parag_condition_para_id") REFERENCES "Paragraphe"("para_id");

ALTER TABLE "Historique" ADD CONSTRAINT "Historique_fk0" FOREIGN KEY ("user_id") REFERENCES "User"("user_id");
ALTER TABLE "Historique" ADD CONSTRAINT "Historique_fk1" FOREIGN KEY ("story_id") REFERENCES "Paragraphe"("story_id");
ALTER TABLE "Historique" ADD CONSTRAINT "Historique_fk2" FOREIGN KEY ("para_id") REFERENCES "Paragraphe"("para_id");

ALTER TABLE "Invited" ADD CONSTRAINT "Invited_fk0" FOREIGN KEY ("user_id") REFERENCES "User"("user_id");
ALTER TABLE "Invited" ADD CONSTRAINT "Invited_fk1" FOREIGN KEY ("story_id") REFERENCES "Story"("story_id");

ALTER TABLE "Redaction" ADD CONSTRAINT "Redaction_fk0" FOREIGN KEY ("user_id") REFERENCES "User"("user_id");
ALTER TABLE "Redaction" ADD CONSTRAINT "Redaction_fk1" FOREIGN KEY ("story_id") REFERENCES "Paragraphe"("story_id");
ALTER TABLE "Redaction" ADD CONSTRAINT "Redaction_fk2" FOREIGN KEY ("para_id") REFERENCES "Paragraphe"("para_id");

