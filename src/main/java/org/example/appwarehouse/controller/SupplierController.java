package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Supplier;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public Result addSupplier(@RequestBody Supplier supplier) {
        Result result = supplierService.createSupplier(supplier);
        return result;
    }
    @PutMapping("/{id}")
    public Result updateSupplier(@PathVariable Integer id, @RequestBody Supplier supplier) {
        Result result = supplierService.updateSupplier(id,supplier);
        return result;
    }
    @GetMapping("/list")
    public List<Supplier> getAllSuppliers() {
        List<Supplier> supplierList = supplierService.allSuppliers();
        return supplierList;
    }

    @DeleteMapping("/{id}")
    public Result deleteSupplier(@PathVariable Integer id) {
        Result result = supplierService.deleteSupplier(id);
        return result;
    }
}
