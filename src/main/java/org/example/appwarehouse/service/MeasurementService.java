package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Measurement;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    public Result addMeasurement( Measurement measurement) {
        boolean exists = measurementRepository.existsByName(measurement.getName());
        if(exists) {
            return new Result("Bundey o'lchov birligi mavjud",false);
        }
        measurementRepository.save(measurement);
        return new Result("Muvaffaqiyatli saqlandi",true);
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    };

    public Measurement getMeasurementById(Integer id) {
        return measurementRepository.findById(id).get();
    }

    public Result updateMeasurement( Integer id, Measurement measurement) {
        Optional<Measurement> optionalMeasurement = measurementRepository.findById(id);
        if(!optionalMeasurement.isPresent()) {
            return new Result("Bundey o'lchov birligi mavjud emas",false);
        }
        Measurement measurementToUpdate = optionalMeasurement.get();
        measurementToUpdate.setName(measurement.getName());
        measurementToUpdate.setActive(measurement.isActive());
        measurementRepository.save(measurementToUpdate);
        return new Result("Muvaffaqiyatli saqlandi",true);
    };

    public Result deleteMeasurement( Integer id) {
        Optional<Measurement> optionalMeasurement = measurementRepository.findById(id);
        if(!optionalMeasurement.isPresent()) {
            return new Result("Bundey o'lchov birligi mavjud emas",false);
        }
        measurementRepository.deleteById(id);
        return new Result("Muvaffaqiyatli o'chirildi",true);
    }
}

