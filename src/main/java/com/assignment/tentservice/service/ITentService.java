package com.assignment.tentservice.service;

public interface ITentService {
    public void addCar(String model, String color, Integer seatingCapacity, Integer driverId);
    public void deleteById(int id);
}
