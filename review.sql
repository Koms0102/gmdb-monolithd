create table review (
	id bigint(20) not null auto_increment, 
	last_updated datetime, 
	review_text LONGTEXT, 
	review_title TEXT, 
	movie_id bigint not null, 
	reviewer_id bigint not null, 
	primary key (id)
) ;

alter table review add constraint review_user_fk foreign key (reviewer_id) references user (id);
alter table review add constraint review_movies_fk foreign key (movie_id) references movies (movie_id);

