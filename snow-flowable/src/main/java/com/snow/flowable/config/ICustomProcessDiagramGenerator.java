package com.snow.flowable.config;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.image.ProcessDiagramGenerator;

import java.awt.*;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface ICustomProcessDiagramGenerator extends ProcessDiagramGenerator {

    InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities,
                                List<String> highLightedFlows, String activityFontName, String labelFontName, String annotationFontName,
                                ClassLoader customClassLoader, double scaleFactor, Color[] colors, Set<String> currIds);
}
