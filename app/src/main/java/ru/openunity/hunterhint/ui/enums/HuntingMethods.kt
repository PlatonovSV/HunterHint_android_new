package ru.openunity.hunterhint.ui.enums

import androidx.annotation.StringRes
import ru.openunity.hunterhint.R

enum class HuntingMethods(val id: Int, @StringRes val stringRes: Int) {
    FROM_THE_APPROACH(0, R.string.from_the_approach),
    ENTRANCE_BY_HORSE_DRAWN_TRANSPORT(1, R.string.entrance_by_horse_drawn_transport),
    FROM_AMBUSH(2, R.string.from_ambush),
    BY_PEN(3, R.string.by_pen),
    BY_CORRAL(4, R.string.by_corral),
    FROM_THE_ENTRANCE_BY_HORSE_DRAWN_TRANSPORT(
        5,
        R.string.from_the_entrance_by_horse_drawn_transport
    ),
    CORALIZATION(6, R.string.coralization),
    AT_CROSSINGS_USING_FLOATING_MEANS(7, R.string.at_crossings_using_floating_means),
    WITH_A_DECOY(8, R.string.with_a_decoy),
    ON_THE_DEN(9, R.string.on_the_den),
    USING_FLOATING_VEHICLES_WITH_THE_ENGINE_OFF(
        10,
        R.string.using_floating_vehicles_with_the_engine_off
    ),
    SELF_PROPELLED_GUNS(11, R.string.self_propelled_guns),
    TRAPS_SELF_PROPELLED_GUNS(12, R.string.traps_self_propelled_guns),
    WITH_HUNTING_BIRDS(13, R.string.with_hunting_birds),
    USING_DOG_SLEDS(14, R.string.using_dog_sleds),
    CORRALING_OF_LYNX_AND_WILD_CATS(15, R.string.corraling_of_lynx_and_wild_cats),
    WITH_THE_USE_OF_LIGHT_DEVICES_(16, R.string.with_the_use_of_light_devices_),
    ON_DENS(17, R.string.on_dens),
    ON_WABU(18, R.string.on_wabu),
    WITH_DOGS(19, R.string.with_dogs),
    ON_CURRENT(20, R.string.on_current),
    FROM_HIDING(21, R.string.from_hiding),
    ON_EVENING_AND_MORNING_TRACTION(22, R.string.on_evening_and_morning_traction),
    FROM_AMBUSH_FROM_HIDING(23, R.string.from_ambush_from_hiding),
    ON_FLIGHTS(24, R.string.on_flights),
    WITH_THE_USE_OF_FLOATING_VEHICLES_WITH_THE_ENGINE_OFF(
        25,
        R.string.with_the_use_of_floating_vehicles_with_the_engine_off
    ),
    WITH_DECOY_MANNA_BIRDS(26, R.string.with_decoy_manna_birds),
    WITH_STUFFED_ANIMALS_AND_PROFILES(27, R.string.with_stuffed_animals_and_profiles),
    SAMOLOV_PARTRIDGES(28, R.string.samolov_partridges)
}

fun getMethodsById(id: Int) = HuntingMethods.entries.find { it.id == id } ?: HuntingMethods.BY_PEN