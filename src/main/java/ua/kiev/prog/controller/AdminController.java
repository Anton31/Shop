package ua.kiev.prog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.kiev.prog.model.Device;
import ua.kiev.prog.model.Photo;
import ua.kiev.prog.model.Type;
import ua.kiev.prog.service.DeviceService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Controller for site administration
 */

@Controller
@RequestMapping("/")
public class AdminController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/admin")
    public String index_admin(Model model) {
        model.addAttribute("types", deviceService.listTypes());
        model.addAttribute("devices", deviceService.listDevices());
        return "index_admin";
    }

    @RequestMapping("/device_add_page")
    public String deviceAddPage(Model model) {
        model.addAttribute("devices", deviceService.listDevices());
        model.addAttribute("types", deviceService.listTypes());
        return "device_add_page";
    }

    @RequestMapping("/type_add_page")
    public String typeAddPage() {
        return "type_add_page";
    }

    @RequestMapping(value = "/device/delete")
    public String search(@RequestParam(value = "todelete[]") int[] todelete, Model model) {
    for(int id : todelete){
        deviceService.deleteDevice(id);
    }
        model.addAttribute("types", deviceService.listTypes());
        model.addAttribute("devices", deviceService.listDevices());
        return "index_admin";
    }

    @RequestMapping(value = "/adddevice", method = RequestMethod.POST)
    public String deviceAdd(@RequestParam(value = "type")int typeId,
                            @RequestParam(required = false) MultipartFile main_photo,
                            @RequestParam(required = false) MultipartFile photo2,
                            @RequestParam(required = false) MultipartFile photo3,
                            @RequestParam(required = false) MultipartFile photo4,
                            @RequestParam(defaultValue = "null") String name,
                            @RequestParam(defaultValue = "null") String manufacturer,
                            @RequestParam(defaultValue = "-1") int price,
                            @RequestParam(defaultValue = "-1", required = false) int ram,
                            @RequestParam(defaultValue = "null", required = false) String processor,
                            Model model) throws IOException {
        Type type = deviceService.findTypeById(typeId);
        if (name.equals(null) || manufacturer.equals(null) || price == -1) {
            model.addAttribute("message", "fill all required fields!");
            model.addAttribute("state", "alert alert-danger");
            model.addAttribute("types", deviceService.listTypes());
            return "device_add_page";
        }
        Device device = new Device(type, name, manufacturer, price,
                ram, processor);

        List<Device> list = deviceService.listDevicesByType(type);
        int count = 0;
        for (Device dev : list) {
            if (dev.getName().equalsIgnoreCase(name)) {
                count++;
            }
        }
        if (count == 0) {
            deviceService.addDevice(device);
            Device d = deviceService.findDeviceByName(name);
            deviceService.addPhoto(new Photo(d, main_photo.getOriginalFilename(), main_photo.getBytes()));
            deviceService.addPhoto(new Photo(d, photo2.getOriginalFilename(), photo2.getBytes()));
            deviceService.addPhoto(new Photo(d, photo3.getOriginalFilename(), photo3.getBytes()));
            deviceService.addPhoto(new Photo(d, photo4.getOriginalFilename(), photo4.getBytes()));
            model.addAttribute("devices", deviceService.listDevices());
            return "index_admin";
        }
        if (count > 0) {
            model.addAttribute("message", "such device already exists!");
            model.addAttribute("state", "alert alert-danger");
            return "device_add_page";
        }
        return "index_admin";
    }





    @RequestMapping(value = "/addtype", method = RequestMethod.POST)
    public String addType(@RequestParam String name, Model model) {

        List<Type> list = deviceService.listTypes();


        int count = 0;
        for (Type t : list) {
            if (t.getName().equalsIgnoreCase(name))
            count++;
        }
        if (count > 0) {
            model.addAttribute("message", "such type already exists!");
            model.addAttribute("state", "alert alert-danger");
            return "type_add_page";}

        if (count == 0){
            Type type = new Type(name);
            deviceService.addType(type);}
            model.addAttribute("types", deviceService.listTypes());
        model.addAttribute("devices", deviceService.listDevices());
        return "index_admin";

    }
}
