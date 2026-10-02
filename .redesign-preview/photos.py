"""References to the 15 user-selected originals; no photos are copied into Git."""
import os
from pathlib import Path

PHOTO_ROOT = Path(os.environ.get("MASJIDKU_PHOTO_DIR",
    "C:/Users/T6N0CV02S35823C/OneDrive/Gambar/Dokumentasi MA ICN"))
DRONE = "Foto Lomba Drone/Drone"
INTERIOR = "Foto Lomba Gabungan/gabungan foto"
# Focal position selects the visible portion of a portrait photo on a 16:9 screen.
PHOTOS = [
    ("roni", "Roni Supriyanto", DRONE, "Juara Favorit Roni Supriyanto.JPG", 20),
    ("jiehan", "Jiehan Herry Saputro", DRONE, "Juara Favorit Jiehan Herry Saputro.jpg", 28),
    ("nofianto", "Nofianto", DRONE, "Juara 1 Nofianto.jpg", 50),
    ("eka", "Eka Agus", DRONE, "Juara Favorit Eka Agus.JPG", 48),
    ("gerdie", "Gerdie Hutomo Nurhadi", DRONE, "Juara 2 Gerdie Hutomo Nurhadi.jpg", 33),
    ("triasdipo", "Triasdipo Oktoavianto", DRONE, "Juara 3 Triasdipo Oktoavianto.jpg", 53),
    ("edi", "Edi Sujana", INTERIOR, "Juara Favorit Edi Sujana.JPG", 68),
    ("airlangga", "Airlangga Jati Kusuma", INTERIOR, "Juara 2 Airlangga Jati Kusuma.jpg", 64),
    ("arif", "Moch Arif Muttaqin", INTERIOR, "Juara Favorit Moch Arif Muttaqin.jpg", 67),
    ("yosi", "Yosi Samsul Maarif", INTERIOR, "Juara 3 Yosi Samsul Maarif.jpg", 62),
    ("noval", "Muhamad Noval Karom", INTERIOR, "Juara Favorit Muhamad Noval Karom.JPG", 62),
    ("aceng", "Aceng Sofian", INTERIOR, "Juara Favorit Aceng Sofian.jpg", 52),
    ("yusron", "Yusron", INTERIOR, "Juara 1 Yusron.jpg", 40),
    ("syaeful", "Muh Syaeful Siddiq", INTERIOR, "Juara Favorit Muh Syaeful Siddiq.jpg", 45),
    ("ganjar", "Ganjar Mustika", INTERIOR, "Juara 3 GANJAR MUSTIKA.jpg", 35),
]
PHOTO_FILES = {key: PHOTO_ROOT / folder / filename for key, _, folder, filename, _ in PHOTOS}


def catalogue():
    return [{"id": key, "name": name, "position": position,
             "group": "Drone" if folder == DRONE else "Interior & lingkungan",
             "url": f"/.redesign-preview/photos/{key}.jpg",
             "thumbnail": f"/.redesign-preview/thumbnails/{key}.jpg",
             "available": PHOTO_FILES[key].is_file()}
            for key, name, folder, _, position in PHOTOS]
