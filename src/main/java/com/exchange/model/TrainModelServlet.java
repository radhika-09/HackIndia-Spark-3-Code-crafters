package com.exchange.model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

@WebServlet("/trainModel")
public class TrainModelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TrainModelServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String csvPath = getServletContext().getRealPath("/accuracy.csv");
        String modelPath = getServletContext().getRealPath("/accuracy.model");

        try (FileReader reader = new FileReader(csvPath)) {
            Instances data = new Instances(reader);
            data.setClassIndex(data.numAttributes() - 1);

            // Log data summary
            logger.info("Training data summary:\n" + data.toSummaryString());

            // Apply StringToWordVector filter to convert string attributes to vectors
            StringToWordVector filter = new StringToWordVector();
            filter.setInputFormat(data);
            Instances filteredData = Filter.useFilter(data, filter);

            Classifier classifier = new J48(); // Using J48 classifier as an example
            classifier.buildClassifier(filteredData);

            // Log classifier summary
            logger.info("Classifier built: " + classifier.toString());

            // Save the trained model
            SerializationHelper.write(modelPath, classifier);

            logger.info("Model trained and saved successfully at " + modelPath);
            response.setContentType("text/plain");
            response.getWriter().write("Model trained and saved successfully");
        } catch (Exception e) {
            logger.severe("Failed to train and save the model: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to train and save the model");
        }
    }
}
