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