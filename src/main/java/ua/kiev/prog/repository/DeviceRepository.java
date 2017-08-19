package ua.kiev.prog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kiev.prog.model.Cart;
import ua.kiev.prog.model.Device;
import ua.kiev.prog.model.Type;

import java.util.List;
import java.util.Set;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Integer> {

        Device findByName(String name);

        List<Device> findByCartsIn(List<Cart>carts);
        List<Device> findByTypeAndManufacturerIn(Type type, Set<String> brands);
        List<Device> findByTypeOrderByNameAsc(Type type);

        List<Device> findByTypeOrderByPriceAsc(Type type);
        List<Device> findByTypeOrderByPriceDesc(Type type);
        List<Device> findByNameStartingWith(String pattern);

}
