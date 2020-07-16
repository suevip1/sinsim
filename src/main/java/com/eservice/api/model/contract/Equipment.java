package com.eservice.api.model.contract;

public class Equipment {
    // 机器装置信息，以JSON字符串的形式进行保存（name/number/price）
    private String name;
    private Integer number;
//    private Integer price;
    private Double price;
    private String type;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
