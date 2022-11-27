-- drop table mip_service;
-- drop table mip_transaction;
-- drop table mip_code;
-- drop table mip_step_result;
-- drop table mip_trx_history;
-- drop table mip_admin;
-- drop table mip_emp_daily_smry;
-- drop sequence SQ_MIP_EMP_DAILY_SMRY_ID;

CREATE TABLE MATERIAL
(
    type  varchar2(50)  NOT NULL,
    quantity integer DEFAULT 200 NOT NULL
);

COMMENT ON COLUMN MATERIAL.type IS '원료';

COMMENT ON COLUMN MATERIAL.quantity IS '수량';

ALTER TABLE MATERIAL
    ADD CONSTRAINT PK_MATERIAL PRIMARY KEY (type);

CREATE TABLE PRODUCT_ORDER
(
	order_no      	varchar2(50) NOT NULL,
    order_code		varchar2(50) NOT NULL,
    step_code       varchar2(20) NOT NULL,
    order_date		varchar2(6) NOT NULL,
    send_date		varchar2(8) 
);

COMMENT ON COLUMN PRODUCT_ORDER.order_no IS '주문번호';

COMMENT ON COLUMN PRODUCT_ORDER.order_code IS '주문코드';

COMMENT ON COLUMN PRODUCT_ORDER.step_code IS '제품 상태';

COMMENT ON COLUMN PRODUCT_ORDER.order_date IS '주문일자';

COMMENT ON COLUMN PRODUCT_ORDER.send_date IS '발송예정일';

ALTER TABLE PRODUCT_ORDER
    ADD CONSTRAINT PK_PRODUCT_ORDER PRIMARY KEY (order_no);
    
CREATE TABLE STOCK
(
	type  varchar2(50)  NOT NULL,
    quantity integer DEFAULT 200 NOT NULL
);

COMMENT ON COLUMN STOCK.type IS '원료';

COMMENT ON COLUMN STOCK.quantity IS '재고';

ALTER TABLE STOCK
    ADD CONSTRAINT PK_STOCK PRIMARY KEY (type);

CREATE SEQUENCE SQ_PRODUCT_ORDER_NO START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 999999999 CYCLE CACHE 50;