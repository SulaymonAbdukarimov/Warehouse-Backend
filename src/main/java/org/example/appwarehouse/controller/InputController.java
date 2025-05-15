package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Input;
import org.example.appwarehouse.payload.InputDto;
import org.example.appwarehouse.payload.InputResponseDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/input")
public class InputController {
    @Autowired
    private InputService inputService;

    @GetMapping("/list")
    public List<InputResponseDto> getAllInputs() {
        List<InputResponseDto> inputList = inputService.getInputProducts();
        return inputList;
    };

    @GetMapping("/{id}")
    public InputResponseDto getInput(@PathVariable Integer id) {
        return inputService.getInput(id);
    }

    @PostMapping
    public Result addInput(@RequestBody InputDto inputDto) {
        Result result = inputService.addInput(inputDto);
        return result;
    }

    @PutMapping("/{id}")
    public Result updateInput( @PathVariable Integer id, @RequestBody InputDto inputDto) {
        Result result = inputService.updateInput(id,inputDto);
        return result;
    }

    @DeleteMapping("/{id}")
    public Result deleteInput(@PathVariable Integer id) {
        Result result = inputService.deleteInput(id);
        return result;
    };
}
