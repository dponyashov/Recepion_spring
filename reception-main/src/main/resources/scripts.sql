select * from pg_indexes where tablename = 't_reception'

explain
select * from t_reception where id_master=1 and id_client=2 limit 100

select * from t_reception r
where (r.id_client = 1)
  and (r.id_master = 1)
  and (r.id_room = 1)
  and (r.date_of_visit = '2024-07-18');