package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Currency;
import org.example.appwarehouse.payload.CategoryDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @PostMapping
    public Result addCurrency(@RequestBody Currency currency ) {
       Result result =  currencyService.addCurrency(currency);
       return result;
    };

    @GetMapping("/list")
    public List<Currency> listCurrency() {
        List<Currency> currencyList = currencyService.getCurrencyList();
        return currencyList;
    }

    @PutMapping("/{id}")
    public Result updateCurrency(@RequestBody Currency currency, @PathVariable Integer id) {
        Result result = currencyService.updateCurrency(id,currency);
        return result;
    }

    @DeleteMapping("/{id}")
    public Result deleteCurrency(@PathVariable Integer id) {
        Result result = currencyService.deleteCurrency(id);
        return result;
    };
}
