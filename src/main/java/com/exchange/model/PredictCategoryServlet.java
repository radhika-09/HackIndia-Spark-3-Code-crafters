package com.exchange.model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Enumeration;

@WebServlet("/predictCategory")
public class PredictCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WekaClassifier wekaClassifier;
    private static final Logger logger = Logger.getLogger(PredictCategoryServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            String modelPath = getServletContext().getRealPath("/accuracy.model");
            logger.info("Model Path: " + modelPath);

            File modelFile = new File(modelPath);
            if (!modelFile.exists()) {
                throw new ServletException("Model file not found at " + modelPath);
            }

            wekaClassifier = new WekaClassifier(modelPath);
        } catch (Exception e) {
            logger.severe("Failed to initialize WekaClassifier: " + e.getMessage());
            throw new ServletException("Failed to initialize WekaClassifier", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Log all request parameters
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            logger.info("Parameter: " + paramName + " = " + paramValue);
        }

        String prodName = request.getParameter("prodname");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");

        logger.info("Received parameters - Product Name: " + prodName + ", Brand: " + brand + ", Model: " + model);

        if (prodName == null || brand == null || model == null) {
            logger.severe("One or more input parameters are null.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input parameters");
            return;
        }

        String category;
        try {
            category = wekaClassifier.classify(prodName, brand, model);
        } catch (Exception e) {
            logger.severe("Error predicting category: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error predicting category");
            return;
        }

        response.setContentType("text/plain");
        response.getWriter().write(category);
    }
}
