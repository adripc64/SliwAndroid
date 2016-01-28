package es.uji.al259348.sliwandroid.core.model;

public class Config {

    private static String[] steps = {
            "Siéntate en tu sitio para ver TV",
            "Túmbate en la cama",
            "Ve a la puerta del frigorífico",
            "Ve al sitio en el que cocinas",
            "Ve al cuarto de baño"
    };

    private static int currentStep = 0;

    public static void startConfig() {
        currentStep = 0;
    }

    public static boolean hasFinished() {
        return currentStep >= steps.length;
    }

    public static String getCurrentStepMsg() {
        if (hasFinished()) return null;
        return steps[currentStep];
    }

    public static void next() {
        currentStep++;
    }

}
