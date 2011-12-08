CREATE VIEW AGG_KEYWORD AS SELECT k.word as word, sum(k.COUNT * o.FACTOR ) as count FROM keyword as k, document as d, owner as o WHERE k.DOCUMENT_ID = d.ID and d.OWNER_ID = o.ID GROUP BY k.word;
CREATE VIEW EVENT_AGG_KEYWORD AS SELECT o.id as course_id, k.word as word, sum(k.COUNT) as count FROM keyword as k, document as d, owner as o WHERE k.DOCUMENT_ID = d.ID and d.OWNER_ID = o.ID and o.DTYPE = 'Event' GROUP BY k.word, o.ID;
CREATE VIEW PERSON_AGG_KEYWORD AS SELECT o.id as person_id, k.word as word, sum(k.COUNT) as count FROM keyword as k, document as d, owner as o WHERE k.DOCUMENT_ID = d.ID and ((d.OWNER_ID = o.ID and o.DTYPE = 'Person') OR EXISTS( SELECT * FROM OWNER_OWNER as oo WHERE oo.PERSONS_ID =o.ID AND d.OWNER_ID = oo.EVENT_ID )) GROUP BY k.word, o.ID;
CREATE VIEW UNIT_SELECT_PERSON_AGG_KEYWORD AS SELECT u.id as unit_id, u.name as unit_name, ak.person_id as person_id, p.firstname as person_name, ak.word as word, ak.count as count FROM UNIT as u, OWNER as p, UNIT_OWNER as uo, PERSON_AGG_KEYWORD as ak WHERE u.id = uo.UNIT_ID AND ak.PERSON_ID = uo.PERSONS_ID AND p.ID = ak.PERSON_ID;
CREATE TABLE BLACKLIST (KEYWORD char(50) UNIQUE);
CREATE TABLE SYNONYM (KEYWORD char(50), SYNONYM char(50) UNIQUE)
CREATE INDEX IDX_SYNONYM_SYNONYM ON  SYNONYM (SYNONYM);
