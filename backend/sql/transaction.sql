DROP DATABASE IF EXISTS `transaction` ;

CREATE DATABASE `transaction`;

USE `transaction`;

CREATE TABLE `user`(
	user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT "用户id",
	username VARCHAR(255) NOT NULL UNIQUE COMMENT "用户名",
	`password` VARCHAR(255) NOT NULL COMMENT "密码",
	email VARCHAR(255) NOT NULL UNIQUE COMMENT "邮箱",
	role TINYINT NOT NULL COMMENT "用户角色，0普通用户，1商家用户，2管理员用户",
	`status` TINYINT NOT NULL DEFAULT -1 COMMENT "用户状态，0表示用户正常，-1表示用户还为通过管理员验证，1表示用户账号异常"
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户基础信息表';

CREATE TABLE common_user(
	user_id INT NOT NULL,
	`name` VARCHAR(255) NOT NULL COMMENT '用户姓名',
	phone VARCHAR(11) NOT NULL COMMENT "用户电话",
	sex TINYINT NOT NULL DEFAULT 1 COMMENT "1为男性，0位女性",
	city VARCHAR(255) NOT NULL COMMENT "需要在前端约束，输入正确的城市信息",
	account VARCHAR(16) NOT NULL COMMENT "银行卡号",
	FOREIGN KEY(user_id) REFERENCES `user`(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='普通用户信息表';


CREATE TABLE business_user(
	user_id INT NOT NULL,
	`name` VARCHAR(255) NOT NULL COMMENT '用户姓名',
	phone VARCHAR(11) NOT NULL COMMENT "用户电话",
	sex TINYINT NOT NULL DEFAULT 1 COMMENT "1为男性，0位女性",
	identification_front VARCHAR(255) NOT NULL COMMENT "身份证前保存路径",
	identification_back VARCHAR(255) NOT NULL COMMENT "身份证后保存路径",
	account VARCHAR(16) NOT NULL COMMENT "银行卡号",
	license VARCHAR(255) NOT NULL COMMENT "营业执照保存路径",
	FOREIGN KEY(user_id) REFERENCES `user`(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商家用户信息表';

CREATE TABLE shop(
	shop_id INT PRIMARY KEY AUTO_INCREMENT COMMENT "商铺id",
	shop_uuid VARCHAR(36) NOT NULL UNIQUE COMMENT "店铺唯一标识",
	shop_name VARCHAR(255) NOT NULL COMMENT "商铺名称",
	business_id INT NOT NULL COMMENT "用户商家id",
	`level` INT NOT NULL DEFAULT 1 COMMENT "商家等级，用于收费1级收0.1%，2级收0.2%，3级收0.5%，4级收0.75%，5级收1%",
	praise_rate DOUBLE(5,2) NOT NULL DEFAULT 0 COMMENT "店铺好评率",
	praise_count INT NOT NULL DEFAULT 0 COMMENT "店铺总好评数量，是该店铺的所有商品的好评数量之和",
	deal_count INT NOT NULL DEFAULT 0 COMMENT "店铺的总交易量，是该店铺的所有商品的交易数量之和",
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "店铺创建时间",
	`status` TINYINT NOT NULL DEFAULT -1 COMMENT "-1表示正在审核中，0表示关了，1表示开着呢",
	FOREIGN KEY(business_id) REFERENCES `user`(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商铺信息表';

CREATE TABLE goods(
	goods_id INT PRIMARY KEY AUTO_INCREMENT COMMENT "商品id",
	goods_uuid VARCHAR(36) NOT NULL UNIQUE COMMENT "商品唯一标识符，由服务端自动生成",
	shop_uuid VARCHAR(36) NOT NULL COMMENT "店铺uuid",
	goods_name VARCHAR(255) NOT NULL COMMENT "商品名",
	goods_price DOUBLE(10,2) NOT NULL COMMENT "商品价格",
	goods_description BLOB COMMENT "商品描述",
	goods_count INT NOT NULL DEFAULT 1 COMMENT "商品数量",
	goods_praise_count INT NOT NULL DEFAULT 0 COMMENT "如果用户打分超过3分（满分5分）那么好评数量+1",
	goods_praise_rate DOUBLE(5,2) NOT NULL DEFAULT 0 COMMENT "商品好评率，计算方式为（goods_praise_count/goods_deal_count）*100",
	goods_deal_count INT NOT NULL DEFAULT 0 COMMENT "商品交易量",
	goods_type VARCHAR(255) NOT NULL COMMENT "商品类别，给出几个具体的商品种类",
	goods_discount_price DOUBLE(10,2) NOT NULL COMMENT "商品打折价,默认与goods_price相等，提供update方法更改",
	size VARCHAR(255) NOT NULL COMMENT "物品大小，有用户输入单位",
	is_bargain TINYINT NOT NULL DEFAULT 0 COMMENT "是否可以讨价还价，默认不可以",
	damage_level INT NOT NULL DEFAULT 10 COMMENT "损坏程度，10为全新，0为完全损坏",
	discount INT NOT NULL DEFAULT 100 COMMENT "折扣，默认100，即不打折",
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT "0已下架（商家手动下架或插入时默认），1在售卖（商家上线商品后需要经过管理员审核后才状态可变为1），-1已售罄（goods_count==0时）"
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商品信息表';


CREATE TABLE deal(
	deal_uuid VARCHAR(36) NOT NULL COMMENT "订单号，订单额唯一标识符",
	goods_uuid VARCHAR(36) NOT NULL COMMENT "商品唯一标识符",
	shop_uuid VARCHAR(36) NOT NULL COMMENT "店铺唯一标识符",
	common_id INT NOT NULL COMMENT "买家id",
	deal_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "交易时间，从用户点击收货开始",
	deal_count INT NOT NULL DEFAULT 1 COMMENT "一种物品的交易数量",
	assess TINYINT NOT NULL DEFAULT 5 COMMENT "对商品的评价，默认为5星好评",
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT "0表示订单正在交易（默认），1订单已退货（买家确认退货成功后订单变为-1），-1订单已彻底完成（无法退货或已退货完成），2订单已被签收（从此时开始24小时内没有退货变为-1）"
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='交易处理表';

CREATE TABLE wallet(
	user_id INT NOT NULL COMMENT "用户id",
	integral INT NOT NULL DEFAULT 0 COMMENT "用户积分",
	sum_money INT NOT NULL DEFAULT 0 COMMENT "用户总充值的金额",
	current_money INT NOT NULL DEFAULT 0 COMMENT "用户当前账户中的金额",
	FOREIGN KEY(user_id) REFERENCES `user`(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户积分表';

CREATE TABLE `comment`(
	comment_id INT PRIMARY KEY AUTO_INCREMENT COMMENT "评论id",
	goods_uuid VARCHAR(36) NOT NULL COMMENT "商品uuid",
	user_id INT NOT NULL  COMMENT "用户id",
	comment_content BLOB NOT NULL COMMENT "评论内容",
	comment_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "评论时间",
	reply_id INT COMMENT "默认为空，如果该评论为评论的评论那么这个值就是相对应的comment_id"
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商品评论表';


CREATE TABLE shop_car(
	goods_uuid VARCHAR(36) NOT NULL COMMENT "商品标识",
	common_id INT NOT NULL COMMENT "购买者id",
	goods_count INT NOT NULL COMMENT "商品数量",
	STATUS TINYINT NOT NULL DEFAULT 0 COMMENT "商品状态，默认为0，即在购物车中，1为已购买，-1为已从购物车中删除"
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT="购物车表";

CREATE TABLE goods_picture(
	goods_uuid VARCHAR(36) NOT NULL COMMENT "商品标识",
	picture_path VARCHAR(255) NOT NULL COMMENT "商品图片存储路径",
	join_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "图片加入时间"
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商品图片表';

CREATE TABLE middle_wallet(
	deal_uuid VARCHAR(36) NOT NULL COMMENT "订单号",
	sum_money DOUBLE(10,2) NOT NULL COMMENT "此订单的总价钱",
	STATUS TINYINT NOT NULL DEFAULT 0 COMMENT "默认为0，0表示正在处理中，1表示商家已收款，-1表示商家已退货，货款已还给买家"
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT "转账中间表，用来确保退货不会出现差值";


INSERT INTO USER(username,PASSWORD,email,role,STATUS) VALUES("admin","admin","admin@qq.com",2,0);




