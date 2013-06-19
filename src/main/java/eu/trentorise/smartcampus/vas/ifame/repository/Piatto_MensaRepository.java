package eu.trentorise.smartcampus.vas.ifame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.Piatto_Mensa;

@Repository
public interface Piatto_MensaRepository extends
		JpaRepository<Piatto_Mensa, Long> {

}
