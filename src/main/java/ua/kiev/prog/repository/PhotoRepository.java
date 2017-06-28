package ua.kiev.prog.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kiev.prog.model.Device;
import ua.kiev.prog.model.Photo;

import java.util.List;
@Repository
public interface PhotoRepository extends CrudRepository<Photo, Integer> {

    List<Photo> findByDeviceId(int id);

    Photo findFirstByDevice(Device device);

}
