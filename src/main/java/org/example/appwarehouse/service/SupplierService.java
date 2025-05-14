package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Supplier;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public Result createSupplier(Supplier supplier) {
        boolean existedByPhoneNumber = supplierRepository.existsByPhoneNumber(supplier.getPhoneNumber());
        if (existedByPhoneNumber) {
            return new Result("Bu nomerda supplier bor",false);
        }
        Supplier saved = supplierRepository.save(supplier);
        if(saved == null) {
            return new Result("Supplier yaratilmadi",false);

        }
        return new Result("Supplier yaratildi",true);
    };

    public Result updateSupplier(Integer id,Supplier supplier) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        if(!optionalSupplier.isPresent()) {
            return new Result("Bundey Supplier yo'q ",false);
        }
        if(!supplier.getPhoneNumber().equals(optionalSupplier.get().getPhoneNumber())) {
            boolean existsByPhoneNumber = supplierRepository.existsByPhoneNumber(supplier.getPhoneNumber());
            if(existsByPhoneNumber) {
                return new Result("Supplier already exists",false);
            }
        }


        Supplier saved = supplierRepository.save(supplier);
        if(saved == null) {
            return new Result("Supplier o'zgartirilmad",false);
        }
        return new Result("Supplier o'zgartirildi",true);
    }

    public List<Supplier> allSuppliers() {
        return  supplierRepository.findAll();
    }

    public Result deleteSupplier(Integer id) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        if(!optionalSupplier.isPresent()) {
            return new Result("Bundey Supplier yo'q ",false);
        }
        Supplier supplier = optionalSupplier.get();
        supplierRepository.delete(supplier);
        return new Result("Supplier o'chirildi",true);
    }

}
