package it.iedx.login.domain;


import com.fasterxml.jackson.annotation.JsonValue;


public enum DeviceStatus {
    UNAVAILABLE,    // non può essere allocato (es. rotto)
    AVAILABLE;      // disponibile per assegnazione

    @JsonValue
    public int toValue() {
        return ordinal();
    }

/*
    AVAILABLE("AVAILABLE"),
    BUSY("BUSY"),
    UNAVAILABLE("UNAVAILABLE");
    private final String status;

    private static Map<String, DeviceStatus> VALUES_MAP =
        Stream.of(DeviceStatus.values()).collect(Collectors.toMap(s -> s.status, Function.identity()));

    DeviceStatus(String status) {
        this.status = status;
    }

    @JsonCreator
    public static DeviceStatus fromString(String status) {
        return Optional.ofNullable(VALUES_MAP.get(status))
            .orElseThrow(() -> new IllegalArgumentException(status));
    }
*/
}
