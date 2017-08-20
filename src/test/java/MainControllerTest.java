import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.kiev.prog.Main;
import ua.kiev.prog.model.Type;
import ua.kiev.prog.service.DeviceService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeviceService deviceService;
    Type type1;
    List<Type> types;
    @Before
    public void setUp() {
        type1 = new Type("Smartphone");
        type1.setId(1);
        types = new ArrayList<>();
        types.add(type1);
    }

    @Test
    public void indexPageTest() throws Exception {
        when(deviceService.listTypes()).thenReturn(types);
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("types", types))
                .andExpect(view().name("index"));
    }


}


