package com.example.bankapp.service;

import com.example.bankapp.dto.CitiesDto;
import com.example.bankapp.dto.CitiesDtoConverter;
import com.example.bankapp.model.Cities;
import com.example.bankapp.repo.CitiesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitiesManager {

    private final CitiesRepo citiesRepo;
    private final CitiesDtoConverter citiesDtoConverter;

    public List<Cities> getAllCities(){
        return new ArrayList<Cities>((Collection<?extends Cities>) citiesRepo.findAll());
    }

    public CitiesDto getCitiesById(Long id){
        Optional<Cities> byId = this.citiesRepo.findById(id);
        return citiesDtoConverter.convert(byId.orElseThrow(() ->
                new RuntimeException("Could not find with following id:" + id)));
    }

    public CitiesDto createCities(Cities cities){
        final var data = this.citiesRepo.save(cities);
        return citiesDtoConverter.convert(data);
    }

    public CitiesDto updateCities(Long id,Cities cities){
        Cities cities1 = this.citiesRepo.findById(id).get();
        cities1.setPlateCode(cities.getPlateCode());
        cities1.setName(cities.getName());
        this.citiesRepo.save(cities1);
        return citiesDtoConverter.convert(cities1);
    }

    public void deleteCities(Long id){
        Cities cities = this.citiesRepo.findById(id).get();
        this.citiesRepo.delete(cities);
    }

}
