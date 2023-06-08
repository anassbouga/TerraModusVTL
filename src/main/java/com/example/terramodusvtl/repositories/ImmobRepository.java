package com.example.terramodusvtl.repositories;
import com.example.terramodusvtl.entities.Immob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImmobRepository extends JpaRepository<Immob,Long> {
}
