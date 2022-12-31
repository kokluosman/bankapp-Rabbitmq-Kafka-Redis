package com.example.bankapp.api;

import com.example.bankapp.dto.CitiesDto;
import com.example.bankapp.dto.CitiesDtoConverter;
import com.example.bankapp.model.Cities;
import com.example.bankapp.service.CitiesManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/cities")
@RequiredArgsConstructor
public class CitiesController {

    private final CitiesManager citiesManager;
    private final CitiesDtoConverter citiesDtoConverter;

    @GetMapping(path = "/getAllCity")
    public ResponseEntity<List<CitiesDto>> getAllCities(){
        return ResponseEntity.ok(this.citiesManager.getAllCities().stream()
                .map(citiesDtoConverter::convert).collect(Collectors.toList()));
    }

    @PostMapping(path = "/createCities")
    public ResponseEntity<CitiesDto> createCities(@RequestBody Cities cities){
        return ResponseEntity.ok(this.citiesManager.createCities(cities));
    }

    @PutMapping(path = "/updateCities")
    public ResponseEntity<CitiesDto> updateCities(@RequestParam Long id,@RequestBody Cities cities){
        CitiesDto citiesDto = this.citiesManager.updateCities(id, cities);
        return ResponseEntity.ok(citiesDto);
    }

}
