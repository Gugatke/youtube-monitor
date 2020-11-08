package com.gsg.youtubemonitor.repository;

import com.gsg.youtubemonitor.model.CountryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryDataRepository extends JpaRepository<CountryData, Integer> {
    void deleteByOwnerUserId(int ownerUserId);

    CountryData findByOwnerUserId(int ownerUserId);
}
