package com.gestion.service;

import com.gestion.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class HolidayService {

    @Autowired
    HolidayRepository holidayRepository;

    public HolidayRepository getRepository(){
        return holidayRepository;
    }
}
