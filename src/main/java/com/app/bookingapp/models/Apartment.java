package com.app.bookingapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//віздповідає за таблицю в бд
@Entity // указывает, что эта модель считается сущностью, связанной с таблицей в базе данных
//автоматично створювати таблиці бази даних для об'єктів цього класу, а також виконувати операції збереження, оновлення, видалення та пошуку даних в цих таблицях.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apartment {

    //анотація для ід
    @Id
    //дозволяє кожен раз генерувати нове значення всередині цього поля - автоматичне нове ід для нового оголошення
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name, description, country, city, street;
    private double price;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String photo;


    //userId
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "apartment")
    private List<Reservations> reservations = new ArrayList<>();

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public double getPrice() {
        return price;
    }

    /*public User getUserId() {
        return userId;
    }*/

    public String getPhoto() {
        return photo;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   /* public void setUserId(User userId) {
        this.userId = userId;
    }
*/
   public void setPhoto(String photo) {
       this.photo = photo;
   }

    public Apartment(String name, String description, String country, String city, String street, double price, String photo) {
        this.name = name;
        this.description = description;
        this.country = country;
        this.city = city;
        this.street = street;
        this.price = price;
        this.photo = photo;
        //this.userId = userId;
    }
    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", description="
                + description + ", price=" + price + ", image="
                + photo + "]";
    }

}
