package es.uji.al259348.sliwandroid.core.repositories;

import java.util.List;

import es.uji.al259348.sliwandroid.core.model.Sample;

public interface SampleRepository extends Repository {

    Sample save(Sample sample);

    Sample findById(String id);
    List<Sample> findAll();

}
