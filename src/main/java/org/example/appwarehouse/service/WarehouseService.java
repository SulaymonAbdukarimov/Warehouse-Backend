package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Warehouse;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    @Autowired
    private WarehouseRepository warehouseRepository;

    public Result addWarehouse(Warehouse warehouse) {
        boolean exists = warehouseRepository.existsByName(warehouse.getName());
        if (exists) {
            return new Result("Bundey warehouse mavjud",false);
        }
        warehouseRepository.save(warehouse);
        return new Result("Yangi warehouse saqlandi",true);
    };

    public List<Warehouse> getWarehouseList() {
        return warehouseRepository.findAll();
    }

    public Result deleteWarehouse(Integer id) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(id);
        if (!warehouseOptional.isPresent()) {
            return new Result("Bundey Warehouse mavjud emas",false);
        }
        warehouseRepository.deleteById(id);
        return new Result("Warehouse o'chirildi",true);
    };

    public Result updateWarehouse(Integer id, Warehouse warehouse) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(id);
        if (!warehouseOptional.isPresent()) {
            return new Result("Bundey Warehouse mavjud emas",false);
        }
        warehouseRepository.save(warehouse);
        return new Result("Warehouse o'zgartirildi",true);
    };
}
