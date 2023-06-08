package com.example.terramodusvtl.repositories;
import com.example.terramodusvtl.entities.devisSte.DemandeurSte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeurSteRepository extends JpaRepository<DemandeurSte,Long> {
}
