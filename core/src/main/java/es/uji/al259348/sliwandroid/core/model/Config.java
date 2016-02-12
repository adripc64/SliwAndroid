package es.uji.al259348.sliwandroid.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private static final int NUM_SAMPLES_PER_STEP = 10;
    private static final int PROGRESS_PER_SAMPLE = 100 / NUM_SAMPLES_PER_STEP;

    public static class ConfigStep {

        private Location location;
        private List<Sample> samples;

        public ConfigStep() {
            super();
            this.location = null;
            this.samples = new ArrayList<>(NUM_SAMPLES_PER_STEP);
        }

        public ConfigStep(Location location) {
            super();
            this.location = location;
            this.samples = new ArrayList<>(NUM_SAMPLES_PER_STEP);
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public List<Sample> getSamples() {
            return samples;
        }

        public void setSamples(List<Sample> samples) {
            this.samples = samples;
        }

        public void addSample(Sample sample) {
            samples.add(sample);
        }

        @JsonIgnore
        public int getProgress() {
            return samples.size() * PROGRESS_PER_SAMPLE;
        }

        @JsonIgnore
        public boolean isCompleted() {
            return samples.size() >= NUM_SAMPLES_PER_STEP;
        }

        @Override
        public String toString() {
            return "ConfigStep{" +
                    "location=" + location +
                    ", samples=" + samples +
                    '}';
        }

    }

    private List<ConfigStep> steps;

    public Config() {
        super();
        this.steps = new ArrayList<>();
    }

    public Config(List<Location> locations) {
        super();
        this.steps = new ArrayList<>(locations.size());
        for (Location location : locations) {
            ConfigStep step = new ConfigStep(location);
            steps.add(step);
        }
    }

    public List<ConfigStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ConfigStep> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Config{" +
                "steps=" + steps +
                '}';
    }

}
