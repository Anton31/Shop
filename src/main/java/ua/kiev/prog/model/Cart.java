package ua.kiev.prog.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Carts")
public class Cart {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "device_id")

    private Device device;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList();
    private int items;

    /**
     * Class constructor (default).
     */
    public Cart() {
    }

    /**
     * Class constructor with parameters.
     */
    public Cart(User user, Device device, int items) {
        this.user = user;
        this.device = device;
        this.items = items;
    }

    /**
     * Returns total price of certain amount of devices.
     */
    public int totalPrice() {
        return items * device.getPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;

        Cart cart = (Cart) o;

        return getId() == cart.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
