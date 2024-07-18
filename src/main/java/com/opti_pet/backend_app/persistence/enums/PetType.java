package com.opti_pet.backend_app.persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PetType {
    // Dog Breeds
    DOG_AFGHAN_HOUND("Dog - Afghan Hound"),
    DOG_AIREDALE_TERRIER("Dog - Airedale Terrier"),
    DOG_AKITA("Dog - Akita"),
    DOG_ALASKAN_MALAMUTE("Dog - Alaskan Malamute"),
    DOG_AMERICAN_BULLDOG("Dog - American Bulldog"),
    DOG_AMERICAN_PIT_BULL_TERRIER("Dog - American Pit Bull Terrier"),
    DOG_AUSTRALIAN_SHEPHERD("Dog - Australian Shepherd"),
    DOG_BASENJI("Dog - Basenji"),
    DOG_BASSET_HOUND("Dog - Basset Hound"),
    DOG_BEAGLE("Dog - Beagle"),
    DOG_BELGIAN_MALINOIS("Dog - Belgian Malinois"),
    DOG_BICHON_FRISE("Dog - Bichon Frise"),
    DOG_BORDER_COLLIE("Dog - Border Collie"),
    DOG_BOSTON_TERRIER("Dog - Boston Terrier"),
    DOG_BOXER("Dog - Boxer"),
    DOG_BULLDOG("Dog - Bulldog"),
    DOG_BULLMASTIFF("Dog - Bullmastiff"),
    DOG_CANE_CORSO("Dog - Cane Corso"),
    DOG_CAVALIER_KING_CHARLES_SPANIEL("Dog - Cavalier King Charles Spaniel"),
    DOG_CHIHUAHUA("Dog - Chihuahua"),
    DOG_COCKER_SPANIEL("Dog - Cocker Spaniel"),
    DOG_COLLIE("Dog - Collie"),
    DOG_DACHSHUND("Dog - Dachshund"),
    DOG_DALMATIAN("Dog - Dalmatian"),
    DOG_DOBERMAN_PINSCHER("Dog - Doberman Pinscher"),
    DOG_ENGLISH_SPRINGER_SPANIEL("Dog - English Springer Spaniel"),
    DOG_FRENCH_BULLDOG("Dog - French Bulldog"),
    DOG_GERMAN_SHEPHERD("Dog - German Shepherd"),
    DOG_GOLDEN_RETRIEVER("Dog - Golden Retriever"),
    DOG_GREAT_DANE("Dog - Great Dane"),
    DOG_GREYHOUND("Dog - Greyhound"),
    DOG_HAVANESE("Dog - Havanese"),
    DOG_IRISH_SETTER("Dog - Irish Setter"),
    DOG_IRISH_WOLFHOUND("Dog - Irish Wolfhound"),
    DOG_JACK_RUSSELL_TERRIER("Dog - Jack Russell Terrier"),
    DOG_KANGAL("Dog - Kangal"),
    DOG_KOMONDOR("Dog - Komondor"),
    DOG_LABRADOR_RETRIEVER("Dog - Labrador Retriever"),
    DOG_LEONBERGER("Dog - Leonberger"),
    DOG_MALTESE("Dog - Maltese"),
    DOG_MINIATURE_PINSCHER("Dog - Miniature Pinscher"),
    DOG_MINIATURE_SCHNAUZER("Dog - Miniature Schnauzer"),
    DOG_NORWEGIAN_ELKHOUND("Dog - Norwegian Elkhound"),
    DOG_NEWFOUNDLAND("Dog - Newfoundland"),
    DOG_OLD_ENGLISH_SHEEPDOG("Dog - Old English Sheepdog"),
    DOG_PAPILLON("Dog - Papillon"),
    DOG_PEKEFACE("Dog - Pekeface"),
    DOG_PEKINGESE("Dog - Pekingese"),
    DOG_PHARAOH_HOUND("Dog - Pharaoh Hound"),
    DOG_PLOTT("Dog - Plott"),
    DOG_POINTER("Dog - Pointer"),
    DOG_POMERANIAN("Dog - Pomeranian"),
    DOG_POODLE("Dog - Poodle"),
    DOG_PORTUGUESE_WATER_DOG("Dog - Portuguese Water Dog"),
    DOG_PUG("Dog - Pug"),
    DOG_PULI("Dog - Puli"),
    DOG_RHODESIAN_RIDGEBACK("Dog - Rhodesian Ridgeback"),
    DOG_ROTTWEILER("Dog - Rottweiler"),
    DOG_SAINT_BERNARD("Dog - Saint Bernard"),
    DOG_SALUKI("Dog - Saluki"),
    DOG_SAMOYED("Dog - Samoyed"),
    DOG_SCHIPPERKE("Dog - Schipperke"),
    DOG_SCOTTISH_TERRIER("Dog - Scottish Terrier"),
    DOG_SHAR_PEI("Dog - Shar Pei"),
    DOG_SHETLAND_SHEEPDOG("Dog - Shetland Sheepdog"),
    DOG_SHIBA_INU("Dog - Shiba Inu"),
    DOG_SHIH_TZU("Dog - Shih Tzu"),
    DOG_SIBERIAN_HUSKY("Dog - Siberian Husky"),
    DOG_SOFT_COATED_WHEATEN_TERRIER("Dog - Soft Coated Wheaten Terrier"),
    DOG_SPINONE_ITALIANO("Dog - Spinone Italiano"),
    DOG_STAFFORDSHIRE_BULL_TERRIER("Dog - Staffordshire Bull Terrier"),
    DOG_TIBETAN_MASTIFF("Dog - Tibetan Mastiff"),
    DOG_VIZSLA("Dog - Vizsla"),
    DOG_WEIMARANER("Dog - Weimaraner"),
    DOG_WELSH_CORGI("Dog - Welsh Corgi"),
    DOG_WELSH_TERRIER("Dog - Welsh Terrier"),
    DOG_WEST_HIGHLAND_WHITE_TERRIER("Dog - West Highland White Terrier"),
    DOG_WHIPPET("Dog - Whippet"),
    DOG_YORKSHIRE_TERRIER("Dog - Yorkshire Terrier"),

    // Cat Breeds
    CAT_ABYSSINIAN("Cat - Abyssinian"),
    CAT_AMERICAN_BOBTAIL("Cat - American Bobtail"),
    CAT_AMERICAN_CURL("Cat - American Curl"),
    CAT_AMERICAN_SHORTHAIR("Cat - American Shorthair"),
    CAT_AMERICAN_WIREHAIR("Cat - American Wirehair"),
    CAT_BALINESE("Cat - Balinese"),
    CAT_BENGAL("Cat - Bengal"),
    CAT_BIRMAN("Cat - Birman"),
    CAT_BOMBAY("Cat - Bombay"),
    CAT_BRITISH_LONGHAIR("Cat - British Longhair"),
    CAT_BRITISH_SHORTHAIR("Cat - British Shorthair"),
    CAT_BURMESE("Cat - Burmese"),
    CAT_BURMILLA("Cat - Burmilla"),
    CAT_CHANTILLY("Cat - Chantilly"),
    CAT_CHARTREUX("Cat - Chartreux"),
    CAT_CORNISH_REX("Cat - Cornish Rex"),
    CAT_CYMRIC("Cat - Cymric"),
    CAT_DEVON_REX("Cat - Devon Rex"),
    CAT_EGYPTIAN_MAU("Cat - Egyptian Mau"),
    CAT_EUROPEAN_BURMESE("Cat - European Burmese"),
    CAT_EXOTIC_SHORTHAIR("Cat - Exotic Shorthair"),
    CAT_GERMAN_REX("Cat - German Rex"),
    CAT_HIMALAYAN("Cat - Himalayan"),
    CAT_JAPANESE_BOBTAIL("Cat - Japanese Bobtail"),
    CAT_JAVANESE("Cat - Javanese"),
    CAT_KORAT("Cat - Korat"),
    CAT_KURILIAN_BOBTAIL("Cat - Kurilian Bobtail"),
    CAT_LA_PERM("Cat - LaPerm"),
    CAT_LYKOI("Cat - Lykoi"),
    CAT_MAINE_COON("Cat - Maine Coon"),
    CAT_MANX("Cat - Manx"),
    CAT_MINUET("Cat - Minuet"),
    CAT_NEBELUNG("Cat - Nebelung"),
    CAT_NORWEGIAN_FOREST_CAT("Cat - Norwegian Forest Cat"),
    CAT_OCICAT("Cat - Ocicat"),
    CAT_ORIENTAL("Cat - Oriental"),
    CAT_PERSIAN("Cat - Persian"),
    CAT_PETERBALD("Cat - Peterbald"),
    CAT_RAGAMUFFIN("Cat - Ragamuffin"),
    CAT_RAGDOLL("Cat - Ragdoll"),
    CAT_RUSSIAN_BLUE("Cat - Russian Blue"),
    CAT_SAVANNAH("Cat - Savannah"),
    CAT_SCOTTISH_FOLD("Cat - Scottish Fold"),
    CAT_SELKIRK_REX("Cat - Selkirk Rex"),
    CAT_SIAMESE("Cat - Siamese"),
    CAT_SIBERIAN("Cat - Siberian"),
    CAT_SINGAPURA("Cat - Singapura"),
    CAT_SNOWSHOE("Cat - Snowshoe"),
    CAT_SOMALI("Cat - Somali"),
    CAT_SPHYNX("Cat - Sphynx"),
    CAT_TONKINESE("Cat - Tonkinese"),
    CAT_TURKISH_ANGORA("Cat - Turkish Angora"),
    CAT_TURKISH_VAN("Cat - Turkish Van");

    private final String breed;

    PetType(String breed) {
        this.breed = breed;
    }

    @JsonCreator
    public static PetType fromValue(String value) {
        for (PetType petTypeEnum : values()) {
            String currentPetType = petTypeEnum.getBreed();
            if (currentPetType.equalsIgnoreCase(value)) {
                return petTypeEnum;
            }
        }

        throw new IllegalArgumentException("Invalid value for Pet Type Enum: " + value);
    }

    @JsonValue
    public String getBreed() {
        return breed;
    }
}
