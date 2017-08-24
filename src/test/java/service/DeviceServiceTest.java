package service;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.kiev.prog.model.Device;
import ua.kiev.prog.model.Type;
import ua.kiev.prog.repository.DeviceRepository;
import ua.kiev.prog.repository.TypeRepository;
import ua.kiev.prog.service.DeviceService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
public class DeviceServiceTest {

    @InjectMocks
    DeviceService deviceService;

    @Mock
    TypeRepository typeRepository;

    @Mock
    DeviceRepository deviceRepository;
    Type type1;
    Device device1;
    Device device2;
    List<Type> types;
    List<Device> devices;


    @Before
    public void setUpTypes() {
        types = new ArrayList<>();
        type1 = new Type("Smartphone");
        type1.setId(1);
        types.add(type1);
    }

    public void setUpDevices() {
        devices = new ArrayList<>();
        device1 = new Device(type1, "iPhone7", "Apple", 30000, -1, null);
        device2 = new Device(type1, "iPhone6", "Apple", 15000, -1, null);
        device1.setId(1);
        device2.setId(2);
        type1.setId(1);
        types.add(type1);
        devices.add(device1);
        devices.add(device2);
    }

    @Test
    public void listTypesTest() {
        when(typeRepository.findAll()).thenReturn(types);
        assertEquals(deviceService.listTypes(), types);
    }

    @Test
    public void listDevicesTest() {
        when(deviceRepository.findByTypeOrderByNameAsc(type1)).thenReturn(devices);
        assertEquals(deviceService.listDevicesByType(type1), devices);
    }


}


