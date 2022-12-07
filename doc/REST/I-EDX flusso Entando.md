#Flusso delle chiamate REST per l'amministrazione

## 1. Gestione esperienze

[Lista esperienze](./I-EDX%20Entando.md#lista-esperienze)

[Pubblica esperienze](./I-EDX%20Entando.md#pubblica-esperienza)  
__NOTA:__ è possibile modificare lo stato dell'esperienza utilizzando anche la REST [Modifica esperienza](./I-EDX%20Entando.md#modifica-esperienza)

[Nascondi esperienze](./I-EDX%20Entando.md#rimozione-esperienza)  
__NOTA:__ è possibile modificare lo stato dell'esperienza utilizzando anche la REST [Modifica esperienza](./I-EDX%20Entando.md#modifica-esperienza)

[Cancella esperienze](./I-EDX%20Entando.md#cancellazione-esperienza)

[Dettaglio esperienza](./I-EDX%20Entando.md#dettaglio-esperienza)  
**NOTA**: viene restituito il record del database, gli asset sono ignorati

[Modifica esperienza](./I-EDX%20Entando.md#modifica-esperienza)  
**NOTA**: viene aggiornato il record del database, gli asset sono ignorati

[Dettaglio asset esperienza](./I-EDX%20Entando.md#dettaglio-asset-esperienza)

[Creazione di una nuova esperienza](./I-EDX%20Another%20Reality.md#creazione-di-una-nuova-esperienza)  
**NOTA**: Quando si crea un'esperienza si deve sempre fornire l'XML associato a essa e opzionalmente un thumbnail.
Il caricamento degli scenari (asset) può avvenire invece in qualsiasi ordine in momenti successivi.

[Caricamento o aggiornamento asset](./I-EDX%20Another%20Reality.md#caricamento-di-un-asset-di-una-esperienza)  

Questa API ha due utilizzi:
 - Caricamento (o sovrascrittura) di un nuovo asset
 - Aggiornamento dell' experience script XML e relativo thumbnail.
 

##2. Gestione dispositivi

[Lista Device](./I-EDX%20Entando.md#lista-dispositivi)

[Dettaglio Device](./I-EDX%20Entando.md#detteglio-dispositivo)

[Aggiornamento Device](./I-EDX%20Entando.md#modifica-device)

[Creazione Device](./I-EDX%20Entando.md#creazione-device)

##3. Supporto AR

Le seguenti API sono a supporto dei punti di interesse; l'edit non è supportato.

[Aggiunta di un POI](./I-EDX%20Entando.md#creazione-di-un-punto-dinteresse)

[Lista dei POI](./I-EDX%20Entando.md#lista-dei-punti-dinteresse)

[Immagine di un POI](./I-EDX%20Entando.md#download-immagine-del-punto-dinteresse)

Cancellazione POI
