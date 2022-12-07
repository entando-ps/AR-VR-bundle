# Project: I-EDX Another Reality

## Creazione di una nuova esperienza
Crea una nuova esperienza.
I parametri **nome**, **descrizione** e **thumbnailFile** sono opzionali. Nome e descrizione sono recuperati dall'experience script,
quando presenti.
I vari asset e relativi thumbnail che compongono l'esperienza possono essere caricati in momenti successivi e in qualsiasi ordine.

**NOTA:** Il nome e la descrizione passati via REST hanno priorità maggiore rispetto a quelli specificati nell' experience script.
### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/experience
>```
### Body formdata

| Param         | value                                                | Type |
|---------------|------------------------------------------------------|------|
| xmlFile       | /home/user/experiences/config.xml                    | file |
| name          | esperienza                                           | text |
| description   | descrizione esperienza                               | text |
| thumbnailFile | /home/user/experiences/Entando_Logo_Dark_Blue@4x.png | file |



### Response: 200
```json
{
    "name": "YOUNG",
    "description": "descrizione esperienza YOUNG",
    "id": 4,
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
            "assetPresentOnDisk": false,
            "thumbnailPresentOnDisk": false,
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
            "age": "TEEN"
        },
        {
            "age": "ADULT"
        }
    ]
}
```

### Profile


| Param     | value                                                                             | Type   | Mandatory |
|-----------|-----------------------------------------------------------------------------------|--------|-----------|
| age       | CHILD, TEEN, YOUNG_ADULT, ADULT, ELDER                                            | string | S         |
| gender    | MALE, FEMALE, UNKNOWN                                                             | string | N         |
| race      | EAST_ASIAN, SOUTHEAST_ASIAN, INDIAN, BLACK, WHITE, MIDDLE_EASTERN, LATINO_ISPANIC | string | N         |
| sentiment | ANGRY, DISGUST, FEAR, HAPPY, SAD, SURPRISE, NEUTRAL                               | string | N         |

**NOTA**: solo `age` è obbligatorio quando si compila un profile per un' esperienza

### Asset

| Param                  | value                                                                                                  | Type    |
|------------------------|--------------------------------------------------------------------------------------------------------|---------|
| id                     | ID univoco dell' asset, da utilizzare per le operazioni di caricamento                                 | string  |
| type                   | 0=IMAGE, 1=VIDEO, 2=HOTSPOT, 3=SUBTITLE, 4=DETAILS_PANEL                                               | numeric |
| assetFilename          | nome del file dell' asset su filesystem; le directory specificate nell'XML non sono riportate          | numeric |
| thumbnailFilename      | nome del file del thumbnail su filesystem; le directory specificate nell'XML non sono riportate        | numeric |
| assetPresentOnDisk     | TRUE = l'asset è stato caricato sul disco FALSE = asset non trovato su filesystem                      | boolean |
| thumbnailPresentOnDisk | TRUE = il thumbnail dell'asset è stato caricato sul disco, FALSE = thumbnail non trovato su filesystem | boolean |
| title                  | Titolo                                                                                                 | string  |


La risposta specifica l'ID dell'esperienza appena creata e la lista degli asset attesi.

### Response: 201
```json
"Created"
```

### Response: 401
```json
"Unauthorized"
```

### Response: 403
```json
"Forbidden"
```

### Response: 404
```json
"Not Found"
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Caricamento di un asset di una esperienza
Questo endpoint permette: 
 - il caricamento di uno scenario (image, video) o asset (hotspot, subtitle) 
 - l'aggiornamento dell' experience script e thumbnail di una esperienza.

Il tipo di operazione da eseguire dipende dal _parametro opzionale_ `isAsset` (default = TRUE)  
In ogni caso come risultato viene restituita la lista aggiornata degli asset (o un errore)

#### Caricamento scenario o asset (isAsset = true, opzionale)

Quando viene creato uno scenario vuoto nel database, l'XML fornito viene deserializzato dal sistema in modo
da avere la lista preventiva dei file che costituiscono questa esperienza.  
Questo permette di poter caricare un file facendo riferimento all'ID dell'esperienza: se il nome di tale file non è conosciuto
allora l'upload fallisce con un errore `400`.

#### Aggiornamento experience script o thumbnail (isAsset = false)
__Siccome è sempre possibile sovrascrivere un asset precedentemente caricato__ è possibile utilizzare questa API per l'aggiornamento dell' experience file (e
thumbnail) della relativa esperienza, purché l'XML (e il thubnail quando fornito) abbiano lo stesso nome fornito durante la creazione. Ad esempio se all'atto della creazione l'experience script è stato fornito con nome `config.xml` lo stesso nome deve essere usato invocando questa API.  
Nel caso il nome dell' experience script (o del thumbnail) differiscano da quelli attesi si avrà un errore `400`

#### NOTE:
- Quando si aggiorna l'experience script gli asset preesistenti non presenti nell'aggiornamento verranno cancellati (thumbnail compreso)
- Per gli asset di tipo scenario (IMAGE, VIDEO) è possibile specificare un thumbnail, tuttavia al momento questo non è recuperabile tramite REST.


### Method: PUT
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/asset
>http://127.0.0.1:8081/services/accesscode/api/experience/5/asset
>```
### Body formdata

| Param         | value                                                                     | Type    |
|---------------|---------------------------------------------------------------------------|---------|
| file          | /home/matteo/Pictures/x-mark-128.png                                      | file    |
| thumbnailFile | /home/user/experiences/Images360/witcher_thumb.jpg                        | file    |

### Query String

| Param         | value                                                                     | Type    |
|---------------|---------------------------------------------------------------------------|---------|
| isAsset       | TRUE=asset and thumbnail update FALSE= experience XML or thumbnail update | boolean |


### Response: 200
```json
{
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
            "assetPresentOnDisk": true,
            "thumbnailPresentOnDisk": true,
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
    ]
}
```


| Param                  | value                                                                                                  | Type    |
|------------------------|--------------------------------------------------------------------------------------------------------|---------|
| id                     | ID univoco dell' asset                                                                                 | string  |
| type                   | 0=IMAGE, 1=VIDEO, 2=HOTSPOT, 3=SUBTITLE, 4=DETAILS_PANEL                                               | numeric |
| assetFilename          | nome del file dell' asset su filesystem; le directory specificate nell'XML non sono riportate          | numeric |
| thumbnailFilename      | nome del file del thumbnail su filesystem; le directory specificate nell'XML non sono riportate        | numeric |
| assetPresentOnDisk     | TRUE = l'asset è stato caricato sul disco FALSE = asset non trovato su filesystem                      | boolean |
| thumbnailPresentOnDisk | TRUE = il thumbnail dell'asset è stato caricato sul disco, FALSE = thumbnail non trovato su filesystem | boolean |
| title                  | Titolo                                                                                                 | string  |

Se l'operazione ha successo viene sempre restituita la lista aggiornata degli asset per l'esperienza corrente

### Response: 400
```
'Bad Request'
```
L'esperienza _non è stata aggiornata_ perché il file non è presente fra quelli dichiarati nell' experience script oppure l'esperienza stessa specificata nel path non esiste.

### Response: 413
```
'Request entity too large'
```

Si sta cercando do fare l'upload di un file troppo grande

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Download asset
Scarica un asset (o scenario), individuato tramite ID univoco, dell'esperienza desiderata.
L'ID dell'asset è proprio quello dichiarato nell' experience script associato all'esperienza.
Qualora due elementi di tipo differente di scenario condividano lo stesso ID, si ricorre al parametro opzionale `type` per specificare il tipo desiderato, altrimenti il sistema restituisce il primo file che rispetta la ricerca indipendentemente dal tipo.

__NOTA:__ per lo scaricamento dell' experience script o thumbnail usare le apposite api sottostanti. 

### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/asset/{ID ASSET}?type={TIPO ASSET}
>http://127.0.0.1:8081/services/accesscode/api/experience/5/asset/hangar?type=IMAGE
>```
### Query Params

| Param | value                                          | Type   |
|-------|------------------------------------------------|--------|
| type  | IMAGE, VIDEO, HOTSPOT, SUBTITLE, DETAILS_PANEL | string |

### Response: 200
Viene restituito lo scenario richiesto. Negli header della response:

| header              | description                                                                                                      |
|---------------------|------------------------------------------------------------------------------------------------------------------|
| Content-Disposition | specifica che il file deve essere gestito come attachment, più nome file es. `attachment; filename="hangar.jpg"` |
| Content-Type        | Media type del file scaricato, es. `image/jpeg`                                                                  |
| Content-Length      | Dimensione del corpo entità, in bytes                                                                            |

### Response: 404
Il tipo di asset richiesto non è noto al sistema.

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Download thumbnail di un asset 
Scarica il thumbnail di un asset (o scenario), individuato tramite ID univoco, dell'esperienza desiderata.
L'ID dell'asset è proprio quello dichiarato nell' experience script associato all'esperienza.
Qualora due elementi di tipo differente di scenario condividano lo stesso ID, si ricorre al parametro opzionale `type` per specificare il tipo desiderato, altrimenti il sistema restituisce il primo file che rispetta la ricerca indipendentemente dal tipo.

__NOTA:__ per lo scaricamento dell' experience script o thumbnail usare le apposite api sottostanti.

### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/asset/thumbnail/{ID ASSET}?type={TIPO ASSET}
>http://127.0.0.1:8081/services/accesscode/api/experience/5/asset/thumbnail/hangar?type=IMAGE
>```
### Query Params

| Param | value                                          | Type   |
|-------|------------------------------------------------|--------|
| type  | IMAGE, VIDEO, HOTSPOT, SUBTITLE, DETAILS_PANEL | string |

__NOTA__: Il `type` si riferisce sempre al tipo dell'asset, e non al tipo di thumbnail (che è sempre immagine!)

### Response: 200
Viene restituito lo scenario richiesto. Negli header della response:

| header              | description                                                                                                      |
|---------------------|------------------------------------------------------------------------------------------------------------------|
| Content-Disposition | specifica che il file deve essere gestito come attachment, più nome file es. `attachment; filename="hangar.jpg"` |
| Content-Type        | Media type del file scaricato, es. `image/jpeg`                                                                  |
| Content-Length      | Dimensione del corpo entità, in bytes                                                                            |

### Response: 404
Il tipo di asset richiesto non è noto al sistema.

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Scarica experience script
Scarica l' experience script in formato XML proprio dell'esperienza desiderata
### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/xml
>http://127.0.0.1:8081/services/accesscode/api/experience/5/xml
>```

### Response: 200

Viene restituito il file XML richiesto. Negli header della response:

| header              | description                                                                                                      |
|---------------------|------------------------------------------------------------------------------------------------------------------|
| Content-Disposition | specifica che il file deve essere gestito come attachment, più nome file es. `attachment; filename="confix.xml"` |
| Content-Type        | Media type del file scaricato, es. `application/xml`                                                             |
| Content-Length      | Dimensione del corpo entità, in bytes                                                                            |

### Response: 404
Esperienza sconosciuta

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Scarica thumbnail esperienza
Scarica il thumbnail associato all'esperienza
### Method: GET
>```
>http://127.0.0.1:8081/services/accesscode/api/experience/{ID ESPERIENZA}/thumbnail
>http://127.0.0.1:8081/services/accesscode/api/experience/5/thumbnail
>```

### Response: 200

Viene restituito il thumbnail richiesto. Negli header della response:

| header              | description                                                                                                         |
|---------------------|---------------------------------------------------------------------------------------------------------------------|
| Content-Disposition | specifica che il file deve essere gestito come attachment, più nome file es. `attachment; filename="thumbnail.jpg"` |
| Content-Type        | Media type del file scaricato, es. `image/jpeg`                                                                     |
| Content-Length      | Dimensione del corpo entità, in bytes                                                                               |

### Response: 404
Esperienza sconosciuta

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
## Polling da Oculus

Questa invocazione viene fatta passando come parametro **l'identificativo univoco del visore** allo scopo di verificare la presenza di un utente in coda per l'utilizzo.

In caso affermativo viene restituita la lista delle esperienze disponibili per la fruizione insieme all'ID dell'access code che viene consumato.

NOTA: questa chiamata può essere effettuata solo una volta in quanto l'access code associato all'utente viene immediatamente invalidato.

### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/login
>```
### Body (**raw**)

```json
{
  "token": "device#3"
}
```
In questo caso il caso token contiene **l'ID univoco** del visore che sta effettuando il polling verso Entando.

### Response: 200
```json
{
    "accessCodeId": 25,
    "experiences": [
        {
            "description": "descrizione esperienza",
            "name": "esperienza",
            "id": 8
        },
        {
            "description": "descrizione esperienza nuova",
            "name": "nuova",
            "id": 18
        }
    ]
}
```

``accessCodeId`` è l'ID (chiave primaria) associato all'access code che viene consumato. 

``id`` è l'ID (chiave primaria) dell'esperienza, da utilizzare in tutte le REST API che coinvolgono un'esperienza.

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Polling da player esterno 
Questa invocazione viene fatta passando come parametro **l'access code** per poter riprodurre l'esperienza in un device diverso dall'Oculus (es. dispositivo mobile).

In caso affermativo viene restituita la lista (eventualmente vuota) delle esperienze disponibili per la fruizione.

NOTA: questa chiamata può essere effettuata solo una volta in quanto l'access code associato all'utente viene immediatamente invalidato.

### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/ext/login
>```
### Body (**raw**)

```json
{
  "token": "NICE"
}
```
In questo caso il caso token contiene **l'access code** così come estratto dal QR code.

### Response: 200
```json
{
  "accessCodeId": 25,
  "experiences": [
    {
      "description": "descrizione esperienza",
      "name": "esperienza",
      "id": 8
    },
    {
      "description": "descrizione esperienza nuova",
      "name": "nuova",
      "id": 18
    }
  ]
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Creazione di un evento
Questa API permette di registrare eventi durante la fruizione di una esperienza: a ciascun evento corrisponde
un record sul database.

**Importante:** 
 - tipi di evento diversi coinvolgono colonne diverse del database (vedere tabella di sotto)
 - non bisogna passare il timestamp poiché questo viene impostato automaticamente alla ricezione
 - I campi `accessCodeId` e `experienceId` devono essere sempre presenti
 - Il campo `eventName` è sempre opzionale

La seguente tabella mostra quali campi sono obbligatori (`S`) a seconda del tipo. I campi non obbligatori, quando specificati, sono 
comunque salvati nel database.

| Type                  | scenarioElementId | choiceId  |
|-----------------------|-------------------|-----------|
| EXPERIENCE_START      | 	N                | S         |
| EXPERIENCE_END        | 	N                | N         |
| SCENARIO_SELECTION    | 	N                | S         |
| RESTART_VIDEO         | 	N                | N         |
| PAUSE                 | 	N                | N         |
| RESUME                | 	N                | N         |
| SKIP_FORWARD          | 	N                | N         |
| SKIP_BACKWARD         | 	N                | N         |
| START_SCENARIO        | 	N                | N         |
| HOTSPOT_SELECTION     | 	S                | N         |
| OPTIONSPOPUP_CHOICE   | 	S                | S         | 
| DETAILSPANEL_EXPAND   | 	S                | N         |
| DETAILSPANEL_MINIMIZE | 	S                | N         |


### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/event
>```
### Body (**raw**)

```json
{
    "accessCodeId": 2,
    "choiceId": "choice-2",
    "event": "string",
    "experienceId": 3,
    "menuId": "menu-1",
    "type": "RESTART_VIDEO"
}
```

### Body formdata

| Param        | value                                                                                                                                         | Type    |
|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------|---------|
| accessCodeId | ID dell'acess code                                                                                                                            | numeric |
| experienceId | ID dell'esperienza al quale appartiene l'evento                                                                                               | numeric |
| type         | 0=EXPERIENCE_START, 1=EXPERIENCE_END, 2=HOTSPOT_SELECTION, 3=PAUSE, 4=RESUME, 5=SKIP_FORWARD, 6=SKIP_BACKWARD, 7=MENU_CHOICE, 8=RESTART_VIDEO | string  |
| menuId       | ID del meni al quale si riferisce l'evento                                                                                                    | string  |
| choiceId     | ID della scelta selezionata                                                                                                                   | string  |
| event        | (opzionale) Stringa descrittiva                                                                                                               | string  |


### Response: 200
```json
{
  "id": 2,
  "experienceId": 3,
  "accessCodeId": 2,
  "date": "2022-02-04T15:02:45.234+01:00",
  "event": "string",
  "type": "RESTART_VIDEO",
  "menuId": "menu-1",
  "choiceId": "choice-2"
}
```


### Response: 400
```
Bad Request
```

Questo errore viene restituito quando uno o più campi obbligatori non sono stati specificati

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Modifica stato dell' access code
Questa chiamata permette di cambiare lo stato di utilizzo dell'access token, principalmente al fine di specificare quando la fruizione dell'esperienza è
terminata. Lo stato iniziale di attesa e quello di utilizzo sono infatti settati automaticamente, mentre quello di terminazione
deve essere inviato quando la visualizzazione dell' esperienza viene conclusa.  
Considerando che nel momento in cui viene creato un access token viene anche assegnato, quando possibile, un dispositivo per la visualizzazione, l'effetto collaterale è quello di svincolare il dispositivo assegnato e renderlo nuovamente disponibile per l'uso da parte di altri utenti.

La chiamata prevede di specificare l'`ID` dell'access code e lo stato da impostare nel payload, secondo la seguente tabella:  

| Value | Mnemonic | Description                         |
|-------|----------|-------------------------------------|
| 0     | PENDING  | Access code creato                  |
| 1     | INUSE    | access code in uso                  |
| 2     | RELEASED | access code utilizzato              |
| 3     | EXTERN   | access code per dispositivo esterno |


### Method: PUT
>```
>http://127.0.0.1:8081/services/accesscode/api/accesscode
>```

### Body (**raw**)

```json
{
    "id": 2,
    "status": 1
}
```

### Response: 200
```json
{
    "id": 2,
    "status": 2
}
```
Viene restituito l'id e lo stato attuale dell'access code interessato.

| Value | Mnemonic | Description                         |
|-------|----------|-------------------------------------|
| 0     | PENDING  | Access code creato                  |
| 1     | INUSE    | access code in uso                  |
| 2     | RELEASED | access code utilizzato              |
| 3     | EXTERN   | access code per dispositivo esterno |

### Response: 404
```
'Not Found'
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## Keep alive
Chiamata per il keep alive, in ingresso bisogna passare l'ID dell'access code che è in fase di utilizzo.

**NOTA**:
- dopo 180 secondi il token in stato `INUSE` viene dichiarato scaduto
- dopo 300 secondi il token in stato `PENDING` viene dichiarato scaduto

### Method: POST
>```
>http://127.0.0.1:8081/services/accesscode/api/ping/{id}
>http://127.0.0.1:8081/services/accesscode/api/ping/1
>```

### Response: 200
```json
{
    "id": 1,
    "status": 3
}
```
Viene restituito l'id e lo stato attuale dell'access code interessato.

| Value | Mnemonic | Description                         |
|-------|----------|-------------------------------------|
| 0     | PENDING  | Access code creato                  |
| 1     | INUSE    | access code in uso                  |
| 2     | RELEASED | access code utilizzato              |
| 3     | EXTERN   | access code per dispositivo esterno |

### Response: 404
```
'Not Found'
```
