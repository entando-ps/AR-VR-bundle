package it.iedx.login.domain;

import com.fasterxml.jackson.annotation.JsonValue;

// Stato dell'assegnazione
public enum LeaseStatus {
    PENDING, // in attesa di essere utilizzato
    INUSE,  // in utilizzo
    RELEASED, // l'access code è stato consumato
    EXTERNAL; // come pending ma non viene utilizzato il visore e quindi ipad, mobile etc. etc.

    @JsonValue
    public int toValue() {
        return ordinal();
    }

}
