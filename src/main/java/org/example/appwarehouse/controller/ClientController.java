package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Client;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping
    public Result addSupplier(@RequestBody Client client) {
        Result result = clientService.createClient(client);
        return result;
    }
    @PutMapping("/{id}")
    public Result updateSupplier(@PathVariable Integer id, @RequestBody Client client) {
        Result result = clientService.updateClient(id,client);
        return result;
    }
    @GetMapping("/list")
    public List<Client> getAllSuppliers() {
        List<Client> supplierList = clientService.allClients();
        return supplierList;
    }

    @DeleteMapping("/{id}")
    public Result deleteSupplier(@PathVariable Integer id) {
        Result result = clientService.deleteClient(id);
        return result;
    }
}
