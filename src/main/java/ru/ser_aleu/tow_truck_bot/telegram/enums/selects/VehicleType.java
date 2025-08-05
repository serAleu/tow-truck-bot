package ru.ser_aleu.tow_truck_bot.telegram.enums.selects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleType implements Selectable<VehicleType> {
    MOTORBIKE("мотоцикл"),
    PASSENGER_CAR("легковой автомобиль"),
    CROSSOVER("кроссовер"),
    SUV_OR_MINIBUS("внедорожник/джип/микроавтобус"),
    PREMIUM_OR_SPORTS_CAR("автомобиль премиум класса или спорткар"),
    COMMERCIAL_VEHICLE("коммерческий транспорт"),
    SPECIAL_EQUIPMENT("спецтехника");

    private final String displayedName;

    @Override
    public String getDisplayName() {
        return displayedName;
    }

//    private static final Map<String, VehicleType> DISPLAYED_NAMES_MAP = Arrays.stream(values())
//                    .collect(Collectors.toMap(
//                            VehicleType::getDisplayedName,
//                            Function.identity()
//                    ));
//
//    public static Map<String, VehicleType> getDisplayedNamesMap() {
//        return DISPLAYED_NAMES_MAP;
//    }
//
//    public static VehicleType getCarTypeByDisplayedName(String displayedName) {
//        return DISPLAYED_NAMES_MAP.get(displayedName);
//    }
//
//    public static String getAllDisplayedNamesAsString() {
//        return Arrays.stream(values())
//                .map(VehicleType::getDisplayedName)
//                .collect(Collectors.joining(";"));
//    }
//
//    public static VehicleType parseFromVehicleTypeString(String input) {
//        if (input == null || !input.startsWith("VEHICLE_TYPE=")) {
//            throw new IllegalArgumentException("Invalid input format. Expected 'VEHICLE_TYPE=TYPE'");
//        }
//
//        String enumName = input.substring("VEHICLE_TYPE=".length());
//        try {
//            return VehicleType.valueOf(enumName);
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Unknown vehicle type: " + enumName);
//        }
//    }
}
