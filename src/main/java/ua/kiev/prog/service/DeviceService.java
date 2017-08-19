package ua.kiev.prog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.model.*;
import ua.kiev.prog.repository.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PhotoRepository photoRepository;

    /**
     * Adds device to database.
     */
    @Transactional
    public void addDevice(Device device) {
        deviceRepository.save(device);
    }

    /**
     * Adds type to database.
     */
    @Transactional
    public void addType(Type type) {
        typeRepository.save(type);
    }


    /**
     * Adds photo to database.
     */
    @Transactional
    public void addPhoto(Photo photo) {
        photoRepository.save(photo);
    }

    /**
     * Removes cart from database.
     */
    @Transactional
    public void deleteCart(int id) {
        cartRepository.delete(id);
    }

    /**
     * Removes device from database.
     */
    @Transactional
    public void deleteDevice(int id) {

        deviceRepository.delete(id);
    }

    /**
     * Returns all types of devices from database.
     */
    @Transactional(readOnly = true)
    public List<Type> listTypes() {
        return (List<Type>) typeRepository.findAll();
    }

    /**
     * Returns all  devices from database.
     */
    @Transactional(readOnly = true)
    public List<Device> listDevices() {
        return (List<Device>) deviceRepository.findAll();
    }

    /**
     * Returns all devices of certain type from database.
     */
    @Transactional(readOnly = true)
    public List<Device> listDevicesByType(Type type) {
        return  deviceRepository.findByTypeOrderByNameAsc(type);

    }

    /**
     * Returns type with  certain id from database.
     */
    @Transactional(readOnly = true)
    public Type findTypeById(int id) {
        return typeRepository.findOne(id);
    }


    /**
     * Returns device with certain name from database.
     */
    @Transactional(readOnly = true)
    public Device findDeviceByName(String name) {
        return deviceRepository.findByName(name);
    }

    /**
     * Returns device with certain id from database.
     */
    @Transactional(readOnly = true)
    public Device findDeviceById(int id) {
        return deviceRepository.findOne(id);
    }

    /**
     * Returns devices  from database according to search pattern.
     */
    @Transactional(readOnly = true)
    public List<Device> searchDevices(String type, String pattern) {
        return deviceRepository.findByNameStartingWith(pattern);
    }

    @Transactional
    public List<Device> devicesInCart(User user) {
        return deviceRepository.findByCartsIn(cartRepository.findByUser(user));
    }

    /**
     * Returns all photos of certain device from database.
     */
    @Transactional
    public List<Photo> getPhotos(Device device) {
        return photoRepository.findByDeviceId(device.getId());
    }

    @Transactional
    public Photo getMainPhoto(Device device) {
        return photoRepository.findFirstByDevice(device);
    }

    @Transactional(readOnly = true)
    public List<Device> priceSorter(Type type, String dir) {
        List<Device> list = null;
        if (dir.equals("asc")) {
            list = deviceRepository.findByTypeOrderByPriceAsc(type);
        }
        if (dir.equals("desc")) {
            list = deviceRepository.findByTypeOrderByPriceDesc(type);
        }
        return list;
    }

    /**
     * Returns all devices with certain amount of ram and certain type of processor.
     */
  /*  @Transactional(readOnly = true)
    public List<Device> ramFilter(String type, List<Integer> ram, List<String> proc) {
        return deviceRepository
    }

    /**
     * Returns all devices with certain manufacturer from database.
     */
    @Transactional(readOnly = true)
    public List<Device> manufacturerFilter(Type type, Set<String> brands) {
        List<Device>deviceList = null;
        if(brands.size()==0){
            deviceList = deviceRepository.findByTypeOrderByNameAsc(type);}
        if(brands.size()>0){
        deviceList = deviceRepository.findByTypeAndManufacturerIn(type, brands);}

        return deviceList;
    }
}
