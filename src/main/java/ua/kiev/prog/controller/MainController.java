package ua.kiev.prog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kiev.prog.model.*;
import ua.kiev.prog.service.DeviceService;
import ua.kiev.prog.service.OrderService;
import ua.kiev.prog.service.UserService;

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
        model.addAttribute("carts", orderService.listCarts(findUser()));
        model.addAttribute("items", orderService.totalItems(findUser()));
        return "index";
    }

    @RequestMapping(value = "/onedevice/{id}", method = RequestMethod.GET)
    public String oneDevice(@PathVariable int id, Model model) {
        Device d = deviceService.findDeviceById(id);
        model.addAttribute("d", d);
        model.addAttribute("c", orderService.listCarts(findUser()));
        model.addAttribute("items", orderService.totalItems(findUser()));
        return "one_device_page";
    }

    @RequestMapping(value = "/type/{type}/{dir}", method = RequestMethod.GET)
    public String nameFilter(@PathVariable String type, @PathVariable String dir,
                             Model model) {
        model.addAttribute("devices", deviceService.listDevicesByType(type, dir));
        model.addAttribute("sortbyname", dir);
        model.addAttribute("carts", orderService.listCarts(findUser()));
        model.addAttribute("items", orderService.totalItems(findUser()));
        return type.toLowerCase();
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
        model.addAttribute("devices", deviceService.ramFilter(type, rams, processors));
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
        model.addAttribute("devices", deviceService.ramFilter(type, rams, processors));
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
        model.addAttribute("devices", deviceService.manufacturerFilter(type, manufacturers));
        return "smartphone";
    }


    @RequestMapping(value = "/photo/{id}/{n}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> onPhoto(@PathVariable int id, @PathVariable int n) {
        Device d = deviceService.findDeviceById(id);

        List<Photo> photos = deviceService.getPhotos(d);

        Photo photo = photos.get(n);

        return ResponseEntity.ok(photo.getBody());
    }

    @RequestMapping(value = "/randomphoto/{name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> rnPhoto(@PathVariable String name) {

        Type type = deviceService.findTypeByName(name);
        List<Device> devices = deviceService.priceSorter(type.getName(), "asc");
        Random random = new Random();
        Device device = devices.get(random.nextInt(devices.size()));
        List<Photo> photos = deviceService.getPhotos(device);
        Photo photo = photos.get(0);
        return ResponseEntity.ok(photo.getBody());
    }

    public User findUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        User user = userService.findUser(username);
        return user;
    }
}












