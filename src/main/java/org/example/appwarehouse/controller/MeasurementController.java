package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Measurement;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/measurement")
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;

    @PostMapping
    public Result addMeasurement(@RequestBody Measurement measurement) {
        Result result = measurementService.addMeasurement(measurement);
        return result;
    }

    @GetMapping("/list")
    public List<Measurement> getAllMeasurements() {
        List<Measurement> result = measurementService.getAllMeasurements();
        return result;
    };

    @GetMapping("/{id}")
    public Measurement getMeasurementById(@PathVariable Integer id) {
        Measurement result = measurementService.getMeasurementById(id);
        return result;
    }

    @PutMapping("/{id}")
    public Result updateMeasurement( @PathVariable Integer id, @RequestBody Measurement measurement) {
        Result result = measurementService.updateMeasurement(id,measurement);
        return result;
    };


    @DeleteMapping("/{id}")
    public Result deleteMeasurement(@PathVariable Integer id) {
        Result result = measurementService.deleteMeasurement(id);
        return result;
    }

}
