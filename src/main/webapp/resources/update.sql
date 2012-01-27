﻿CREATE VIEW AGG_KEYWORD AS SELECT k.word as word, sum(k.COUNT * o.FACTOR ) as count FROM keyword as k, document as d, owner as o WHERE k.DOCUMENT_ID = d.ID and d.OWNER_ID = o.ID GROUP BY k.word;
CREATE VIEW EVENT_AGG_KEYWORD AS SELECT o.id as course_id, k.word as word, sum(k.COUNT) as count FROM keyword as k, document as d, owner as o WHERE k.DOCUMENT_ID = d.ID and d.OWNER_ID = o.ID and o.DTYPE = 'Event' GROUP BY k.word, o.ID;
CREATE VIEW PERSON_AGG_KEYWORD AS SELECT o.id as person_id, k.word as word, sum(k.COUNT) as count FROM keyword as k, document as d, owner as o WHERE k.DOCUMENT_ID = d.ID and ((d.OWNER_ID = o.ID and o.DTYPE = 'Person') OR EXISTS( SELECT * FROM OWNER_OWNER as oo WHERE oo.PERSONS_ID =o.ID AND d.OWNER_ID = oo.EVENT_ID )) GROUP BY k.word, o.ID;
CREATE VIEW UNIT_SELECT_PERSON_AGG_KEYWORD AS  SELECT u.id AS unit_id, u.name AS unit_name, ak.person_id, (p.firstname || ' ' || p.lastname) AS person_name, ak.word, ak.count FROM person_agg_keyword ak JOIN owner p ON ak.person_id = p.id LEFT JOIN unit_owner uo ON p.id = uo.persons_id LEFT JOIN unit u ON uo.unit_id = u.id