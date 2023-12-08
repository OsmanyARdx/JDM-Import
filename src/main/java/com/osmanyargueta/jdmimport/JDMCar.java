 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.osmanyargueta.jdmimport;

import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Blueprint class of a JDMCar
 * @author Osmany
 */
public class JDMCar {

    private int year;
    private String make;
    private  String model;
    private String color;
    private Long odometer;
    private double price;
    private String owner = null;
    private String url = null;
    private Image carImage = null;
    
    /**
     * Default constructor, everything will be null
     */
    public JDMCar(){
        make = null;
        model = null;
        color = null;
        odometer = null;
        price = 0.0;
        owner = null;
        url = null;
        carImage = null;
    }

    /**
     * Default constructor
     * @param y takes in int y to be set as the year
     * @param mak takes in String mak to be set as the make
     * @param mod takes in String mod to be set as the model
     * @param col takes in String col to be set as the color
     * @param odo takes in Long odo to be set as the odometer
     * @param pri takes in double pri to be set as the price
     * @param ow takes in String ow to be set as the owner
     * @param u takes in String u to be set as the url for the image
     */
    public JDMCar(int y, String mak, String mod, String col, Long odo, double pri, String ow, String u) {
        year = y;
        make = mak;
        model = mod;
        color = col;
        odometer = odo;
        price = pri;
        owner = ow;
        url = u;
        carImage = new Image(url);
    }

    /**
     * Method to get the year
     * @return returns the year as an int
     */
    public int getYear() {
        return year;
    }

    /**
     * Method to set they year
     * @param year takes in an int that will be set as the year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Method to get the make
     * @return returns the make as a String
     */
    public String getMake() {
        return make;
    }

    /**
     * Method to set the make
     * @param make takes a String that will be set as the make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Method to get the model
     * @return returns the make as a String
     */
    public String getModel() {
        return model;
    }

    /**
     * Method to set the model
     * @param model takes in a String that will be set as the model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Method to get the model
     * @return returns the model as a String
     */
    public String getColor() {
        return color;
    }

    /**
     * Method to set the color
     * @param color takes in a String that will be set as the color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Method to get the odometer reading
     * @return returns the odometer as a Long
     */
    public Long getOdometer() {
        return odometer;
    }

    /**
     * Method to set the odometer reading
     * @param odometer takes in a Long that will be set as the odometer
     */
    public void setOdometer(Long odometer) {
        this.odometer = odometer;
    }

    /**
     * Method to get the price of the car
     * @return returns the price as a double
     */
    public double getPrice() {
        return price;
    }

    /**
     * Method to set the price
     * @param price takes in a double that will be set as the price
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Method to get owner's name
     * @return returns the name of the owner as a String
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Method to set the owner's name
     * @param owner takes in a String that will be set as the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Method to get the online url of the image
     * @return returns the image online url as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Method to set the url
     * @param url takes in a String that will be set as the url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Method to get the image
     * @return returns an image of the car
     */
    public Image getCarImage() {
        return carImage;
    }

    /**
     * Method to set the car image
     * @param carImage takes in an image that will be set as the carImage
     */
    public void setCarImage(Image carImage) {
        this.carImage = carImage;
    }
    
    /**
     * Method override of toString, will return only the model of the car
     * @return returns car model only.
     */
    @Override
    public String toString(){
        return model;
    }
    
    
}
