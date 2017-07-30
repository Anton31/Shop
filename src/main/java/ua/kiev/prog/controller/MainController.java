package ua.kiev.prog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kiev.prog.model.*;
import ua.kiev.prog.security.UserService;
import ua.kiev.prog.service.DeviceService;
import ua.kiev.prog.service.OrderService;

import java.util.*;

/**
 * Controller for site navigation
 */

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = {"/", "/user"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("types", deviceService.listTypes());
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }


        model.addAttribute("incart", deviceService.devicesInCart(findUser()));
        model.addAttribute("items", orderService.totalItems(findUser()));
        return "index";
    }

    @RequestMapping(value = "/onedevice/{deviceId}", method = RequestMethod.GET)
    public String oneDeviceById(@PathVariable int deviceId, Model model) {
        Device d = deviceService.findDeviceById(deviceId);
        model.addAttribute("device", d);
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("incart", deviceService.devicesInCart(findUser()));

        model.addAttribute("items", orderService.totalItems(findUser()));
        return "one_device";
    }


    @RequestMapping(value = "/ajax/{chars}", method = RequestMethod.GET)
    @ResponseBody
    String getAjax(@PathVariable String chars) {
        List<Device> list = deviceService.searchDevices("all", chars);
        StringBuilder names = new StringBuilder();
        names.append("<table class='table table-bordered'>");
        for (Device device : list) {


            names.append("<tr><td><b><a href=" + "/onedevice/" + device.getId() + ">" + device.getName() + "</a></b></b></td></tr>");
        }
        names.append("</table>");
        return names.toString();
    }

    @RequestMapping(value = "/type/{typeId}/{dir}", method = RequestMethod.GET)
    public String nameFilter(@PathVariable int typeId, @PathVariable String dir,
                             Model model) {
        Type type = deviceService.findTypeById(typeId);
        model.addAttribute("type", type);
        model.addAttribute("devices", deviceService.listDevicesByType(type, dir));
        if (findUser() == null) {
            model.addAttribute("user", " Log in");
        } else {
            model.addAttribute("user", " " + findUser().getUsername());
        }
        model.addAttribute("sortbyname", dir);
        model.addAttribute("incart", deviceService.devicesInCart(findUser()));
        model.addAttribute("items", orderService.totalItems(findUser()));
        return "device";
    }

    @RequestMapping(value = "/price_sorter/{type}/{dir}")
    public String priceSorter(@PathVariable String type,
                              @PathVariable String dir,
                              Model model
    ) {
        model.addAttribute("devices", deviceService.priceSorter(type, dir));

        model.addAttribute("sortbyprice", dir);

        model.addAttribute("carts", orderService.listCarts(findUser()));
        model.addAttribute("items", orderService.totalItems(findUser()));
        return type.toLowerCase();
    }

    List<Integer> rams = new ArrayList<>();
    List<String> processors = new ArrayList<>();

    @RequestMapping(value = "/ram_filter/{type}/{sram}",
            method = RequestMethod.GET)

    public String ramFilter(@PathVariable String type,
                            @PathVariable String sram,
                            Model model
    ) {
        Integer ram = Integer.parseInt(sram);
        if (!rams.contains(ram)) {
            rams.add(ram);
        } else if (rams.contains(ram)) {
            rams.remove(ram);
        }
        model.addAttribute("rams", rams);
        model.addAttribute("processors", processors);
        model.addAttribute("sortbyname", "ascending");
        model.addAttribute("items", orderService.totalItems(findUser()));
        //   model.addAttribute("devices", deviceService.ramFilter(type, rams, processors));
        if (type.equals("all")) {
            return "index";
        } else {
            return type;
        }
    }

    @RequestMapping(value = "/proc_filter/{type}/{proc}",
            method = RequestMethod.GET)
    public String processorFilter(@PathVariable String type,
                                  @PathVariable String proc,
                                  Model model
    ) {
        if (!processors.contains(proc)) {
            processors.add(proc);
        } else {
            processors.remove(proc);
        }
        model.addAttribute("rams", rams);
        model.addAttribute("processors", processors);
        model.addAttribute("sortbyname", "ascending");
        model.addAttribute("items", orderService.totalItems(findUser()));
        // model.addAttribute("devices", deviceService.ramFilter(type, rams, processors));
        if (type.equals("all")) {
            return "index";
        } else {
            return type;
        }
    }

    List<String> manufacturers = new ArrayList<>();

    @RequestMapping("/manufacturer_filter/{type}/{manufacturer}")
    public String manufacturerFilter(Model model, @PathVariable String type,
                                     @PathVariable String manufacturer) {
        if (!manufacturers.contains(manufacturer)) {
            manufacturers.add(manufacturer);

        } else {
            manufacturers.remove(manufacturer);
        }
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("sortbyname", "ascending");
        model.addAttribute("items", orderService.totalItems(findUser()));
        //  model.addAttribute("devices", deviceService.manufacturerFilter(type, manufacturers));
        return "smartphone";
    }

    @RequestMapping(value = "/photo/{deviceId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> onPhoto(@PathVariable int deviceId) {
        Device d = deviceService.findDeviceById(deviceId);

        Photo photo = deviceService.getMainPhoto(d);


        return ResponseEntity.ok(photo.getBody());
    }

    @RequestMapping(value = "/photo/{deviceId}/{n}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> onPhoto(@PathVariable int deviceId, @PathVariable int n) {
        Device d = deviceService.findDeviceById(deviceId);

        List<Photo> photos = deviceService.getPhotos(d);

        Photo photo = photos.get(n);

        return ResponseEntity.ok(photo.getBody());
    }

    @RequestMapping(value = "/randomphoto/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> rnPhoto(@PathVariable int typeId) {

        Type type = deviceService.findTypeById(typeId);
        List<Device> devices = deviceService.listDevicesByType(type, "asc");
        Random random = new Random();
        Device device = devices.get(random.nextInt(devices.size()));

        Photo photo = deviceService.getMainPhoto(device);
        return ResponseEntity.ok(photo.getBody());
    }

    public User findUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        User user = userService.findByUsername(username);


        return user;
    }
}












