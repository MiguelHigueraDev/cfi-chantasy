package dev.miguelhiguera.chantasy.repositories;

import dev.miguelhiguera.chantasy.entities.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CountryRepositoryTests {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void CountryRepository_SaveAll_ReturnSavedCountries() {
        Country country = this.getFirstCountry();

        Country savedCountry = countryRepository.save(country);

        assertThat(savedCountry.getId()).isNotNull();
        assertThat(savedCountry.getId()).isGreaterThan(0);
        assertThat(savedCountry.getName()).isEqualTo(country.getName());
    }

    @Test
    void CountryRepository_Update_ReturnUpdatedCountry() {
        Country country = this.getFirstCountry();

        Country savedCountry = countryRepository.save(country);

        savedCountry.setName("USA");
        savedCountry.setCode("USA");
        savedCountry.setFlagUrl("https://flag.com/usa");

        Country updatedCountry = countryRepository.save(savedCountry);

        assertThat(updatedCountry.getId()).isEqualTo(savedCountry.getId());
        assertThat(updatedCountry.getName()).isEqualTo("USA");
        assertThat(updatedCountry.getCode()).isEqualTo("USA");
        assertThat(updatedCountry.getFlagUrl()).isEqualTo("https://flag.com/usa");
    }

    @Test
    void CountryRepository_GetAll_ReturnMoreThanOneCountry() {
        Country country = this.getFirstCountry();

        Country country2 = Country.builder()
                .name("USA")
                .code("USA")
                .flagUrl("https://flag.com/usa").build();

        countryRepository.save(country);
        countryRepository.save(country2);

        Iterable<Country> iterator = countryRepository.findAll();
        List<Country> countryList = StreamSupport.stream(iterator.spliterator(), false).toList();

        assertThat(countryList).isNotNull();
        assertThat(countryList.size()).isEqualTo(2);
        assertThat(countryList.getFirst().getName()).isEqualTo("Mexico");
        assertThat(countryList.get(1).getName()).isEqualTo("USA");
    }

    @Test
    void CountryRepository_FindById_ReturnCountry() {
        Country country = this.getFirstCountry();

        countryRepository.save(country);

        Country foundCountry = countryRepository.findById(country.getId()).get();
        assertThat(foundCountry).isNotNull();
        assertThat(foundCountry.getName()).isEqualTo(country.getName());
    }

    @Test
    void CountryRepository_FindByName_ReturnCountry() {
        Country country = this.getFirstCountry();

        countryRepository.save(country);

        Country foundCountry = countryRepository.findByName(country.getName()).get();
        assertThat(foundCountry).isNotNull();
        assertThat(foundCountry.getName()).isEqualTo(country.getName());
    }

    @Test
    void CountryRepository_Delete_ReturnEmptyOptional() {
        Country country = this.getFirstCountry();

        countryRepository.save(country);

        countryRepository.delete(country);

        assertThat(countryRepository.findById(country.getId())).isEmpty();
    }

    private Country getFirstCountry() {
        return Country.builder()
                .name("Mexico")
                .code("MEX")
                .flagUrl("https://flag.com/mexico").build();
    }
}
