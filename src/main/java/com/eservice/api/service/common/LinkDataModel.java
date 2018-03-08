package com.eservice.api.service.common;

import java.util.ArrayList;

/*
此类用于ProcessRecord的
NodeData JSON数据的解析
*/
public class LinkDataModel {

    private String fromPort;
    private String toPort;
    private Integer from;
    private Integer to;
    private ArrayList<Float> points;

    public String getFromPort() {
        return fromPort;
    }

    public void setFromPort(String fromPort) {
        this.fromPort = fromPort;
    }

    public String getToPort() {
        return toPort;
    }

    public void setToPort(String toPort) {
        this.toPort = toPort;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public ArrayList<Float> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Float> points) {
        this.points = points;
    }
}
