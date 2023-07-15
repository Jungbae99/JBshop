-- 회원
delete from cart_item;
delete from cart;
delete from order_item;
delete from orders;
delete from delivery;
delete from member;

insert into member (`email`, `password`, `username`, `phone_number`,`author`, `city`, `street`, `zipcode`, `grade`) values ('admin@admin.com', '123', 'admin', '000-000-0000', 'ADMIN', '서울', '강동', '길동', 'VVIP');
insert into member (`email`, `password`, `username`, `phone_number`,`author`, `city`, `street`, `zipcode`, `grade`) values ('wjdqo@naver.com', '123', 'wjdqo', '000-000-0000', 'USER', '서울', '강동', '길동', 'BASIC');

-- update member set author = 'ADMIN' where username is USER;

-- 권한
-- insert into authority (`authority_name`) values ('ROLE_ADMIN');

-- 회원별 권한
-- insert into member_authority (`member_id`, `authority_id`) values (1, 1);
