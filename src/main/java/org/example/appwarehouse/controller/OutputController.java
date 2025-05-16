package org.example.appwarehouse.controller;

import org.example.appwarehouse.payload.InputDto;
import org.example.appwarehouse.payload.OutputRequestDto;
import org.example.appwarehouse.payload.OutputResponseDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.OutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/output")
public class OutputController {

    @Autowired
    OutputService outputService;

    @GetMapping("/list")
    public List<OutputResponseDto> getOutputs() {
        List<OutputResponseDto> responseDtoList = outputService.getOutputs();
        return responseDtoList;
    }

    @GetMapping("/{id}")
    public OutputResponseDto getOutput(@PathVariable Integer id) {
        OutputResponseDto responseDto = outputService.getOutputById(id);
        return responseDto;
    }

    @PostMapping
    public Result addOutput(@RequestBody OutputRequestDto outputRequestDto) {
        Result result = outputService.addOutput(outputRequestDto);
        return result;
    };

    @PutMapping("/{id}")
    public Result updateOutput(@PathVariable Integer id, @RequestBody OutputRequestDto outputRequestDto) {
        Result result = outputService.updateOutput(id,outputRequestDto);
        return result;
    }


    @DeleteMapping("/{id}")
    public Result deleteOutput(@PathVariable Integer id) {
        Result result = outputService.deleteOutput(id);
        return result;
    }


}
