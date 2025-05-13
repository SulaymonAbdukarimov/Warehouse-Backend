package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Currency;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public Result addCurrency(Currency currency) {
        boolean exists = currencyRepository.existsByName(currency.getName());
        if (exists) {
            return new Result("Bundey currency mavjud",false);
        }
        currencyRepository.save(currency);
        return new Result("Yangi currency saqlnadi",true);
    };

    public List<Currency> getCurrencyList() {
        return currencyRepository.findAll();
    }

    public Result deleteCurrency(Integer id) {
        Optional<Currency> currencyOptional = currencyRepository.findById(id);
        if (!currencyOptional.isPresent()) {
            return new Result("Bundey currency mavjud emas",false);
        }
        currencyRepository.deleteById(id);
        return new Result("Currency o'chirildi",true);
    };

    public Result updateCurrency(Integer id, Currency currency) {
        Optional<Currency> currencyOptional = currencyRepository.findById(id);
        if (!currencyOptional.isPresent()) {
            return new Result("Bundey currency mavjud emas",false);
        }
        currencyRepository.save(currency);
        return new Result("Currency o'zgartirildi",true);
    };
}
