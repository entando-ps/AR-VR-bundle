# Project: I-EDX Entando

##Cancellazione esperienza
Cancella una intera esperienza, compresi i file allegati, dato il suo ID univoco

### Method: DELETE
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}
>http://127.0.0.1:8081/services/accesscode/api/experience/7
>```
### Response: 204
```json
null
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Dettaglio asset esperienza
Restituisce i dettagli dell'esperienza compresi i metadati relativi agli scenari dichiarati nel relativo experience script.

Per ciascun asset (scenario, hotpoint etc.) è anche presente un flag `assetPresentOnDisk` che indica se il file sia presente su disco (`true`) o se sia ancora da caricare (`false`).

### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/describe
>http://127.0.0.1:8081/services/accesscode/api/experience/8/describe
>```
>
### Response: 200
```json
{
    "name": "TRE",
    "description": "descrizione esperienza TRE",
    "id": 10,
    "assets": [
        {
            "id": "nice_city",
            "type": 1,
            "assetFilename": "nice_city.mp4",
            "thumbnailFilename": "nice_city.png",
            "assetPresentOnDisk": false,
            "thumbnailPresentOnDisk": false,
            "title": "Nice City"
        },
        {
            "id": "ayutthaya",
            "type": 1,
            "assetFilename": "ayutthaya.mp4",
            "thumbnailFilename": "ayutthaya.png",
            "assetPresentOnDisk": false,
            "thumbnailPresentOnDisk": false,
            "title": "Ayutthaya"
        },
        {
            "id": "doi_suthep",
            "type": 1,
            "assetFilename": "doi_suthep.mp4",
            "thumbnailFilename": "doi_suthep.png",
            "assetPresentOnDisk": false,
            "thumbnailPresentOnDisk": false,
            "title": "Doi Suthep"
        },
        {
            "id": "penguins",
            "type": 1,
            "assetFilename": "penguins.mp4",
            "thumbnailFilename": "penguins.jpg",
            "assetPresentOnDisk": false,
            "thumbnailPresentOnDisk": false,
            "title": "Penguins"
        },
        {
            "id": "hangar",
            "type": 0,
            "assetFilename": "hangar.jpg",
            "thumbnailFilename": "hangar_thumb.jpg",
            "assetPresentOnDisk": true,
            "thumbnailPresentOnDisk": true,
            "title": "Hangar"
        },
        {
            "id": "witcher",
            "type": 0,
            "assetFilename": "witcher.jpg",
            "thumbnailFilename": "witcher_thumb.jpg",
            "assetPresentOnDisk": false,
            "thumbnailPresentOnDisk": false,
            "title": "The Witcher"
        },
        {
            "id": "HotspotTest",
            "type": 2,
            "assetFilename": "ayutthaya.png",
            "thumbnailFilename": null,
            "assetPresentOnDisk": false,
            "thumbnailPresentOnDisk": false,
            "title": "Go to Ayutthaya"
        }
    ],
    "profiles": [
        {
            "age": "YOUNG_ADULT"
        },
        {
            "age": "ADULT"
        }
    ],
    "status": 0
}
```

### Profiles

| Param  | value                                                      | Type    |
|--------|------------------------------------------------------------|---------|
| age    | CHILD, TEEN, YOUNG_ADULT, ADULT, ELDER                     | string  |
| type   | 0=IMAGE, 1=VIDEO, 2=THUMBNAIL, 3=SUBTITLE, 4=DETAILS_PANEL | numeric |
| status | 0 = DRAFT, 1 = PUBLISHED                                   | numeric |

Nota: i profili possono essere comprensivi di `race`, `sentiment` e `gender` tuttavia solo il campo `age` viene usato per cercare gli 
scenari da proporre all'utente. 

### Assets

| Param                  | value                                                                                                  | Type    |
|------------------------|--------------------------------------------------------------------------------------------------------|---------|
| id                     | ID univoco dell' asset                                                                                 | string  |
| type                   | 0=IMAGE, 1=VIDEO, 2=HOTSPOT, 3=SUBTITLE                                                                | numeric |
| assetFilename          | nome del file dell' asset su filesystem; le directory specificate nell'XML non sono riportate          | numeric |
| thumbnailFilename      | nome del file del thumbnail su filesystem; le directory specificate nell'XML non sono riportate        | numeric |
| assetPresentOnDisk     | TRUE = l'asset è stato caricato sul disco FALSE = asset non trovato su filesystem                      | boolean |
| thumbnailPresentOnDisk | TRUE = il thumbnail dell'asset è stato caricato sul disco, FALSE = thumbnail non trovato su filesystem | boolean |
| title                  | Titolo                                                                                                 | string  |


### Response: 404
```json
"Not Found"
```
L'ID dell'esperienza fornita non è valido

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Lista esperienze
Restituisce la lista breve delle esperienze disponibili.

Opzionalmente è possibile specificare in query string il parametro `status` per poter restringere la ricerca alle sole liste con lo stato desiderato.

Per ciascuna esperienza viene restituito il nome e la relativa descrizione.

### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/experiences?status=0
>```
### Query Params


| Param  | value                    | type    |
|--------|--------------------------|---------|
| status | 0 = DRAFT, 1 = PUBLISHED | numeric |


### Response: 200
```json
[
    {
        "name": "UNO",
        "id": 1,
        "description": "descrizione esperienza UNO"
    },
    {
        "name": "DUE",
        "id": 2,
        "description": "descrizione esperienza DUE"
    },
    {
        "name": "TRE",
        "id": 3,
        "description": "descrizione esperienza TRE"
    }
]
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Lista dispositivi
Restituisce la lista dei device presenti sul sistema.

**NOTA**: essendo la lista generalmente contenuta vengono restituiti tutti i dettagli dei singoli device.
### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/devices
>```
### Response: 200
```json
[
    {
        "id": 1,
        "deviceId": "DEVUNO",
        "status": 0,
        "note": "postazione 1",
        "name": "vr1",
        "updated": null,
        "added": "2021-11-10T17:17:46+01:00"
    },
    {
        "id": 2,
        "deviceId": "DEVDUE",
        "status": 0,
        "note": "postazione 2",
        "name": "vr2",
        "updated": null,
        "added": "2021-11-10T17:17:46+01:00"
    },
    {
        "id": 15,
        "deviceId": "DEVTRE",
        "status": 0,
        "note": "postazione 3",
        "name": "vr3",
        "updated": "2021-12-14T17:13:41+01:00",
        "added": "2021-12-14T16:37:14+01:00"
    }
]
```

| Param  | value                    | type    |
|--------|--------------------------|---------|
| status | 0 = DRAFT, 1 = PUBLISHED | numeric |

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Pubblica esperienza
Rendi disponibile un'esperienza per la fruizione da parte degli utenti.

**NOTA**: pubblicare un'esperienza già disponibile non costituisce errore.

### Method: PUT
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/publish
>http://127.0.0.1:8081/services/accesscode/api/experience/5/publish
>```
### Response: 200
```json
true
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Nascondi esperienza
RImuovi un'esperienza dal novero di quelle disponibili per la fruizione da parte degli utenti.

**NOTA**: rimuovere un'esperienza già non disponibile non costituisce errore.

### Method: PUT
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/retire
>http://127.0.0.1:8081/services/accesscode/api/experience/5/retire
>```
### Response: 200
```json
true
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Dettaglio esperienza
Restituisce il record di una esperienza con l'esclusione dei metadati.

**NOTA**: il campo **xml** e **thumbnail** contengono rispettivamente il _nome del file_ dell' experience script e del thumbnail così come salvati nel filesystem;
si sconsiglia la modifica di questi campi.

### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}
>http://127.0.0.1:8081/services/accesscode/api/experience/8
>```
### Response: 200
```json
{
    "id": 8,
    "name": "TRE",
    "description": "descrizione esperienza TRE",
    "path": "1640165968149",
    "status": 0,
    "profiles": {
        "profiles": [
            {
                "age": "YOUNG_ADULT"
            },
            {
                "age": "ELDER"
            }
        ]
    },
    "upload": "2021-12-22T10:39:28+01:00",
    "xml": "config.xml",
    "thumbnail": "Entando_Logo_Dark_Blue@4x.png"
}
```
### Response: 404
```json
"Not found"
```

| Param  | value                    | type    |
|--------|--------------------------|---------|
| status | 0 = DRAFT, 1 = PUBLISHED | numeric |

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Modifica esperienza
Modifica il record di un'esperienza escludendo i metadati (che non possono esser modificati in alcuna maniera)

**NOTA**: la PUT su un record inesistente non produce errori ma non avrà alcun effetto pratico.

### Method: PUT
>```
>http://127.0.0.1:8081/services/accesscode/api/experience
>```
### Body (**raw**)

```json
{
    "id": 8,
    "name": "TRE",
    "description": "descrizione esperienza TRE",
    "path": "1640165968149",
    "status": 0,
    "profiles": {
        "profiles": [
            {
                "age": "YOUNG_ADULT"
            },
            {
                "age": "TEEN"
            }
        ]
    },
    "upload": "2021-12-22T10:39:28+01:00",
    "xml": "config.xml",
    "thumbnail": "Entando_Logo_Dark_Blue@4x.png"
}
```

| Param  | value                                  | Type    |
|--------|----------------------------------------|---------|
| age    | CHILD, TEEN, YOUNG_ADULT, ADULT, ELDER | string  |
| status | 0 = DRAFT, 1 = PUBLISHED               | numeric |

**ATTENZIONE**:  
Cambiare i campi `path`, `xml` e `thumbnail` implica modificare il percorso relativo dove vengono salvati tutti gli asset (path) oppure i nomi sul filesystem
dell' experience script edel thumbnail, rispettivamente. Sebbene sia tecnicamente possibile modificare questi valori la pratica è sconsigliata per i seguenti motivi:
 - bisognerebbe [ricaricare gli asset con l'API apposita](./I-EDX%20Another%20Reality.md#caricamento-di-un-asset-di-una-esperienza)  
 - i file sostituiti non sarebbero cancellati creando quindi garbage sul filesystem.

È preferibile quindi [cancellare](I-EDX%20Entando.md#cancellazione-esperienza) e [ricaricare](I-EDX%20Another%20Reality.md#creazione-di-una-nuova-esperienza) l'esperienza piuttosto che modificare questi valori.

### Response: 200
```json
{
    "id": 8,
    "name": "TRE",
    "description": "descrizione esperienza TRE",
    "path": "1640165968149",
    "status": 0,
    "profiles": {
        "profiles": [
            {
                "age": "YOUNG_ADULT"
            },
            {
                "age": "TEEN"
            }
        ]
    },
    "upload": "2021-12-22T09:39:28Z",
    "xml": "config.xml",
    "thumbnail": "Entando_Logo_Dark_Blue@4x.png"
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Cancellazione device
Cancella un device dato il suo ID univoco

### Method: DELETE
>```
>http://127.0.0.1:8081/services/accesscode/api/device/{ID DEVICE}
>http://127.0.0.1:8081/services/accesscode/api/device/7
>```
### Response: 204
```json
null
```

### Response: 500
```json
"Internal Server Error" 
```

L'errore 500 viene restituito quando l'ID fornito non esiste, es.:

```json
{
    "type": "https://www.jhipster.tech/problem/problem-with-message",
    "title": "Internal Server Error",
    "status": 500,
    "detail": "No class it.iedx.login.domain.Device entity with id 5 exists!",
    "path": "/services/accesscode/api/device/5",
    "message": "error.http.500"
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Dettaglio dispositivo
Carica i dettagli del singolo dispositivo.
### Method: GET
>```
>
>http://127.0.0.1:8081/services/accesscode/api/device/{ID DEVICE}
>http://127.0.0.1:8081/services/accesscode/api/device/1
>```

L'**ID del device** è la chiave primaria dell'entità device sul database, e non deve essere confuso
con l'**ID UNIVOCO** del device usato in sede di richiesta esperienze.

### Response: 200
```json
{
    "id": 1,
    "deviceId": "DEVUNO",
    "status": 0,
    "note": "postazione 1",
    "name": "vr1",
    "updated": null,
    "added": "2021-11-10T17:17:46+01:00"
}
```


| Param  | value                    | type    |
|--------|--------------------------|---------|
| status | 0 = DRAFT, 1 = PUBLISHED | numeric |

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Creazione device
Creazione di un nuovo device.


### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/device
>```
### Body (**raw**)

```json
{
    
    "deviceId": "DEVUNO",
    "status": 0,
    "note": "postazione 1",
    "name": "vr1"
}
```

### Response: 200
```json
{
    "id": 1,
    "deviceId": "DEVUNO",
    "status": 0,
    "note": "postazione 1",
    "name": "vr1",
    "updated": "2022-01-05T11:12:28.152+01:00",
    "added": "2021-11-10T16:17:46Z"
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃


##Modifica device
Modifica il record di un device. In risposta il sistema restituisce il payload inviato.

**NOTA**: la PUT su un record inesistente non produce errori ma non avrà alcun effetto pratico.

### Method: PUT
>```
>http://127.0.0.1:8081/services/accesscode/api/device
>```
### Body (**raw**)

```json
{
    "id": 1,
    "deviceId": "DEVUNO",
    "status": 0,
    "note": "postazione 1",
    "name": "vr1",
    "updated": "2022-01-05T11:12:28.152+01:00",
    "added": "2021-11-10T16:17:46Z"
}
```

### Response: 200
```json
{
    "id": 1,
    "deviceId": "DEVUNO",
    "status": 0,
    "note": "postazione 1",
    "name": "vr1",
    "updated": "2022-01-05T11:12:28.152+01:00",
    "added": "2021-11-10T16:17:46Z"
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃


##Creazione di un punto d'interesse
Un Point Of Interest viene utilizzato per creare un marker nella posizione desiderata da visualizzare nella pagina della realtà aumentata.



### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/poi
>```
### Body formdata


| Param | value                         | Type    |
|-------|-------------------------------|---------|
| name  | Nome del POI                  | String  |
| link  | link associato                | String  |
| src   | source path relativo immagine | String  |
| info  | informazioni associate al POI | String  |
| lat   | latitudine POI                | Numeric |
| lng   | Longitudine                   | Numeric |
| file  | icona associata al POI        | File    |



### Response: 200
```json
{
    "id": 3,
    "name": "Questo è il palazzo Pazze",
    "link": "https://xyz.github.io/artest/test.html",
    "src": "assets/asset.png",
    "location": {
        "lat": 39.169192,
        "lng": 8.526058
    },
    "info": "INFO"
}
```

Il payload restituito comprende l'ID assegnato al nuovo POI

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Lista dei punti d'interesse
Restituisce la lista dei POI registrati.

### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/pois
>```


### Response: 200
```json
[
    {
        "id": 3,
        "name": "Questo è il palazzo Pazze",
        "link": "https://xyz.github.io/artest/test.html",
        "src": "assets/asset.png",
        "location": {
            "lat": 39.169192,
            "lng": 8.526058
        },
        "info": "INFO"
    },
    {
        "id": 4,
        "name": "Questo è il laboratorio",
        "link": "https://xyz.github.io/artest/testlab.html",
        "src": "assets/lab.png",
        "location": {
            "lat": 39.169192,
            "lng": 8.526059
        },
        "info": "INFO LAB"
    }
]
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Download immagine del punto d'interesse
Restituisce l'immagine associata al punto d'interesse desiderato, identificato tramite il suo ID univoco

### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/poi/{id}/image
>http://127.0.0.1:8081/services/accesscode/api/poi/3/image
>```


### Response: 200
Viene restituito l'immagine richiesta. Negli header della response:

| header              | description                                                                                                   |
|---------------------|---------------------------------------------------------------------------------------------------------------|
| Content-Disposition | specifica che il file deve essere gestito come attachment, più nome file es. `attachment; filename="poi.jpg"` |
| Content-Type        | Media type del file scaricato, es. `image/jpeg`                                                               |
| Content-Length      | Dimensione del corpo entità, in bytes                                                                         |


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

### Response: 404
```json
"Not Found"
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

##Cancellazione di un punto d'interesse
Cancella un POI, compreso il file immagine allegato, dato il suo ID univoco

### Method: DELETE
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}
>http://127.0.0.1:8081/services/accesscode/api/experience/7
>```
### Response: 204
```json
null
```
