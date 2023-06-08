package com.example.terramodusvtl.repositories.devisPersonne;
import com.example.terramodusvtl.entities.devisPersonne.DevisPersonneOpci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevisPersonneOpciRepository extends JpaRepository<DevisPersonneOpci,Long> {

}
