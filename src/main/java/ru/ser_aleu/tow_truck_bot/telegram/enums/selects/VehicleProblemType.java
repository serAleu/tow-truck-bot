package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum VehicleProblemType implements Selectable<VehicleProblemType> {

    NO_PROBLEMS("новый или с пробегом, заводится и едет, неисправностей нет"),
    HAS_ISSUES("заводится и едет, но есть неисправность"),
    WONT_START_CAN_NEUTRAL("не заводится, можно перевести КПП в нейтраль и снять ручной тормоз"),
    WONT_START_CANT_NEUTRAL("не заводится, нельзя перевести в нейтраль и снять ручной тормоз"),
    DAMAGED_IN_ACCIDENT("повреждён в ДТП");

    private static final Map<String, VehicleProblemType> DESCRIPTION_TO_ENUM_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            VehicleProblemType::getDescription,
                            Function.identity()
                    ));

    VehicleProblemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Map<String, VehicleProblemType> getDescriptionToEnumMap() {
        return DESCRIPTION_TO_ENUM_MAP;
    }

    public static VehicleProblemType fromDescription(String description) {
        VehicleProblemType result = DESCRIPTION_TO_ENUM_MAP.get(description);
        if (result == null) {
            throw new IllegalArgumentException("Unknown vehicle problem type: " + description);
        }
        return result;
    }

    public static String getAllDescriptionsAsString() {
        return Arrays.stream(values())
                .map(VehicleProblemType::getDescription)
                .collect(Collectors.joining(";"));
    }

    public static VehicleProblemType parseFromString(String input) {
        if (input == null || !input.startsWith("PROBLEM_TYPE=")) {
            throw new IllegalArgumentException("Invalid input format. Expected 'PROBLEM_TYPE=TYPE'");
        }

        String enumName = input.substring("PROBLEM_TYPE=".length());
        try {
            return VehicleProblemType.valueOf(enumName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown vehicle problem type: " + enumName);
        }
    }
}
