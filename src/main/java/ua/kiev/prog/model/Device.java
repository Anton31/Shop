package ua.kiev.prog.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Devices")
public class Device {
    @Id
    @GeneratedValue
    private int id;
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private List<Cart> carts;

    private String name;
    private String manufacturer;
    private int price;
    private int ram;
    private String processor;

    /**
     * Class constructor (default).
     */
    public Device() {
    }

    /**
     * Class constructor with parameters.
     */
    public Device(Type type, String name, String manufacturer, int price, int ram, String processor) {
        this.type = type;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.ram = ram;
        this.processor = processor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        return getId() == device.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}


