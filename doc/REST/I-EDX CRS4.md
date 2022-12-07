# Project: I-EDX CRS4

## Richiesta accesscode
Richiede la creazione di un access code associato al profilo desiderato.
Come risultato si ottiene il codice creato e, se disponibile, il messaggio relativo al dispositivo che è stato assegnato per la fruizione dell'esperienza.
### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/accesscode
>```
### Body (**raw**)

```json
{
    "age": "CHILD",
    "gender": "FEMALE",
    "race": "EAST_ASIAN",
    "sentiment": "NEUTRAL"
}
```

### Body formdata

| Param     | value                                                                             | Type   |
|-----------|-----------------------------------------------------------------------------------|--------|
| age       | CHILD, TEEN, YOUNG_ADULT, ADULT, ELDER                                            | string |
| gender    | MALE, FEMALE, UNKNOWN                                                             | string |
| race      | EAST_ASIAN, SOUTHEAST_ASIAN, INDIAN, BLACK, WHITE, MIDDLE_EASTERN, LATINO_ISPANIC | string |
| sentiment | ANGRY, DISGUST, FEAR, HAPPY, SAD, SURPRISE, NEUTRAL                               | string |


### Response: 200
```json
{
    "code": "YIXO",
    "message": "postazione 2",
    "qrcode": "iedx://accesscode?YIXO"
}
```

`message` è il messaggio human readable associato al dispositivo che è stato assegnato all'utente. Quando non viene trovato un dispositivo libero il valore restituito è `null`  
`qrcode` rappresenta la stringa che deve essere trasformata in QR code


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

