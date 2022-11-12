INSERT INTO books
SELECT generate_series(1, 10),
       concat(
               'title-', substr(gen_random_uuid()::text, 1, 10)
           ),
       concat(
               'author-', substr(gen_random_uuid()::text, 1, 10)
           ),
       substr(gen_random_uuid()::text, 1, 20);
