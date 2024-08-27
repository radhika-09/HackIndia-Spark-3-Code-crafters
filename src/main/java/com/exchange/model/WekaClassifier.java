package com.exchange.model;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

public class WekaClassifier {
    private Classifier classifier;
    private Instances dataStructure;
    private static final Logger logger = Logger.getLogger(WekaClassifier.class.getName());

    public WekaClassifier(String modelPath) throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(modelPath)) {
            classifier = (Classifier) SerializationHelper.read(fileInputStream);
        }
        createDataStructure();
    }

    private void createDataStructure() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("ItemName", (ArrayList<String>) null)); // String attribute
        attributes.add(new Attribute("Brand", (ArrayList<String>) null));    // String attribute
        attributes.add(new Attribute("Model", (ArrayList<String>) null));    // String attribute

        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("Electronics");
        classValues.add("Clothing");
        classValues.add("Furniture");

        attributes.add(new Attribute("category", classValues)); // Nominal attribute

        dataStructure = new Instances("PredictCategory", attributes, 0);
        dataStructure.setClassIndex(dataStructure.numAttributes() - 1);
    }

    public String classify(String prodName, String brand, String model) throws Exception {
        if (prodName == null || brand == null || model == null) {
            throw new IllegalArgumentException("Input values cannot be null");
        }

        DenseInstance instance = new DenseInstance(dataStructure.numAttributes());
        instance.setValue(dataStructure.attribute("ItemName"), prodName);
        instance.setValue(dataStructure.attribute("Brand"), brand);
        instance.setValue(dataStructure.attribute("Model"), model);
        instance.setDataset(dataStructure);

        // Log instance details
        logger.info("Instance to classify: " + instance.toString());

        double classIndex = classifier.classifyInstance(instance);
        return dataStructure.classAttribute().value((int) classIndex);
    }
}
