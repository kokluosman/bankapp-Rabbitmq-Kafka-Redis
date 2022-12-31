package com.example.bankapp.dto;

import com.example.bankapp.model.Cities;
import org.springframework.stereotype.Component;

@Component
public class CitiesDtoConverter {

    public CitiesDto convert(Cities cities){
        CitiesDto citiesDto = new CitiesDto();
        citiesDto.setId(citiesDto.getId());
        citiesDto.setName(citiesDto.getName());
        citiesDto.setPlateCode(citiesDto.getPlateCode());
        return citiesDto;
    }
}
