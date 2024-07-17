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
    DOG_JACK_RUSSELL_TERRIER("Dog - Jack Russell Terrier"),
    DOG_LABRADOR_RETRIEVER("Dog - Labrador Retriever"),
    DOG_MALTESE("Dog - Maltese"),
    DOG_MINIATURE_SCHNAUZER("Dog - Miniature Schnauzer"),
    DOG_NEWFOUNDLAND("Dog - Newfoundland"),
    DOG_PAPILLON("Dog - Papillon"),
    DOG_PEKINGESE("Dog - Pekingese"),
    DOG_POMERANIAN("Dog - Pomeranian"),
    DOG_POODLE("Dog - Poodle"),
    DOG_PUG("Dog - Pug"),
    DOG_ROTTWEILER("Dog - Rottweiler"),
    DOG_SAMOYED("Dog - Samoyed"),
    DOG_SCHIPPERKE("Dog - Schipperke"),
    DOG_SHIBA_INU("Dog - Shiba Inu"),
    DOG_SHIH_TZU("Dog - Shih Tzu"),
    DOG_SIBERIAN_HUSKY("Dog - Siberian Husky"),
    DOG_STAFFORDSHIRE_BULL_TERRIER("Dog - Staffordshire Bull Terrier"),
    DOG_WEIMARANER("Dog - Weimaraner"),
    DOG_WEST_HIGHLAND_WHITE_TERRIER("Dog - West Highland White Terrier"),
    DOG_YORKSHIRE_TERRIER("Dog - Yorkshire Terrier"),

    // Cat Breeds
    CAT_ABYSSINIAN("Cat - Abyssinian"),
    CAT_AMERICAN_BOBTAIL("Cat - American Bobtail"),
    CAT_AMERICAN_SHORTHAIR("Cat - American Shorthair"),
    CAT_AMERICAN_WIREHAIR("Cat - American Wirehair"),
    CAT_BALINESE("Cat - Balinese"),
    CAT_BENGAL("Cat - Bengal"),
    CAT_BIRMAN("Cat - Birman"),
    CAT_BOMBAY("Cat - Bombay"),
    CAT_BRITISH_SHORTHAIR("Cat - British Shorthair"),
    CAT_BURMESE("Cat - Burmese"),
    CAT_BURMILLA("Cat - Burmilla"),
    CAT_CHARTREUX("Cat - Chartreux"),
    CAT_CORNISH_REX("Cat - Cornish Rex"),
    CAT_DEVON_REX("Cat - Devon Rex"),
    CAT_EGYPTIAN_MAU("Cat - Egyptian Mau"),
    CAT_EXOTIC_SHORTHAIR("Cat - Exotic Shorthair"),
    CAT_HIMALAYAN("Cat - Himalayan"),
    CAT_JAPANESE_BOBTAIL("Cat - Japanese Bobtail"),
    CAT_KORAT("Cat - Korat"),
    CAT_LA_PERM("Cat - LaPerm"),
    CAT_MAINE_COON("Cat - Maine Coon"),
    CAT_MANX("Cat - Manx"),
    CAT_NORWEGIAN_FOREST_CAT("Cat - Norwegian Forest Cat"),
    CAT_OCICAT("Cat - Ocicat"),
    CAT_ORIENTAL("Cat - Oriental"),
    CAT_PERSIAN("Cat - Persian"),
    CAT_RAGDOLL("Cat - Ragdoll"),
    CAT_RUSSIAN_BLUE("Cat - Russian Blue"),
    CAT_SAVANNAH("Cat - Savannah"),
    CAT_SCOTTISH_FOLD("Cat - Scottish Fold"),
    CAT_SIAMESE("Cat - Siamese"),
    CAT_SIBERIAN("Cat - Siberian"),
    CAT_SINGAPURA("Cat - Singapura"),
    CAT_SOMALI("Cat - Somali"),
    CAT_SPHYNX("Cat - Sphynx"),
    CAT_TONKINESE("Cat - Tonkinese"),
    CAT_TURKISH_ANGORA("Cat - Turkish Angora"),
    CAT_TURKISH_VAN("Cat - Turkish Van");

    private final String petType;

    PetType(String petType) {
        this.petType = petType;
    }

    @JsonCreator
    public static PetType fromValue(String value) {
        for (PetType petTypeEnum : values()) {
            String currentPetType = petTypeEnum.getPetType();
            if (currentPetType.equalsIgnoreCase(value)) {
                return petTypeEnum;
            }
        }

        throw new IllegalArgumentException("Invalid value for Pet Type Enum: " + value);
    }

    @JsonValue
    public String getPetType() {
        return petType;
    }
}
