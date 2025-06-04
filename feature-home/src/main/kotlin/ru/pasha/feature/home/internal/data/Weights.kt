package ru.pasha.feature.home.internal.data

internal val DefaultWeights: Map<String, Map<String, Float>> = mapOf(
    "amenity" to mapOf(
        "hospital" to 1.0f,
        "police" to 0.9f,
        "pharmacy" to 0.8f,
        "school" to 0.85f,
        "bank" to 0.7f,
        "post_office" to 0.75f,
        "fuel" to 0.6f,
        "cafe" to 0.4f,
        "restaurant" to 0.5f,
        "cinema" to 0.55f,
        "theatre" to 0.6f,
        "library" to 0.65f
    ),
    "historic" to mapOf(
        "monument" to 0.7f,
        "memorial" to 0.65f,
        "castle" to 0.8f,
        "archaeological_site" to 0.75f
    ),
    "emergency" to mapOf(
        "fire_hydrant" to 0.85f,
        "phone" to 0.6f,
        "hospital" to 1.0f
    ),
    "public_transport" to mapOf(
        "station" to 0.9f,
        "stop" to 0.7f,
        "platform" to 0.6f
    ),
    "railway" to mapOf(
        "station" to 0.95f,
        "halt" to 0.7f,
        "tram_stop" to 0.75f
    ),
    "religion" to mapOf(
        "church" to 0.75f,
        "mosque" to 0.75f,
        "synagogue" to 0.75f,
        "temple" to 0.75f,
        "shrine" to 0.7f
    ),
    "shop" to mapOf("*" to 0.3f),
    "sport" to mapOf(
        "stadium" to 0.8f,
        "gym" to 0.6f,
        "swimming_pool" to 0.65f,
        "tennis" to 0.55f,
        "soccer" to 0.5f,
        "golf" to 0.5f
    ),
    "tourism" to mapOf(
        "hotel" to 0.7f,
        "guest_house" to 0.6f,
        "museum" to 0.75f,
        "attraction" to 0.65f,
        "viewpoint" to 0.6f,
        "camp_site" to 0.55f
    ),
    "water" to mapOf(
        "river" to 0.85f,
        "lake" to 0.8f,
        "pond" to 0.65f,
        "fountain" to 0.5f
    ),
    "wheelchair" to mapOf(
        "yes" to 0.4f,
        "designated" to 0.45f,
        "limited" to 0.35f
    ),
    "aeroway" to mapOf(
        "aerodrome" to 0.9f,
        "helipad" to 0.7f,
        "runway" to 0.8f
    ),
    "barrier" to mapOf(
        "fence" to 0.3f,
        "wall" to 0.4f,
        "hedge" to 0.2f,
        "gate" to 0.35f
    ),
    "cycleway" to mapOf(
        "lane" to 0.6f,
        "track" to 0.65f,
        "shared_lane" to 0.55f
    ),
    "landuse" to mapOf(
        "retail" to 0.7f,
        "industrial" to 0.75f,
        "commercial" to 0.8f,
        "residential" to 0.85f,
        "recreation_ground" to 0.6f
    ),
    "leisure" to mapOf(
        "park" to 0.8f,
        "garden" to 0.7f,
        "playground" to 0.65f,
        "sports_centre" to 0.7f,
        "stadium" to 0.75f,
        "marina" to 0.6f
    ),
    "natural" to mapOf(
        "wood" to 0.85f,
        "water" to 0.9f,
        "tree" to 0.3f,
        "beach" to 0.7f,
        "cliff" to 0.75f,
        "wetland" to 0.65f,
        "glacier" to 0.8f
    ),
    "building" to mapOf(
        "cathedral" to 0.85f,
        "chapel" to 0.7f,
        "government" to 0.9f,
        "train_station" to 0.95f,
        "stadium" to 0.8f,
        "commercial" to 0.75f
    ),
    "surface" to mapOf(
        "paved" to 0.6f,
        "asphalt" to 0.65f,
        "cobblestone" to 0.55f,
        "gravel" to 0.5f,
        "dirt" to 0.4f,
        "grass" to 0.35f
    )
)

internal val NatureWeights: Map<String, Map<String, Float>> = mapOf(
    "amenity" to mapOf(
        "hospital" to 0.3f,
        "police" to 0.2f,
        "pharmacy" to 0.25f,
        "school" to 0.3f,
        "bank" to 0.15f,
        "post_office" to 0.2f,
        "fuel" to 0.1f,
        "cafe" to 0.15f,
        "restaurant" to 0.2f,
        "cinema" to 0.15f,
        "theatre" to 0.2f,
        "library" to 0.25f
    ),
    "historic" to mapOf(
        "monument" to 0.4f,
        "memorial" to 0.35f,
        "castle" to 0.45f,
        "archaeological_site" to 0.5f
    ),
    "emergency" to mapOf(
        "fire_hydrant" to 0.6f,
        "phone" to 0.3f,
        "hospital" to 0.7f
    ),
    "public_transport" to mapOf(
        "station" to 0.4f,
        "stop" to 0.3f,
        "platform" to 0.25f
    ),
    "railway" to mapOf(
        "station" to 0.5f,
        "halt" to 0.4f,
        "tram_stop" to 0.45f
    ),
    "religion" to mapOf(
        "church" to 0.5f,
        "mosque" to 0.5f,
        "synagogue" to 0.5f,
        "temple" to 0.5f,
        "shrine" to 0.45f
    ),
    "shop" to mapOf("*" to 0.1f),
    "sport" to mapOf(
        "stadium" to 0.4f,
        "gym" to 0.3f,
        "swimming_pool" to 0.35f,
        "tennis" to 0.25f,
        "soccer" to 0.2f,
        "golf" to 0.35f
    ),
    "tourism" to mapOf(
        "hotel" to 0.4f,
        "guest_house" to 0.35f,
        "museum" to 0.45f,
        "attraction" to 0.4f,
        "viewpoint" to 0.8f,
        "camp_site" to 0.9f
    ),
    "water" to mapOf(
        "river" to 0.95f,
        "lake" to 0.9f,
        "pond" to 0.85f,
        "fountain" to 0.4f
    ),
    "wheelchair" to mapOf(
        "yes" to 0.3f,
        "designated" to 0.35f,
        "limited" to 0.25f
    ),
    "aeroway" to mapOf(
        "aerodrome" to 0.4f,
        "helipad" to 0.3f,
        "runway" to 0.35f
    ),
    "barrier" to mapOf(
        "fence" to 0.2f,
        "wall" to 0.25f,
        "hedge" to 0.15f,
        "gate" to 0.2f
    ),
    "cycleway" to mapOf(
        "lane" to 0.5f,
        "track" to 0.6f,
        "shared_lane" to 0.4f
    ),
    "landuse" to mapOf(
        "retail" to 0.3f,
        "industrial" to 0.25f,
        "commercial" to 0.2f,
        "residential" to 0.4f,
        "recreation_ground" to 0.85f
    ),
    "leisure" to mapOf(
        "park" to 0.95f,
        "garden" to 0.9f,
        "playground" to 0.7f,
        "sports_centre" to 0.6f,
        "stadium" to 0.5f,
        "marina" to 0.4f
    ),
    "natural" to mapOf(
        "wood" to 0.97f,
        "water" to 0.95f,
        "tree" to 0.6f,
        "beach" to 0.9f,
        "cliff" to 0.85f,
        "wetland" to 0.8f,
        "glacier" to 0.75f
    ),
    "building" to mapOf(
        "cathedral" to 0.4f,
        "chapel" to 0.35f,
        "government" to 0.3f,
        "train_station" to 0.5f,
        "stadium" to 0.4f,
        "commercial" to 0.2f
    ),
    "surface" to mapOf(
        "paved" to 0.3f,
        "asphalt" to 0.2f,
        "cobblestone" to 0.4f,
        "gravel" to 0.6f,
        "dirt" to 0.85f,
        "grass" to 0.9f
    )
)

internal val ResidentialWeights: Map<String, Map<String, Float>> = mapOf(
    "amenity" to mapOf(
        "hospital" to 0.6f,
        "police" to 0.5f,
        "pharmacy" to 0.4f,
        "school" to 0.55f,
        "bank" to 0.65f,
        "post_office" to 0.7f,
        "fuel" to 0.3f,
        "cafe" to 0.8f,
        "restaurant" to 0.75f,
        "cinema" to 0.85f,
        "theatre" to 0.9f,
        "library" to 0.85f
    ),
    "historic" to mapOf(
        "monument" to 0.95f,
        "memorial" to 0.85f,
        "castle" to 0.97f,
        "archaeological_site" to 0.9f
    ),
    "emergency" to mapOf(
        "fire_hydrant" to 0.7f,
        "phone" to 0.4f,
        "hospital" to 0.6f
    ),
    "public_transport" to mapOf(
        "station" to 0.95f,
        "stop" to 0.8f,
        "platform" to 0.75f
    ),
    "railway" to mapOf(
        "station" to 0.97f,
        "halt" to 0.8f,
        "tram_stop" to 0.85f
    ),
    "religion" to mapOf(
        "church" to 0.97f,
        "mosque" to 0.95f,
        "synagogue" to 0.95f,
        "temple" to 0.96f,
        "shrine" to 0.9f
    ),
    "shop" to mapOf("*" to 0.6f),
    "sport" to mapOf(
        "stadium" to 0.9f,
        "gym" to 0.6f,
        "swimming_pool" to 0.7f,
        "tennis" to 0.5f,
        "soccer" to 0.4f,
        "golf" to 0.45f
    ),
    "tourism" to mapOf(
        "hotel" to 0.9f,
        "guest_house" to 0.8f,
        "museum" to 0.97f,
        "attraction" to 0.95f,
        "viewpoint" to 0.85f,
        "camp_site" to 0.4f
    ),
    "water" to mapOf(
        "river" to 0.5f,
        "lake" to 0.45f,
        "pond" to 0.4f,
        "fountain" to 0.9f
    ),
    "wheelchair" to mapOf(
        "yes" to 0.8f,
        "designated" to 0.85f,
        "limited" to 0.7f
    ),
    "aeroway" to mapOf(
        "aerodrome" to 0.9f,
        "helipad" to 0.8f,
        "runway" to 0.85f
    ),
    "barrier" to mapOf(
        "fence" to 0.6f,
        "wall" to 0.8f,
        "hedge" to 0.4f,
        "gate" to 0.7f
    ),
    "cycleway" to mapOf(
        "lane" to 0.5f,
        "track" to 0.6f,
        "shared_lane" to 0.4f
    ),
    "landuse" to mapOf(
        "retail" to 0.9f,
        "industrial" to 0.8f,
        "commercial" to 0.95f,
        "residential" to 0.85f,
        "recreation_ground" to 0.7f
    ),
    "leisure" to mapOf(
        "park" to 0.7f,
        "garden" to 0.8f,
        "playground" to 0.6f,
        "sports_centre" to 0.75f,
        "stadium" to 0.9f,
        "marina" to 0.65f
    ),
    "natural" to mapOf(
        "wood" to 0.4f,
        "water" to 0.5f,
        "tree" to 0.3f,
        "beach" to 0.4f,
        "cliff" to 0.6f,
        "wetland" to 0.3f,
        "glacier" to 0.5f
    ),
    "building" to mapOf(
        "cathedral" to 0.99f,
        "chapel" to 0.95f,
        "government" to 0.97f,
        "train_station" to 0.98f,
        "stadium" to 0.9f,
        "commercial" to 0.93f
    ),
    "surface" to mapOf(
        "paved" to 0.9f,
        "asphalt" to 0.95f,
        "cobblestone" to 0.85f,
        "gravel" to 0.4f,
        "dirt" to 0.2f,
        "grass" to 0.3f
    )
)

internal val PedestrianWeights: Map<String, Map<String, Float>> = mapOf(
    "amenity" to mapOf(
        "hospital" to 0.4f,
        "police" to 0.3f,
        "pharmacy" to 0.35f,
        "school" to 0.4f,
        "bank" to 0.25f,
        "post_office" to 0.3f,
        "fuel" to 0.1f,
        "cafe" to 0.85f,
        "restaurant" to 0.75f,
        "cinema" to 0.65f,
        "theatre" to 0.7f,
        "library" to 0.8f
    ),
    "historic" to mapOf(
        "monument" to 0.8f,
        "memorial" to 0.75f,
        "castle" to 0.85f,
        "archaeological_site" to 0.7f
    ),
    "emergency" to mapOf(
        "fire_hydrant" to 0.7f,
        "phone" to 0.5f,
        "hospital" to 0.6f
    ),
    "public_transport" to mapOf(
        "station" to 0.8f,
        "stop" to 0.9f,
        "platform" to 0.85f
    ),
    "railway" to mapOf(
        "station" to 0.7f,
        "halt" to 0.6f,
        "tram_stop" to 0.75f
    ),
    "religion" to mapOf(
        "church" to 0.8f,
        "mosque" to 0.75f,
        "synagogue" to 0.75f,
        "temple" to 0.7f,
        "shrine" to 0.65f
    ),
    "shop" to mapOf("*" to 0.6f),
    "sport" to mapOf(
        "stadium" to 0.5f,
        "gym" to 0.4f,
        "swimming_pool" to 0.45f,
        "tennis" to 0.35f,
        "soccer" to 0.3f,
        "golf" to 0.25f
    ),
    "tourism" to mapOf(
        "hotel" to 0.7f,
        "guest_house" to 0.65f,
        "museum" to 0.9f,
        "attraction" to 0.85f,
        "viewpoint" to 0.95f,
        "camp_site" to 0.4f
    ),
    "water" to mapOf(
        "river" to 0.7f,
        "lake" to 0.65f,
        "pond" to 0.6f,
        "fountain" to 0.85f
    ),
    "wheelchair" to mapOf(
        "yes" to 0.9f,
        "designated" to 0.85f,
        "limited" to 0.7f
    ),
    "aeroway" to mapOf(
        "aerodrome" to 0.3f,
        "helipad" to 0.2f,
        "runway" to 0.25f
    ),
    "barrier" to mapOf(
        "fence" to 0.15f,
        "wall" to 0.1f,
        "hedge" to 0.05f,
        "gate" to 0.2f
    ),
    "cycleway" to mapOf(
        "lane" to 0.6f,
        "track" to 0.7f,
        "shared_lane" to 0.5f
    ),
    "landuse" to mapOf(
        "retail" to 0.7f,
        "industrial" to 0.3f,
        "commercial" to 0.6f,
        "residential" to 0.8f,
        "recreation_ground" to 0.95f
    ),
    "leisure" to mapOf(
        "park" to 0.97f,
        "garden" to 0.95f,
        "playground" to 0.9f,
        "sports_centre" to 0.6f,
        "stadium" to 0.5f,
        "marina" to 0.4f
    ),
    "natural" to mapOf(
        "wood" to 0.85f,
        "water" to 0.8f,
        "tree" to 0.7f,
        "beach" to 0.75f,
        "cliff" to 0.6f,
        "wetland" to 0.5f,
        "glacier" to 0.4f
    ),
    "building" to mapOf(
        "cathedral" to 0.9f,
        "chapel" to 0.85f,
        "government" to 0.8f,
        "train_station" to 0.95f,
        "stadium" to 0.7f,
        "commercial" to 0.6f
    ),
    "surface" to mapOf(
        "paved" to 0.95f,
        "asphalt" to 0.9f,
        "cobblestone" to 0.7f,
        "gravel" to 0.4f,
        "dirt" to 0.3f,
        "grass" to 0.5f
    )
)
