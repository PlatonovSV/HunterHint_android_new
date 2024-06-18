package ru.openunity.hunterhint.ui.searchFilters

enum class SearchFilters(val id: Int) {
    MIN_PRICE(0),
    MAX_PRICE(1),
    RESOURCES_TYPE(2),
    HUNTING_METHOD(3),
    REGION(4),
    MUNICIPAL_DISTRICT(5),
    NUMBER_HUNTERS(6),
    NUMBER_GUESTS(7),
    IS_NEED_ACCOMMODATION(8),
    IS_NEED_BATHHOUSE(9),
    SUPPORT(10),
    GROUNDS_NAME(11),
    START_DATE(12),
    FINAL_DATE(13)
}