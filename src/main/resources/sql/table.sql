create table if not exists comments
(
	id int auto_increment
		primary key,
	m_id varchar(50) not null,
	comment_time varchar(50) null,
	recommend_level varchar(20) null,
	votes int null,
	comment text null
)charset=utf8mb4;

create table if not exists movies
(
    id varchar(50) collate utf8mb4_unicode_ci not null
        primary key,
    cover varchar(255) null,
    cover_x int null,
    cover_y int null,
    is_new tinyint(1) null,
    playable tinyint(1) null,
    rate varchar(20) null,
    title varchar(255) null,
    url varchar(500) null
)charset=utf8mb4;