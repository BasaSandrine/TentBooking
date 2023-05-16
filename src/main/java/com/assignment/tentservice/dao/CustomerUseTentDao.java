package com.assignment.tentservice.dao;

import com.assignment.tentservice.model.Tent;

import java.util.List;

public class CustomerUseTentDao {
    private int driverId;
    private String driverName;
    private List<Tent> usedTents;

    public CustomerUseTentDao() {}

    public CustomerUseTentDao(int driverId, String driverName, List<Tent> usedTents) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.usedTents = usedTents;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public List<Tent> getUsedCars() {
        return usedTents;
    }

    public void setUsedCars(List<Tent> usedTents) {
        this.usedTents = usedTents;
    }

    @Override
    public String toString() {
        return "CustomerUseTentDao{" +
                "driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", usedCars=" + usedTents +
                '}';
    }
}
