package com.kcs.score;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class KubeScoreResultParser {
  private static final String OBJECT_LINE_START = "v1/";
  private static final String VULNERABILITY_GROUP_LINE_START = "[";
  private static final String VULNERABILITY_LINE_START = "·";

  // wish this could be done in a more intuitive way but afraid it can't
  public static KubeScoreResultDto parse(String input) {
    var objectsList = new ArrayList<KubeScoreResultDto.ObjectResultDto>();
    var resultDto = new KubeScoreResultDto(objectsList);
    var linesIterator = input.lines().iterator();
    var currentVulnerabilityTypes = new ArrayList<KubeScoreResultDto.VulnerabilityTypeDto>();
    var currentVulnerabilityInstances = new ArrayList<KubeScoreResultDto.VulnerabilityInstanceDto>();
    var currentVulnerabilityRemediation = new StringBuilder();
    KubeScoreResultDto.ObjectResultDto currentObject = null;
    KubeScoreResultDto.VulnerabilityTypeDto currentVulnerabilityType = null;
    KubeScoreResultDto.VulnerabilityInstanceDto currentVulnerabilityInstance = null;

    while (linesIterator.hasNext()) {
      var line = sanitizeWhitespaces(linesIterator.next());
      if (line.startsWith(OBJECT_LINE_START)) {
        currentVulnerabilityRemediation = setCurrentRemediation(currentVulnerabilityInstance, currentVulnerabilityRemediation);
        currentVulnerabilityInstance = addCurrentVulnerabilityInstance(currentVulnerabilityInstance, currentVulnerabilityInstances);
        currentVulnerabilityType = addCurrentVulnerabilityType(currentVulnerabilityType, currentVulnerabilityInstances, currentVulnerabilityTypes);
        addCurrentObjectToList(currentObject, currentVulnerabilityTypes, objectsList);
        currentVulnerabilityInstances = new ArrayList<>();
        currentVulnerabilityTypes = new ArrayList<>();
        currentObject = extractObjectPropertiesFromStartLine(line);
      } else if (line.startsWith(VULNERABILITY_GROUP_LINE_START)) {
        currentVulnerabilityRemediation = setCurrentRemediation(currentVulnerabilityInstance, currentVulnerabilityRemediation);
        currentVulnerabilityInstance = addCurrentVulnerabilityInstance(currentVulnerabilityInstance, currentVulnerabilityInstances);
        addCurrentVulnerabilityType(currentVulnerabilityType, currentVulnerabilityInstances, currentVulnerabilityTypes);
        currentVulnerabilityInstances = new ArrayList<>();
        currentVulnerabilityType = extractVulnerabilityTitleAndSeverity(line);
      } else if (line.startsWith(VULNERABILITY_LINE_START)) {
        currentVulnerabilityRemediation = setCurrentRemediation(currentVulnerabilityInstance, currentVulnerabilityRemediation);
        addCurrentVulnerabilityInstance(currentVulnerabilityInstance, currentVulnerabilityInstances);
        currentVulnerabilityInstance = new KubeScoreResultDto.VulnerabilityInstanceDto(sanitizeWhitespaces(line));
      } else {
        currentVulnerabilityRemediation.append(line);
      }
    }

    if (currentObject != null) {
      currentObject.setVulnerabilities(currentVulnerabilityTypes);
      objectsList.add(currentObject);
    }
    return resultDto;
  }

  private static void addCurrentObjectToList(KubeScoreResultDto.ObjectResultDto currentObject, ArrayList<KubeScoreResultDto.VulnerabilityTypeDto> currentVulnerabilityGroups, ArrayList<KubeScoreResultDto.ObjectResultDto> objects) {
    if (currentObject != null) {
      currentObject.setVulnerabilities(currentVulnerabilityGroups);
      objects.add(currentObject);
    }
  }

  private static StringBuilder setCurrentRemediation(KubeScoreResultDto.VulnerabilityInstanceDto currentVulnerability, StringBuilder currentVulnerabilityRemediation) {
    if (currentVulnerability != null && !currentVulnerabilityRemediation.isEmpty()) {
      currentVulnerability.setRemediation(sanitizeWhitespaces(currentVulnerabilityRemediation.toString()));
      currentVulnerabilityRemediation = new StringBuilder();
    }
    return currentVulnerabilityRemediation;
  }

  private static KubeScoreResultDto.VulnerabilityInstanceDto addCurrentVulnerabilityInstance(KubeScoreResultDto.VulnerabilityInstanceDto currentVulnerability, ArrayList<KubeScoreResultDto.VulnerabilityInstanceDto> currentVulnerabilities) {
    if (currentVulnerability != null) {
      currentVulnerabilities.add(currentVulnerability);
    }
    return null;
  }

  private static KubeScoreResultDto.VulnerabilityTypeDto addCurrentVulnerabilityType(KubeScoreResultDto.VulnerabilityTypeDto currentVulnerabilityGroup, ArrayList<KubeScoreResultDto.VulnerabilityInstanceDto> currentVulnerabilities, ArrayList<KubeScoreResultDto.VulnerabilityTypeDto> currentVulnerabilityGroups) {
    if (currentVulnerabilityGroup != null) {
      currentVulnerabilityGroup.setInstances(currentVulnerabilities);
      currentVulnerabilityGroups.add(currentVulnerabilityGroup);
    }
    return null;
  }

  private static String sanitizeWhitespaces(String input) {
    var noNewlines = input.replaceAll("\\r?\\n", " ");
    var noExtraSpaces = noNewlines.replaceAll("\\s+", " ");
    return noExtraSpaces.trim();
  }

  private static KubeScoreResultDto.VulnerabilityTypeDto extractVulnerabilityTitleAndSeverity(String input) {
    var patternString = "\\s*\\[([a-zA-Z0-9-_]+)\\]\\s+(.*)";
    var pattern = Pattern.compile(patternString);
    var matcher = pattern.matcher(input);
    if (matcher.find()) {
      var severity = matcher.group(1);
      var title = matcher.group(2);
      return new KubeScoreResultDto.VulnerabilityTypeDto(severity, title);
    } else {
      throw new RuntimeException();
    }
  }

  private static KubeScoreResultDto.ObjectResultDto extractObjectPropertiesFromStartLine(String input) {
    var patternString = "v1\\/([a-zA-Z0-9-_]+)\\s+([a-zA-Z0-9-_]+)\\s+in\\s+([a-zA-Z0-9-_]+)";
    var pattern = Pattern.compile(patternString);
    var matcher = pattern.matcher(input);
    if (matcher.find()) {
      var kind = matcher.group(1);
      var name = matcher.group(2);
      var namespace = matcher.group(3);
      return new KubeScoreResultDto.ObjectResultDto(kind, name, namespace);
    } else {
      throw new RuntimeException();
    }
  }
}
