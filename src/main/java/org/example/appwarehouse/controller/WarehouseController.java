package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Warehouse;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping
    public Result addWarehouse(@RequestBody Warehouse warehouse ) {
       Result result =  warehouseService.addWarehouse(warehouse);
       return result;
    };

    @GetMapping("/list")
    public List<Warehouse> listWarehouse() {
        List<Warehouse> warehouseList = warehouseService.getWarehouseList();
        return warehouseList;
    }

    @PutMapping("/{id}")
    public Result updateWarehouse(@RequestBody Warehouse warehouse, @PathVariable Integer id) {
        Result result = warehouseService.updateWarehouse(id,warehouse);
        return result;
    }

    @DeleteMapping("/{id}")
    public Result deleteWarehouse(@PathVariable Integer id) {
        Result result = warehouseService.deleteWarehouse(id);
        return result;
    };
}
