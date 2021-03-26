package com.srx.transaction.Enum;

public enum ShopLevel {
    LEVEL_ONE(1,0.001),
    LEVEL_TWO(2,0.002),
    LEVEL_THREE(3,0.005),
    LEVEL_FOUR(4,0.0075),
    LEVEL_FIVE(5,0.01),
    ;

    ShopLevel(Integer shopLevel, Double rate) {
        this.shopLevel = shopLevel;
        this.rate = rate;
    }

    private final Integer shopLevel;
    private final Double rate;

    public Integer getShopLevel() {
        return shopLevel;
    }

    public Double getRate() {
        return rate;
    }
}
