## Flusso chiamate REST per il caricamento di un'esperienza su Entando

Quando si crea un'esperienza si deve sempre fornire l'XML associato ad essa e opzionalmente un thumbnail.
Il caricamento degli scenari (asset) può avvenire invece in qualsiasi ordine.

[Creazione di una nuova esperienza](./I-EDX%20Another%20Reality.md#creazione-di-una-nuova-esperienza)

Quando viene creato uno scenario vuoto nel database, l'XML fornito viene deserializzato dal sistema in modo
da avere la lista preventiva dei file che costituiscono questa esperienza.  


Per ogni scenario è possibile specificare un thumbnail, tuttavia al momento questo non è recuperabile tramite REST.

[Caricamento o aggiornamento asset](./I-EDX%20Another%20Reality.md#caricamento-di-un-asset-di-una-esperienza)

Questa API ha due utilizzi:
- Caricamento (o sovrascrittura) di un nuovo asset
- Aggiornamento dell' experience script XML e relativo thumbnail.  

[Download di un asset](I-EDX%20Another%20Reality.md#donwload-asset)

[Pubblica esperienze](./I-EDX%20Entando.md#pubblica-esperienza)  
__NOTA:__ è possibile modificare lo stato dell'esperienza utilizzando anche la REST [Modifica esperienza](./I-EDX%20Entando.md#modifica-esperienza)

[Nascondi esperienze](./I-EDX%20Entando.md#rimozione-esperienza)  
__NOTA:__ è possibile modificare lo stato dell'esperienza utilizzando anche la REST [Modifica esperienza](./I-EDX%20Entando.md#modifica-esperienza)


