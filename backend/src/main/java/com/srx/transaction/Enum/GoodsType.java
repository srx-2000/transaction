package com.srx.transaction.Enum;

public enum GoodsType {
    BOOK("书籍",1),
    STATIONERY("文具",2),
    Digital("数码",3),
    CODE("代码代写",4)
    ;


    private final String type;
    private final Integer typeCode;

    GoodsType(String type, int typeCode) {
        this.type = type;
        this.typeCode = typeCode;
    }
}
