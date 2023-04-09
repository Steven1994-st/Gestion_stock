package com.gestion.services;

import com.gestion.repositories.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class HolidayService {

    @Autowired
    HolidayRepository holidayRepository;

    public HolidayRepository getRepository(){
        return holidayRepository;
    }
}
