package esprit.tn.service;

import  esprit.tn.entity.Country;

import java.util.List;

public interface CountryService {
    Country getCountryById(Long id);
    Country getCountryByName(String name);
    List<Country> getCountryList();
}
