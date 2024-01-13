package com.kcs.kubebench;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class KubeBenchFromHumanResultParser {

  private static final String CHECK_LINE_START = "[";

  public static KubeBenchFromHumanResult parse(String input) {
    var checksMap = new HashMap<String, KubeBenchFromHumanResult.CheckDto>();
    var currentRemediation = new StringBuilder();
    String currentRemediationIdentifier = null;
    var categories = new HashMap<String, KubeBenchFromHumanResult.CheckCategoryDto>();
    var groups = new HashMap<String, KubeBenchFromHumanResult.CheckGroupDto>();

    for (String line : input.lines().toList()) {
      if (line.startsWith(CHECK_LINE_START)) {
        handleCheckLine(line, categories, groups, checksMap);
      } else if (checksMap.containsKey(getFirstWord(line))) {
        currentRemediationIdentifier = handleRemediationLine(line, checksMap, currentRemediationIdentifier, currentRemediation);
      } else if (line.isBlank()) {
        setCurrentRemediation(checksMap, currentRemediationIdentifier, currentRemediation);
      }
    }

    checksMap.forEach((id, value) -> {
      var groupId = getGroupIdFromCheckId(id);
      groups.get(groupId).getChecks().add(value);
    });

    groups.forEach((id, value) -> {
      var categoryId = getCategoryIdFromGroupId(id);
      categories.get(categoryId).getGroups().add(value);
    });

    return new KubeBenchFromHumanResult(categories.values().stream().toList());
  }

  private static String getCategoryIdFromGroupId(String groupId) {
    return groupId.substring(0, groupId.lastIndexOf("."));
  }

  private static String getGroupIdFromCheckId(String checkId) {
    return checkId.substring(0, checkId.lastIndexOf("."));
  }

  @Nullable
  private static String handleRemediationLine(String line, Map<String, KubeBenchFromHumanResult.CheckDto> checksMap, String currentRemediationIdentifier, StringBuilder currentRemediation) {
    setCurrentRemediation(checksMap, currentRemediationIdentifier, currentRemediation);
    currentRemediationIdentifier = getFirstWord(line);
    currentRemediation.append(line.replace(currentRemediation, ""));
    return currentRemediationIdentifier;
  }

  private static void handleCheckLine(String line, HashMap<String, KubeBenchFromHumanResult.CheckCategoryDto> categories, HashMap<String, KubeBenchFromHumanResult.CheckGroupDto> groups, HashMap<String, KubeBenchFromHumanResult.CheckDto> checksMap) {
    var identifier = getSecondWord(line);
    if (isCategory(identifier)) {
      categories.put(identifier, extractCategoryFromLine(line));
    } else if (isGroup(identifier)) {
      groups.put(identifier, (extractGroupFromLine(line)));
    } else if (isCheck(identifier)) {
      var check = extractCheckFromLine(line);
      checksMap.put(check.getIdentifier(), check);
    }
  }

  private static boolean isCheck(String input) {
    return Pattern.matches("^\\d+\\.\\d+\\.\\d+$", input);
  }

  private static boolean isGroup(String input) {
    return Pattern.matches("^\\d+\\.\\d+$", input);
  }

  private static boolean isCategory(String input) {
    return Pattern.matches("^\\d+$", input);
  }

  private static void setCurrentRemediation(Map<String, KubeBenchFromHumanResult.CheckDto> checksMap, String currentRemediationIdentifier, StringBuilder currentRemediation) {
    if (currentRemediationIdentifier != null && !currentRemediation.isEmpty()) {
      checksMap.get(currentRemediationIdentifier).setRemediation(currentRemediation.toString());
      currentRemediation.setLength(0);
    }
  }

  private static KubeBenchFromHumanResult.CheckGroupDto extractGroupFromLine(String input) {
    var pattern = Pattern.compile("\\[(.*?)\\] ((?:\\d+(?:\\.\\d+){0,2})) ?(.*)");
    var matcher = pattern.matcher(input);
    if (matcher.find()) {
      var identifier = matcher.group(2);
      var title = matcher.group(3);
      return new KubeBenchFromHumanResult.CheckGroupDto(identifier, title);
    } else {
      throw new IllegalArgumentException("The provided string doesn't match the expected format");
    }
  }

  private static KubeBenchFromHumanResult.CheckCategoryDto extractCategoryFromLine(String input) {
    var pattern = Pattern.compile("\\[(.*?)\\] ((?:\\d+(?:\\.\\d+){0,2})) ?(.*)");
    var matcher = pattern.matcher(input);
    if (matcher.find()) {
      var identifier = matcher.group(2);
      var title = matcher.group(3);
      return new KubeBenchFromHumanResult.CheckCategoryDto(identifier, title);
    } else {
      throw new IllegalArgumentException("The provided string doesn't match the expected format");
    }
  }

  private static KubeBenchFromHumanResult.CheckDto extractCheckFromLine(String input) {
    var pattern = Pattern.compile("\\[(.*?)\\] ((?:\\d+(?:\\.\\d+){0,2})) ?(.*)");
    var matcher = pattern.matcher(input);
    if (matcher.find()) {
      var identifier = matcher.group(1);
      var severity = matcher.group(2);
      var title = matcher.group(3);
      return new KubeBenchFromHumanResult.CheckDto(severity, identifier, title);
    } else {
      throw new IllegalArgumentException("The provided string doesn't match the expected format");
    }
  }

  private static String getSecondWord(String str) {
    if (str == null || str.isEmpty()) {
      return "";
    }

    var words = str.split("\\s+");
    if (words.length < 2) {
      return "";
    }

    return words[1];
  }

  private static String getFirstWord(String input) {
    var pattern = Pattern.compile("\\S+");
    var matcher = pattern.matcher(input);

    if (matcher.find()) {
      return matcher.group();
    } else {
      return null;
    }
  }
}

