INSERT INTO users ( email, first_name, last_name, password, postcode, user_role, username )
VALUES ( 'gadmin@localhost', 'gadmin', 'gadmin', '$2a$10$p3Q7uAuj6J.wM15xVL/QAeVOtLEy0HShtsqSRILTlOOVrxtLe50d6',
         '12345', 'GENERAL_ADMIN', 'gadmin' ),
       ( 'cadmin@localhost', 'cadmin', 'cadmin', '$2a$10$Us28KZ6IEZobDd3ZIkQ9kudH0BssiIpjhGlHWN/YuE761XKw8krIS',
         '6857', 'COUNCIL_ADMIN', 'cadmin' ),
       ( 'moderator@localhost', 'moderator', 'moderator',
         '$2a$10$NL4iJdBptEYYDnsYvKaIW.7kPQNmi2ZQD9WZQ81VD2ETmpRVULrfS',
         '8678675', 'MODERATOR', 'moderator' ),
       ( 'member1@localhost', 'member1', 'member1', '$2a$10$TeFgOrKh3heqFwXOcFZ82OdQ8asTAr8krJydjgsAXvJc6kcYvFHZK',
         '23425', 'MEMBER', 'member1' ),
       ( 'member2@localhost', 'member2', 'member2', '$2a$10$0SEX6x9MVC24UkyFSgMmJeRvNXlXgBH.vmiBvRPOpNBExurHyzCuS',
         '89876', 'MEMBER', 'member2' ),
       ( 'moderator1@localhost', 'moderator1', 'moderator1',
         '$2a$12$fqJRfbx7TOIIPoyERKII4OTkuz1Wf3ZDCOyHZc3CVPGpkP601Pnv.',
         '123123', 'MODERATOR', 'moderator1' );

INSERT INTO youthcouncil ( council_logo, council_name, description, is_after_election, municipality )
VALUES ( 'my/youth/council/logo', 'my_youthcouncil', 'my youthcouncil description', 'false', 'my_municipality' );

INSERT INTO youthcouncil_council_admins
VALUES ( 1, 2 );
INSERT INTO youthcouncil_council_members
VALUES ( 1, 4 ), ( 1, 6 );

-- INFORMATIVE PAGES
INSERT INTO informative_pages ( is_enabled, title )
VALUES ( 'true', 'my_informative_page' );
INSERT INTO youthcouncil_informative_pages
VALUES ( 1, 1 );


-- ACTION POINTS
INSERT INTO module_items( module_item_type, description, is_default, is_enabled, title, label, image )
VALUES ( 'actionpoint', 'my action point description', 'false', 'true', 'my_action_point', 'NEW',
         'my/action/point/image ' );
INSERT INTO youthcouncil_module_items
VALUES ( 1, 1 );

-- CALL FOR IDEA
INSERT INTO module_items( module_item_type, description, is_default, is_enabled, title, image )
VALUES ( 'call-for-idea', 'my call for idea description', 'false', 'true', 'my_call_for_idea',
         'my/call/for/idea/image ' );
INSERT INTO youthcouncil_module_items
VALUES ( 1, 2 );

-- NEWSFEED
INSERT INTO module_items( module_item_type, description, is_default, is_enabled, title, image )
VALUES ( 'announcement', 'my announcement description', 'false', 'true', 'my_announcement', 'my/announcement/image ' );
INSERT INTO youthcouncil_module_items
VALUES ( 1, 3 );


-- THEMES
INSERT INTO themes( theme )
VALUES ( 'road safety' );
INSERT INTO subthemes( sub_theme )
VALUES ( 'safety at intersection x' ),
       ( 'safety in area y' );

INSERT INTO themes_sub_themes
VALUES ( 1, 1 );