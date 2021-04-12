SELECT para."story_id", para."para_id", para."user_id", para."content", para."is_final" FROM "Parent Section" pare LEFT JOIN "Paragraphe" para ON para."story_id" = pare."story_id" AND para."para_id" = pare."para_id" WHERE para."story_id" = 23 START WITH para."is_final" = 1 CONNECT BY pare."para_id" = prior pare."parent_para_id";

with t as (
    SELECT para."story_id", para."para_id", para."user_id", para."content", para."is_final", pare."parent_para_id" FROM "Parent Section" pare LEFT JOIN "Paragraphe" para ON para."story_id" = pare."story_id" AND para."para_id" = pare."para_id" WHERE para."story_id" = 21
)
SELECT para."story_id", para."para_id", para."user_id", para."content", para."is_final" FROM t START WITH "is_final" = 1 CONNECT BY "para_id" = prior "parent_para_id"; 