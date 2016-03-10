package ua.kiev.prog;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Cart {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "device_id")

    private Device device;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList();
    private int items;


    public int totalPrice() {
        return items * device.getPrice();
    }

    public Cart() {
    }

    public Cart(Device device, int items) {
        this.device = device;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
