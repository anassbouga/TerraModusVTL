package com.example.terramodusvtl.repositories.devisSte;
import com.example.terramodusvtl.entities.devisSte.DevisSteDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevisSteDigitalRepository extends JpaRepository<DevisSteDigital,Long> {

}
