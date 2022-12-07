#Flusso delle chiamate REST per la fruizione dell'esperienza 

## 1. registrazione profilo utente [CRS4]

Il primo step è quello di registrare un profilo utente, associato a un access code.  
Questa chiamata viene fatta dal totem dove viene profilato l'utente.

[Registrazione di un profilo](./I-EDX%20CRS4.md#richiesta-accesscode)

Il messaggio restituito dovrebbe essere visualizzato nel totem perché l'utente possa leggerlo.  

Un messaggio vuoto indica l'impossibilità di assegnare una postazione all'utente, che quindi potrà usufruire dell'esperienza solo tramite dispositivo mobile.  

L'access code invece non deve essare mai visualizzato, ma usato per generare un QR code per poter usufruire dell'esperienza da un dispositivo mobile 
(a discrezione dell'utente).

## 2. ottenimento delle esperienze disponibili per l'utente [Another Reality]
Il passo successivo è quello di 'consumare' l'access code prodotto. Si distinguono due casi in base al device utilizzato:

###Oculus quest:

Ciascun dispositivo, al wake up, manda una richiesta per vedere se ci sia un utente in attesa.
Come parametro viene inviato l'id univoco del dispositivo (una stringa qualunque).

[Polling visore](./I-EDX%20Another%20Reality.md#polling-da-oculus)


###Dispositivo mobile

Qualora non sia disponibile una postazione oppure qualora l'utente decida di utilizzare il proprio device personale,
è necessario fare lo scan del QR code mostrato nel totem

[Polling external device](./I-EDX%20Another%20Reality.md#polling-da-player-esterno)

Queste due chiamate rendono lo stesso risultato.  
Oltre alla lista delle esperienze disponibili per il profilo associato, viene anche restituito l'ID relativo all'access code che viene invalidato.  
Quest'ultimo codice, assieme all'ID dell'esperienza in uso consente la creazione di eventi che saranno poi utilizzati per generare statistiche.

## 3. Dettagli esperienza [Another Reality]

A questo punto l'Oculus Quest (oppure l'app esterna installata su dispositivo esterno) ha una lista grezza di esperienze, e un l'ID dell'access code utilizzato.
Tralasciamo l'ID dell'access code utile per la creazione di eventi, e soffermiamoci sugli step necessari per poter scaricare nel device tutti gli elementi
facenti parte di un'esperienza.

Allo step precedente sono stati recuperati i seguenti parametri:

| Param       | value                       |
|-------------|-----------------------------|
| id          | id univoco esperienza       |
| name        | nome dell'esperienza        |
| description | descrizione dell'esperienza |

**NOTA**: per ciascuna esperienza viene restituita la lista degli asset (vedere dettaglio delle REST apposite) 

Questo è già sufficiente per visualizzare una lista delle esperienze sul device, qualunque esso sia; l'azione successiva è quindi quella di recuperare
il thumbnail (opzionale) di ciascuna esperienza.

[Download thumbnail](./I-EDX%20Another%20Reality.md#scarica-thumbnail-esperienza)


## 4. Download dell'esperienza sul device [Another Reality]

Siamo nella situazione in cui l'utente ha scelto di usufruire di una determinata esperienza, bisogna quindi poter scaricare tutti gli scenari (o asset) che la 
compongono.  
Lo scaricamento degli asset di una esperienza può essere effettuato in qualsiasi ordine.

[Download experience script](./I-EDX%20Another%20Reality.md#scarica-experience-script)

Scaricare l'XML dell'esperienza è necessario per poter ricavare la lista degli scenari (asset). Uno scenario da scaricare è individuato tramite il suo ID 
alfanumerico e, opzionalmente, in base al tipo.

[Download asset](./I-EDX%20Another%20Reality.md#donwload-scenario)

**NOTA:** l' experience script viene analizzato dal servizio di Entando, quindi se si volesse saltare la deserializzazione dell'XML sul device è possibile
invocare [questa REST API](./I-EDX%20Entando.md#dettaglio-scenari-esperienza) per ottenere la lista degli scenari disponibili.
In questo caso, _solo il file name (senza il path) viene restituito_!
