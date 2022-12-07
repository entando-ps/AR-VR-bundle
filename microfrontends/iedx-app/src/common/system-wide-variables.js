export const SECURITY_TOKEN = '554c8ffbe6a574c33680fb594ab8c0952b54d9301fc941a34ba27434cfd8cbca'

/* POLLING */
export const POLLING_INTERVAL = 5000
export const POLLING_INTERVAL_DEVICE = POLLING_INTERVAL
export const POLLING_INTERVAL_DEVICE_LIST = POLLING_INTERVAL
export const POLLING_INTERVAL_EXPERIENCE = POLLING_INTERVAL
export const POLLING_INTERVAL_EXPERIENCE_LIST = POLLING_INTERVAL
export const POLLING_INTERVAL_POI_LIST = POLLING_INTERVAL
export const POLLING_INTERVAL_ASSET_LIST = POLLING_INTERVAL

/* LABELS */
export const HOME_TITLE_LABEL = 'I-EDX - Pannello di controllo'
export const HOME_SUBTITLE_LABEL =
  "Prototipo realizzato nell'ambito del progetto di Ricerca e Sviluppo denominato 'I-EDX (Immersive Entando Digital Experience)', condotto da Entando e CRS4 con il supporto di AnotherReality, ed incentrato sullo studio della realtà estesa nelle sue varie declinazioni: realtà virtuale (VR), realtà aumentata (AR), realtà mista (MR)"
export const HOME_DEVICES_TITLE_LABEL = 'VR/MR - Gestione Dispositivi'
export const HOME_DEVICES_SUBTITLE_LABEL =
  'Interfaccia di aggiunta, modifica, abilitazione e rimozione dei visori per la realtà virtuale (VR) e la realtà mista (MR)'
export const HOME_EXPERIENCES_TITLE_LABEL = 'VR/MR - Gestione Esperienze'
export const HOME_EXPERIENCES_SUBTITLE_LABEL =
  'Interfaccia di aggiunta, modifica, abilitazione e rimozione delle esperienze di realtà virtuale (VR) e realtà mista (MR)'
export const HOME_POIS_TITLE_LABEL = 'AR - Gestione Punti di Interesse'
export const HOME_POIS_SUBTITLE_LABEL =
  'Interfaccia di creazione, gestione e rimozione dei punti di interesse (POI) per la realtà aumentata (AR)'

export const DEVICES_TITLE_LABEL = HOME_DEVICES_TITLE_LABEL
export const DEVICES_SUBTITLE_LABEL = HOME_DEVICES_SUBTITLE_LABEL
export const DEVICES_TABLE_TITLE_LABEL = 'Lista dei Dispositivi'
export const DEVICES_BUTTON_LABEL = 'Aggiungi Dispositivo'
export const DEVICES_NO_ITEMS = 'Il database non contiene alcun dispositivo'
export const NEW_DEVICE_TITLE_LABEL = 'Nuovo Dispositivo'
export const DEVICE_DETAIL_TITLE_LABEL = 'Dettaglio dispositivo - '

export const EXPERIENCES_TITLE_LABEL = HOME_EXPERIENCES_TITLE_LABEL
export const EXPERIENCES_SUBTITLE_LABEL = HOME_EXPERIENCES_SUBTITLE_LABEL
export const EXPERIENCES_TABLE_TITLE_LABEL = 'Lista delle Esperienze'
export const EXPERIENCES_BUTTON_LABEL = 'Aggiungi Esperienza'
export const EXPERIENCES_NO_ITEMS = 'Il database non contiene alcuna esperienza'
export const NEW_EXPERIENCE_TITLE_LABEL = 'Nuova Esperienza'
export const EXPERIENCE_DETAIL_TITLE_LABEL = 'Dettaglio esperienza - '
export const EXPERIENCE_ASSETS_NO_ITEMS = "Il database non contiene alcuno scenario per l'esperienza corrente"

export const POIS_TITLE_LABEL = HOME_POIS_TITLE_LABEL
export const POIS_SUBTITLE_LABEL = HOME_POIS_SUBTITLE_LABEL
export const POIS_TABLE_TITLE_LABEL = 'Lista dei Punti di Interesse'
export const POIS_BUTTON_LABEL = 'Aggiungi POI'
export const POIS_NO_ITEMS = 'Il database non contiene alcun punto di interesse'
export const NEW_POI_TITLE_LABEL = 'Nuovo Punto di Interesse'

export const INACTIVE = {
  value: 0,
  maleLabel: 'Disattivo',
  femaleLabel: 'Disattiva',
}
export const ACTIVE = {
  value: 1,
  maleLabel: 'Attivo',
  femaleLabel: 'Attiva',
}

export const IS_ASSET = {
  value: true,
  label: "Deselezionare per modificare il file XML e l'immagine di anteprima (thumbnail) dell'esperienza",
}

export const CREATE = 'create'
export const UPDATE = 'update'

export const BLANK_SPACE = ' '
export const MISSING_DETAIL = '--'

export const CHILD = {
  label: 'Bambino',
  value: 'CHILD',
}
export const TEEN = {
  label: 'Ragazzo',
  value: 'TEEN',
}
export const YOUNG_ADULT = {
  label: 'Giovane adulto',
  value: 'YOUNG_ADULT',
}
export const ADULT = {
  label: 'Adulto',
  value: 'ADULT',
}
export const ELDER = {
  label: 'Anziano',
  value: 'ELDER',
}

export const ASSET_TYPE_IMAGE = {
  value: 0,
  label: 'Immagine',
}
export const ASSET_TYPE_VIDEO = {
  value: 1,
  label: 'Video',
}
export const ASSET_TYPE_THUMBNAIL = {
  value: 2,
  label: 'Thumbnail',
}
export const ASSET_TYPE_SUBTITLE = {
  value: 3,
  label: 'Sottotitolo',
}
export const ASSET_TYPE_DETAILS_PANEL = {
  value: 4,
  label: 'Pannello dei dettagli',
}
